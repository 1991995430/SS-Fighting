package com.ss.song.utils;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * SSH工具类
 *
 * @author wang.sheng
 *
 */
public final class SshUtils
{
	private static final Log log = LogFactory.getLog(SshUtils.class);

	private SshUtils()
	{
	}

	/**
	 * 根据SSH上下文参数配置,执行一条或者多条命令
	 *
	 * @param sshContext
	 * @param commands
	 */
	public static void execute(SshContext sshContext, String... commands)
	{
		execute(sshContext, null, null, commands);
	}

	/**
	 * 根据SSH上下文参数配置,执行一条或者多条命令
	 *
	 * @param sshContext
	 * @param autoCommand
	 * @param commands
	 */
	public static void execute(SshContext sshContext, AutoCommand autoCommand, String... commands)
	{
		execute(sshContext, autoCommand, null, commands);
	}

	/**
	 * 根据SSH上下文参数配置,执行一条或者多条命令
	 *
	 * @param sshContext
	 * @param autoCommand
	 * @param echoWatcher
	 * @param commands
	 */
	public static void execute(SshContext sshContext, AutoCommand autoCommand, EchoWatcher echoWatcher, String... commands)
	{
		if (commands == null || commands.length < 1)
		{
			return;
		}
		JSch jsch = new JSch();
		Session session = null;
		ChannelShell channel = null;
		InputStream is = null;
		OutputStream os = null;
		try
		{
			if (sshContext.getIdentityList() != null && !sshContext.getIdentityList().isEmpty())
			{
				for (String identity : sshContext.getIdentityList())
				{
					jsch.addIdentity(identity);
				}
			}
			session = jsch.getSession(sshContext.getUsername(), sshContext.getHost(), sshContext.getPort());
			if (sshContext.getConfigMap() != null && !sshContext.getConfigMap().isEmpty())
			{
				for (Map.Entry<String, String> entry : sshContext.getConfigMap().entrySet())
				{
					session.setConfig(entry.getKey(), entry.getValue());
				}
			}
			if (!StringUtils.isEmpty(sshContext.getPassword()))
			{
				// 设置密码
				session.setPassword(sshContext.getPassword());
			}
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect(sshContext.getTimeout());
			channel = (ChannelShell) session.openChannel("shell");
			channel.connect(sshContext.getTimeout());
			channel.setAgentForwarding(true);
			channel.setPty(true);
			is = channel.getInputStream();
			os = channel.getOutputStream();
			String command = StringUtils.join(commands, "\n") + "\n";
			os.write(command.getBytes());
			os.flush();
			log.info("[" + sshContext.getHost() + "] execute: " + command);
			Thread.sleep(500L);
			StringBuffer buffer = new StringBuffer();
			while (true)
			{
				int length = is.available();
				if (length < 0)
				{
					break;
				}
				else if (length == 0)
				{
					String line = buffer.toString().trim();
					if (StringUtils.endsWith(line, "]#") || StringUtils.endsWith(line, "]$"))
					{
						// 命令行回显已经结束
						break;
					}
					if (autoCommand != null)
					{
						String command2 = autoCommand.match(line);
						if (!StringUtils.isEmpty(command2))
						{
							os.write((command2 + "\n").getBytes());
							os.flush();
						}
					}
					Thread.sleep(500L);
				}
				else
				{
					byte[] bytes = new byte[length];
					is.read(bytes, 0, length);
					char[] chars = new String(bytes, 0, length).toCharArray();
					for (char ch : chars)
					{
						if (ch == '\n')
						{
							if (echoWatcher != null)
							{
								echoWatcher.output(buffer.toString().trim());
							}
							else
							{
								log.info(buffer.toString().trim());
							}
							buffer = new StringBuffer();
						}
						else
						{
							buffer.append(ch);
						}
					}
				}
			}
			if (echoWatcher != null)
			{
				echoWatcher.output(buffer.toString().trim());
			}
			else
			{
				log.info(buffer.toString().trim());
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("ssh execute failed!", e);
		}
		finally
		{
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(is);
			if (channel != null)
			{
				channel.disconnect();
			}
			if (session != null)
			{
				session.disconnect();
			}
		}

	}
}

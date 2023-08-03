package com.ss.song.utils;

import ch.ethz.ssh2.*;
//import com.zhijian.ezbuild.install.constants.ExceptionInfo;
//import com.zhijian.ezbuild.install.exception.EZBuildException;
import com.ss.song.exception.EZBuildException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.*;

/**
 * 功能描述:SSH远程调用工具类
 */

@Slf4j
public class SSHClientUtils {

    private static String DEFAULT_CHART = "utf-8";
    private String username;
    private String password;
    private String privateKeyPath;
    private Connection connection;
    private Boolean usePassword;
    private static SSHClientUtils instance;

    static synchronized public SSHClientUtils getInstance(String ip, int port, String username, String passward, String privateKeyPath, Boolean usePassword) {
        if (instance == null) {
            instance = new SSHClientUtils(ip, port, username, passward, privateKeyPath, usePassword);
        }
        return instance;
    }

    public SSHClientUtils(String ip, int port, String username, String password, String privateKeyPath, Boolean usePassword) {
        this.username = username;
        this.password = password;
        this.privateKeyPath = privateKeyPath;
        this.usePassword = usePassword;
        this.connection = new Connection(ip, port);
    }

    /**
     * ssh用户登录验证，使用用户名和密码来认证
     */
    public boolean isAuthedWithPassword(String user, String password) throws IOException {
        return connection.authenticateWithPassword(user, password);
    }

    /**
     * ssh用户登录验证，使用用户名、私钥、密码来认证 其中密码如果没有可以为null，生成私钥的时候如果没有输入密码，则密码参数为null
     */
    public boolean isAuthedWithPublicKey(String user, File privateKey, String password) throws IOException {
        return connection.authenticateWithPublicKey(user, privateKey, password);
    }

    public boolean isAuth() throws IOException {
        if (usePassword) {
            return isAuthedWithPassword(username, password);
        } else {
            return isAuthedWithPublicKey(username, new File(privateKeyPath), password);
        }
    }


    /**
     * 执行远程命令
     *
     * @param command 需要执行的命令
     * @param timeout 超时时间（秒）
     * @return 执行结果
     * @throws Exception ？
     */
    public String execRemoteCommand(String command, long timeout, boolean isNeedSudo) throws Exception {
        boolean istimeout = false;
        Session session = null;
        InputStream stdout = null;
        BufferedReader br = null;
        String retryCommand = command;
        try {
            connection.connect(null, 5 * 1000, 0);
            boolean isAuthed = isAuth();
            if (isAuthed) {
                log.info("认证成功!");
                StringBuilder sb = new StringBuilder();
                session = connection.openSession();
                session.requestPTY("vt100", 80, 24, 640, 480, null);
                if (!username.equals("root")) {
                    if (isNeedSudo) {
                        command = "echo '" + password + "' | sudo -S " + command;
                    }
                }
                session.execCommand(command);
                stdout = new StreamGobbler(session.getStdout());
                br = new BufferedReader(new InputStreamReader(stdout, DEFAULT_CHART));
                long start = System.currentTimeMillis();
                char[] arr = new char[512];
                int read;
                while (true) {
                    read = br.read(arr, 0, arr.length);
                    if (read < 0 || (System.currentTimeMillis() - start) > timeout * 1000) {
                        if ((System.currentTimeMillis() - start) > timeout * 1000) {
                            istimeout = true;
                        }
                        break;
                    }
                    sb.append(new String(arr, 0, read));
                }
                if (session.getExitStatus() == null) {
                    stdout.close();
                    br.close();
                    if (connection != null) {
                        connection.close();
                    }
                    session.close();
                    return execRemoteCommand(retryCommand, timeout, isNeedSudo);
                } else if (session.getExitStatus() == 0) {
                    log.info("脚本执行成功:\n" + sb.toString());
                    return sb.toString();
                } else if (istimeout) {
                    log.info("脚本执行超时:\n" + sb.toString());
                    //throw new EZBuildException(ExceptionInfo.SCRIPT_EXE_TIMEOUT);
                } else {
                    log.error("脚本执行异常: \n" + sb.toString());
                    //throw new EZBuildException(ExceptionInfo.SCRIPT_EXE_ERROR.name(), ExceptionInfo.SCRIPT_EXE_ERROR.getRetCd(), " ( " + sb.toString() + " ) ");
                }
            } else {
                log.error("认证失败!");
                //throw new EZBuildException(ExceptionInfo.AUTH_SSH_ERROR);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //throw new EZBuildException(ExceptionInfo.CONNECT_SSH_ERROR, ex);
        } finally {
            if (stdout != null) {
                stdout.close();
            }
            if (br != null) {
                br.close();
            }
            if (connection != null) {
                connection.close();
            }
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    /**
     * 终端执行远程命令
     *
     * @param command 需要执行的命令
     * @param timeout 超时时间（秒）
     */
    public String execRemoteCommandOnTerminal(String command, String targetUsername, long timeout, boolean isNeedSudo) throws Exception {
        Session session = null;
        PrintWriter out = null;
        InputStream stdout = null;
        BufferedReader br = null;
        String retryCommand = command;
        try {
            connection.connect(null, 5 * 1000, 0);
            boolean isAuthed = isAuth();
            if (isAuthed) {
                log.info("认证成功!");
                StringBuilder sb = new StringBuilder();
                session = connection.openSession();
                if (!username.equals("root")) {
                    if (isNeedSudo) {
                        //是否需要切换用户
                        if (StringUtils.isBlank(targetUsername)) {
                            command = "echo '" + password + "' | sudo -S " + command;
                        } else {
                            /*String os = EZBuildPropertiesUtil.getProperty("os.name");
                            if (StringUtils.equals(os, "centos")) {
                                command = "echo '" + password + "' | sudo -S su - " + targetUsername + " --session-command '" + command + " '";
                                log.info("os:{} and command:{}", os, command);

                            } else {
                                command = "echo '" + password + "' | sudo -S su - " + targetUsername + " -c '" + command + " '";
                                log.info("os:{} and command:{}", os, command);
                            }*/
                        }
                    }
                }
                // 建立虚拟终端
                session.requestPTY("bash");
                // 打开一个Shell
                session.startShell();
                // 准备输入命令
                out = new PrintWriter(session.getStdin());
                stdout = new StreamGobbler(session.getStdout());
                //终端执行命令
                // 输入待执行命令
                out.println(command);
                out.println("exit");
                out.close();
                //等待，除非1.连接关闭；2.输出数据传送完毕；3.进程状态为退出；4.超时
                int result = session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, timeout * 1000);
                log.info("waitForCondition result:{}", result);

                //获取输出
                br = new BufferedReader(new InputStreamReader(stdout, DEFAULT_CHART));
                char[] arr = new char[512];
                int read;
                while (true) {
                    read = br.read(arr, 0, arr.length);
                    if (read < 0) {
                        break;
                    }
                    sb.append(new String(arr, 0, read));
                }
                if (session.getExitStatus() == null) {
                    stdout.close();
                    br.close();
                    if (connection != null) {
                        connection.close();
                    }
                    session.close();
                    return execRemoteCommandOnTerminal(retryCommand, targetUsername, timeout, isNeedSudo);
                } else if (session.getExitStatus() == 0) {
                    log.info("脚本执行成功:\n" + sb.toString());
                    return sb.toString();
                } else {
                    log.error("脚本执行异常: \n" + sb.toString());
                    // throw new EZBuildException(ExceptionInfo.SCRIPT_EXE_ERROR.name(), ExceptionInfo.SCRIPT_EXE_ERROR.getRetCd(), " ( " + sb.toString() + " ) ");
                }
            } else {
                log.error("认证失败!");
                // throw new EZBuildException(ExceptionInfo.AUTH_SSH_ERROR);
            }
        } catch (EZBuildException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            // throw new EZBuildException(ExceptionInfo.CONNECT_SSH_ERROR, ex);
        } finally {
            if (stdout != null) {
                stdout.close();
            }
            if (br != null) {
                br.close();
            }
            if (out != null) {
                out.close();
            }
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return null;
    }

    /**
     * 创建终端执行远程命令（默认30秒超时）
     *
     * @param command 需要执行的命令
     */
    public String execRemoteCommandOnTerminal(String command, boolean isNeedSudo) throws Exception {
        return execRemoteCommandOnTerminal(command, null, 30, isNeedSudo);
    }

    /**
     * 创建终端sudo切换用户执行远程命令（默认30秒超时）
     *
     * @param command 需要执行的命令
     */
    public String switchUserExecRemoteCommandOnTerminal(String command, String targetUsername) throws Exception {
        return execRemoteCommandOnTerminal(command, targetUsername, 30, true);
    }

    /**
     * 生成终端执行远程命令（默认30秒超时）
     *
     * @param command 需要执行的命令
     * @return 执行结果
     * @throws Exception ？
     */
    public String execRemoteCommand(String command, boolean isNeedSudo) throws Exception {
        return execRemoteCommand(command, 30, isNeedSudo);
    }


    /**
     * 批量执行远程命令
     *
     * @param command 需要执行的命令列表
     * @param timeout 超时时间（秒）
     */
    public String execRemoteCommand(String[] command, long timeout, boolean isNeedSudo) throws Exception {
        Session session = null;
        InputStream stdout = null;
        BufferedReader br = null;
        String[] retryCommand = command;
        long retryTimeout = timeout;
        boolean retryIsNeedSudo = isNeedSudo;
        try {
            connection.connect(null, 5 * 1000, 0);
            boolean istimeout = false;
            boolean isAuthed = isAuth();
            if (isAuthed) {
                log.info("认证成功!");
                StringBuilder sb = new StringBuilder();
                for (String s : command) {
                    session = connection.openSession();
                    session.requestPTY("vt100", 80, 24, 640, 480, null);
                    String cmd = s;
                    if (!username.equals("root")) {
                        if (isNeedSudo) {
                            cmd = "echo '" + password + "' | sudo -S " + command;
                        }
                    }
                    session.execCommand(cmd);
                    stdout = new StreamGobbler(session.getStdout());
                    br = new BufferedReader(new InputStreamReader(stdout, DEFAULT_CHART));
                    long start = System.currentTimeMillis();
                    char[] arr = new char[512];
                    int read;
                    while (true) {
                        read = br.read(arr, 0, arr.length);
                        if (read < 0 || (System.currentTimeMillis() - start) > timeout * 1000) {
                            if ((System.currentTimeMillis() - start) > timeout * 1000) {
                                istimeout = true;
                            }
                            break;
                        }
                    }
                }
                if (session.getExitStatus() == null) {
                    if (stdout != null) {
                        stdout.close();
                    }
                    if (br != null) {
                        br.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                    session.close();
                    return execRemoteCommand(retryCommand, retryTimeout, retryIsNeedSudo);
                } else if (session.getExitStatus() == 0) {
                    log.info("脚本执行成功: \n" + sb.toString());
                    return sb.toString();
                } else if (istimeout) {
                    log.info("脚本执行超时: \n" + sb.toString());
                    // throw new EZBuildException(ExceptionInfo.SCRIPT_EXE_TIMEOUT);
                } else {
                    log.error("脚本执行异常: \n" + sb.toString());
                    // throw new EZBuildException(ExceptionInfo.SCRIPT_EXE_ERROR.name(), ExceptionInfo.SCRIPT_EXE_ERROR.getRetCd(), " ( " + sb.toString() + " ) ");
                    throw new RuntimeException();
                }
            } else {
                log.error("认证失败!");
                // throw new EZBuildException(ExceptionInfo.AUTH_SSH_ERROR);
            }
        } catch (EZBuildException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            // throw new EZBuildException(ExceptionInfo.CONNECT_SSH_ERROR, ex);
            throw new RuntimeException();
        } finally {
            if (stdout != null) {
                stdout.close();
            }
            if (br != null) {
                br.close();
            }
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return null;
    }

    /**
     * 批量执行远程命令(默认30秒超时)
     *
     * @param command 需要执行的命令列表
     */
    public String execRemoteCommand(String[] command, boolean isNeedSudo) throws Exception {
        return execRemoteCommand(command, 30, isNeedSudo);
    }


    public void setIp(String ip) {
    }

    public void setPort(int port) {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public void setUsePassword(Boolean usePassword) {
        this.usePassword = usePassword;
    }

    public static void setDefaultChart(String defaultChart) {
        SSHClientUtils.DEFAULT_CHART = defaultChart;
    }

    public static void main(String[] args) {
        String ip = "192.168.2.68";
        String username = "root";
        String password = "Root@123";
        try {
            SSHClientUtils sshClientUtils = SSHClientUtils.getInstance(ip, 22, username, password, "", true);
            //System.out.println(sshClientUtils.execRemoteCommand("egrep \"^kkk\" /etc/passwd  >& /dev/null  ",true));
            // System.out.println(sshClientUtils.execRemoteCommand("egrep \"^ss:\" /etc/group >& /dev/null  ", true));
            System.out.println(sshClientUtils.execRemoteCommand("mkdir /usr/ss/trino/coordinate/etc/catalog -p", true));
            System.out.println(sshClientUtils.execRemoteCommand("mkdir /usr/ss/trino/worker/etc/catalog -p", true));

        } catch (EZBuildException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

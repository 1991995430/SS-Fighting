package com.ss.song.utils;

//import com.zhijian.ezbuild.install.constants.ExceptionInfo;
//import com.zhijian.ezbuild.install.exception.EZBuildException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ShellUtils {
    public static String lineSeparator = System.getProperty("line.separator");
    public static String COMMAND_SH = "sh";
    public static String COMMAND_EXIT = "exit\n";
    public static String COMMAND_LINE_END = "\n";

    static {
        if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
            log.info("window");
            COMMAND_SH = "cmd";
        } else {
            log.info("unix");
        }
    }

    //Sudo执行
    public static String execCommand(String command, String targetUsername, String password) {
        if (StringUtils.isNotBlank(targetUsername)) {
            //当目标用户为当前登陆用户时
            if (!targetUsername.equals("root")) {
                if (targetUsername.equals(System.getProperty("user.name"))) {
                    command = "echo '" + password + "' | sudo -S " + command;
                } else {
                    // 当目标用户不为当前登陆用户时并且不为root用户的时候
                    /*String os = EZBuildPropertiesUtil.getProperty("os.name");
                    if (StringUtils.equals(os, "centos")) {
                        command = "echo '" + password + "' | sudo -S su - " + targetUsername + " --session-command '" + command + " '";
                        log.info("os:{} and command:{}", os, command);
                    } else {
                        command = "echo '" + password + "' | sudo -S su - " + targetUsername + " -c '" + command + " '";
                        log.info("os:{} and command:{}", "os", command);
                    }*/
                }
            }
        }
        return execCommand(new String[]{command}, true);
    }

    public static String execCommand(String command, boolean isNeedResultMsg) {
        return execCommand(new String[]{command}, isNeedResultMsg);
    }

    /**
     * execute shell commands
     *
     * @param commands     command array
     * @param needResponse whether need result msg
     */
    public static String execCommand(String[] commands, final boolean needResponse) {
        int result = -1;
        Process process = null;
        final StringBuilder successMsg = new StringBuilder();
        final StringBuilder errorMsg = new StringBuilder();

        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }
                log.info("Command: [ " + command + " ]");
                // do not use os.writeBytes(command), avoid chinese charset error
                os.write(command.getBytes(StandardCharsets.UTF_8));
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            final BufferedReader successResult = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            final BufferedReader errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));

            new Thread(() -> {
                try {
                    if (needResponse) {
                        String s;
                        while ((s = successResult.readLine()) != null) {
                            successMsg.append(s);
                            successMsg.append(lineSeparator);
                        }
                    }
                } catch (IOException e) {
                    log.error("EZBuildException Info:", e);
                }
            }).start();
            //启动两个线程,解决process.waitFor()阻塞问题
            new Thread(() -> {

                try {
                    if (needResponse) {
                        String s;
                        while ((s = errorResult.readLine()) != null) {
                            errorMsg.append(s);
                            errorMsg.append(lineSeparator);
                        }
                    }
                } catch (IOException e) {
                    log.error("EZBuildException Info:", e);
                }
            }).start();
            result = process.waitFor();
            errorResult.close();
            successResult.close();

        } catch (Exception e) {
            log.error("EZBuildException Info:", e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                log.error("EZBuildException Info:", e);
            } finally {
                if (process != null) {
                    process.destroy();
                }
            }

        }
        if (result == 0) {
            log.info("脚本执行成功:\n" + successMsg.toString());
            return successMsg.toString();
        } else {
            log.error("脚本执行异常: \n" + errorMsg.toString());
            //throw new EZBuildException(ExceptionInfo.SCRIPT_EXE_ERROR.name(), ExceptionInfo.SCRIPT_EXE_ERROR.getRetCd(), " ( " + errorMsg.toString() + " ) ");
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) {
        System.out.println(ShellUtils.execCommand(args, true));

        System.out.println(ShellUtils.execCommand("echo hadoop | ssh -t root@192.168.217.10 \" ls /root >> /tmp/11 2>&1 & \"", true));
    }
}

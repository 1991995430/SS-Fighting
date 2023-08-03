package com.ss.song.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
// import com.zhijian.ezbuild.install.constants.ExceptionInfo;
// import com.zhijian.ezbuild.install.exception.EZBuildException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 *
 *目前连接限制为1  后面假如需要则修改为线程池
 */
@Slf4j
public class SCPClientUtils {

    private  String username;
    private  String password;
    private  String privateKeyPath;
    private  Boolean usePassword=false;
    private  Connection connection;
    private static SCPClientUtils instance;

    static synchronized public SCPClientUtils getInstance(String ip, int port, String username, String passward, String privateKeyPath, Boolean usePassword) {
        if (instance == null) {
            instance = new SCPClientUtils(ip, port, username, passward,privateKeyPath,usePassword);
        }
        return instance;
    }

    static synchronized public SCPClientUtils getInstance(String ip, int port, String username, String passward, Boolean usePassword) {
        if (instance == null) {
            instance = new SCPClientUtils(ip, port, username, passward,usePassword);
        }
        return instance;
    }

    public SCPClientUtils(String ip, int port, String username, String passward, String privateKeyPath, Boolean usePassword) {
        this.username = username;
        this.password = passward;
        this.privateKeyPath=privateKeyPath;
        this.usePassword=usePassword;
        this.connection=new Connection(ip, port);
    }

    public SCPClientUtils(String ip, int port, String username, String passward, Boolean usePassword) {
        this.username = username;
        this.password = passward;
        this.usePassword=usePassword;
        this.connection=new Connection(ip, port);
    }


    /**
     * ssh用户登录验证，使用用户名和密码来认证
     *
     */
    public  boolean isAuthedWithPassword(String user, String password) throws IOException {
        return connection.authenticateWithPassword(user, password);
    }

    /**
     * ssh用户登录验证，使用用户名、私钥、密码来认证 其中密码如果没有可以为null，生成私钥的时候如果没有输入密码，则密码参数为null
     *
     */
    public  boolean isAuthedWithPublicKey(String user, File privateKey, String password) throws IOException {
        return connection.authenticateWithPublicKey(user, privateKey, password);
    }

    public  boolean isAuth() throws IOException {
        if (usePassword) {
            return isAuthedWithPassword(username, password);
        } else {
            return isAuthedWithPublicKey(username, new File(privateKeyPath), password);
        }
    }

    public  void getFile(String remoteFile, String path) /*throws EZBuildException*/ {
        try {
            connection.connect();
            boolean isAuthed = isAuth();
            if (isAuthed) {
                log.info("认证成功!");
                SCPClient scpClient = connection.createSCPClient();
                scpClient.get(remoteFile, path);
            } else {
                log.error("认证失败!");
                // throw  new EZBuildException(ExceptionInfo.AUTH_SCP_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // throw  new EZBuildException(ExceptionInfo.CONNECT_SCP_ERROR,e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public  void putFile(String localFile, String remoteTargetDirectory) /*throws EZBuildException*/ {
        try {
            connection.connect();
            boolean isAuthed = isAuth();
            if (isAuthed) {
                SCPClient scpClient = connection.createSCPClient();
                scpClient.put(localFile, remoteTargetDirectory);
            } else {
                log.error("认证失败!");
                // throw  new EZBuildException(ExceptionInfo.AUTH_SCP_ERROR);
            }

        } catch (IOException e){
            e.printStackTrace();
            // throw  new EZBuildException(ExceptionInfo.CONNECT_SCP_ERROR.getRetCd(),e.getCause().getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            // throw  new EZBuildException(ExceptionInfo.CONNECT_SCP_ERROR,e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
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

    public static void main(String[] args) {
        try {
            // getFile("/home/users/ubuntu/error.txt", "c://");
            SCPClientUtils scpclientUtils= SCPClientUtils.getInstance("192.168.2.68",22,"root","Root@123",true);
            scpclientUtils.putFile("D:\\testssh\\etc\\config.properties", "/usr/ss");
        } catch (Exception e) {
            System.out.println("********");
            e.printStackTrace();
        }
    }

}

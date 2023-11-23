package com.ss.song.datasource;

import com.ss.song.utils.AESUtils;
import lombok.Data;

import java.io.Serializable;

@Data
public class DataSourceParam implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 数据源类型，具体参考每个数据源类的type字段
     */
    private String type;

    /**
     * 数据源连接用的url
     */
    private String url;

    /**
     * 数据源连接用的用户名
     */
    private String userName;

    /**
     * 数据源连接用的密码
     */
    private String password;

    /**
     * 从url解析出的dbName
     */
    private String dbName;

    /**
     * 从url解析出的ip地址
     */
    private String ip;

    /**
     * 从url解析出的端口号
     */
    private String port;

    /**
     * hive的代理mysql的连接信息
     */
    private DataSourceParam proxy;

    /**
     * gbase8s的实例名
     */
    private String instanceName;

    /**
     * db2的schemaName
     */
    private String schemaName;

    /**
     * access的路径
     */
    private String accessPath;

    /**
     * 解密符号(未加密的为true,加密的为false)，不填也为false
     */
    private Boolean encryptFlag;

    public void encrypt() {
        if (this.password != null) {
            this.password = AESUtils.encrypt(this.password);
            this.encryptFlag = false;
            if (proxy != null && proxy.password != null) {
                proxy.password = AESUtils.encrypt(proxy.password);
                proxy.encryptFlag = false;
            }
        }

    }

    public void decrypt() {
        if (this.password != null) {
            if (encryptFlag == null || !encryptFlag) {
                this.password = AESUtils.decrypt(this.password);
                encryptFlag = true;
            }
            if (proxy != null && proxy.password != null && (proxy.encryptFlag == null || !proxy.encryptFlag)) {
                proxy.password = AESUtils.decrypt(proxy.password);
                proxy.encryptFlag = true;
            }
        }

    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public DataSourceParam getProxy() {
        return proxy;
    }

    public void setProxy(DataSourceParam proxy) {
        this.proxy = proxy;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getAccessPath() {
        return accessPath;
    }

    public void setAccessPath(String accessPath) {
        this.accessPath = accessPath;
    }

    public Boolean getEncryptFlag() {
        return encryptFlag;
    }

    public void setEncryptFlag(Boolean encryptFlag) {
        this.encryptFlag = encryptFlag;
    }
}

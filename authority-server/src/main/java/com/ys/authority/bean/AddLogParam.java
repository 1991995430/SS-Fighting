package com.ys.authority.bean;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 日志对象
 *
 * @author suwenbao
 * @since 2022/8/5
 */

@Data
public class AddLogParam {
    /**
     * 服务id
     */
    private Integer serviceId;


    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 操作的ip地址
     */
    private String remoteAddr;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求的URI
     */
    private String requestUri;

    /**
     * 用户操作（调用的函数）
     */
    private String operations;

    /**
     * 传入的参数
     */
    //TODO 修改参数类型为StringBuffer
    private String params;

    /**
     * 开始调用时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束调用时间
     */
    private LocalDateTime endTime;

    /**
     * 调用持续时间
     */
    private Integer callTime;

    /**
     * 返回值
     */
    private String returnCode;

    /**
     * 异常信息
     */
    private String errorMessage;
}

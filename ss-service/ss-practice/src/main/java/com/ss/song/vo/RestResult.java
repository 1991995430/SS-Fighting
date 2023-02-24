package com.ss.song.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zqxw
 */
@Data
public class RestResult<T> implements Serializable {
    public static final int CODE_SUCCESS = 200;
    public static final String SUCCESS = "success";

    public static final int CODE_PARAM_ERR = 400;
    public static final String PARAM_ERR = "参数错误";

    public static final int CODE_AUTH_ERR = 401;
    public static final String AUTH_ERR = "认证检查失败";

    public static final int CODE_ITEM_EXISTED = 403;
    public static final String ITEM_ALREADY_EXISTED = "信息已存在";
    public static final String NAME_ALREADY_EXISTED = "名称已被使用";

    public static final int CODE_CAN_NOT_FIND = 416;
    public static final String CAN_NOT_FIND_ITEM = "元素未找到";

    public static final int CODE_ITEM_USED = 413;
    public static final String ITEM_ALREADY_USED = "元素已被使用";

    public static final String INNER_ERROR = "系统内部错误";
    public static final int CODE_INNER_ERROR = 500;

    public static final String STATUS_ERROR = "状态错误";
    public static final int CODE_STATUS_ERROR = 502;

    public static final String FILE_FORMAT_ERROR = "文件格式化错误";
    public static final int CODE_FILE_FORMAT_ERROR = 503;

    private int status = CODE_SUCCESS;
    private String message = SUCCESS;
    private T data;
    private long timestamp;
    private Integer totalNumber;

    public RestResult() {
        timestamp = System.currentTimeMillis();
    }

    public static <T> RestResult<T> success(T data) {
        RestResult<T> r = new RestResult<>();
        r.setData(data);
        return r;
    }

    public static <T> RestResult<T> ret(int code, String msg) {
        RestResult<T> r = new RestResult<>();
        r.setStatus(code);
        r.setMessage(msg);
        return r;
    }

    public static <T> RestResult<T> success(T data, int totalNumber) {
        RestResult<T> r = new RestResult<>();
        r.setData(data);
        r.setTotalNumber(totalNumber);
        return r;
    }
}

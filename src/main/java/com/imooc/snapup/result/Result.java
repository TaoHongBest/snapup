package com.imooc.snapup.result;

/**
 * @author taohong on 09/10/2018
 */
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    /**
     * Get called when success
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    /**
     * Get called when error
     */
    public static <T> Result<T> error(CodeMsg cm) {
        return new Result<T>(cm);
    }

    // Constructor for Result when success
    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    // Constructor for Result when error
    public Result(CodeMsg cm) {
        if (cm == null) {
            return;
        }
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}

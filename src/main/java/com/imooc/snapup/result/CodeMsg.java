package com.imooc.snapup.result;

/**
 * @author taohong on 09/10/2018
 */
public class CodeMsg {
    private int code;
    private String msg;

    // General errors
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "server error");
    // Login errors 5002XX

    // Item errors 5003XX

    // Order errors 5004XX

    // Snapup errors 5005XX

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

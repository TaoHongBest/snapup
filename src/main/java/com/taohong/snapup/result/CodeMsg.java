package com.taohong.snapup.result;

/**
 * @author taohong on 09/10/2018
 */
public class CodeMsg {
    private int code;
    private String msg;

    // General errors
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "server error");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "parameter validation error: %s");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "illegal request");
    // Login errors 5002XX
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session does not exit or no longer valid");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "password cannot be empty");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "phone number cannot be empty");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "invalid phone number");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "phone number does not exist");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "wrong password");

    // Item errors 5003XX

    // Order errors 5004XX
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500400, "order does not exist");

    // Snapup errors 5005XX
    public static CodeMsg SNAPUP_OVER = new CodeMsg(500500, "snap-up is over");
    public static CodeMsg REPEAT_SNAPUP = new CodeMsg(500501, "cannot repeat snap up one item");
    public static CodeMsg SNAPUP_FAIL = new CodeMsg(500502, "Snap-up failed");


    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

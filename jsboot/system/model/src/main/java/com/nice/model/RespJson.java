package com.nice.model;

public class RespJson {
    private Integer status;
    private String msg;
    private Object obj;

    //java里面可以new自己吗？？？？？
    public static RespJson build() {
        return new RespJson();
    }

    public static RespJson ok(String msg) {
        return new RespJson(200, msg, null);
    }

    public static RespJson ok(String msg, Object obj) {
        return new RespJson(200, msg, obj);
    }

    public static RespJson error(String msg) {
        return new RespJson(500, msg, null);
    }

    public static RespJson error(String msg, Object obj) {
        return new RespJson(500, msg, obj);
    }

    private RespJson() {
    }

    private RespJson(Integer status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    public Integer getStatus() {
        return status;
    }

    public RespJson setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RespJson setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getObj() {
        return obj;
    }

    public RespJson setObj(Object obj) {
        this.obj = obj;
        return this;
    }
}

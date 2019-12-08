package com.opensouce.component.netty.message;

/**
 * @author ZhangLong on 2019/12/7  8:50 下午
 * @version V1.0
 */
public class ServerResponseClient<RespT> extends InOutResultData {

    private ServerResponseClient success(RespT responseData){
        this.code = SUCCESS;
        this.msg = SUCCESS;
        this. data = responseData;
        return this;
    }

    private ServerResponseClient fail(){
        this.code = FAIL;
        this.msg = FAIL;
        return this;
    }

    private String code;

    private String msg;

    private RespT data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RespT getData() {
        return data;
    }

    public void setData(RespT data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ServerResponseClient{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

package com.opensouce.component.netty.message;

/**
 * @author ZhangLong on 2019/12/7  8:50 下午
 * @version V1.0
 */
public class ClientRequestServer<ReqT> extends InOutResultData{

    private ClientRequestServer success(ReqT requestData){
        this.code = SUCCESS;
        this.msg = SUCCESS;
        this. data = requestData;
        return this;
    }

    private ClientRequestServer fail(){
        this.code = FAIL;
        this.msg = FAIL;
        return this;
    }

    private String code;

    private String msg;

    private ReqT data;

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

    public ReqT getData() {
        return data;
    }

    public void setData(ReqT data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ClientRequestServer{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

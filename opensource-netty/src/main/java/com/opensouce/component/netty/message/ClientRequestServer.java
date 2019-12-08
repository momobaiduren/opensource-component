package com.opensouce.component.netty.message;

/**
 * @author ZhangLong on 2019/12/7  8:50 下午
 * @version V1.0
 */
public class ClientRequestServer<ReqT> extends InOutResultData{

    private ReqT data;

    public ReqT getData() {
        return data;
    }

    public void setData(ReqT data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ClientRequestServer{" +
                "data=" + data +
                '}';
    }
}

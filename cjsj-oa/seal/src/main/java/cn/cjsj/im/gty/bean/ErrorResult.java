package cn.cjsj.im.gty.bean;

/**
 * Created by LuoYang on 2017/3/2.
 */

public class ErrorResult {
    private String status;

    //用来模仿Data
    private String data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ErrorResult{" +
                "status='" + status + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}

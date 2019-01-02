package cn.cjsj.im.gty.bean;

/**
 * Created by LuoYang on 17/2/5.
 */
public class HttpResult<T> {


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    //用来模仿Data
    private T data;




    public T getSubjects() {
        return data;
    }

    public void setSubjects(T data) {
        this.data = data;
    }


    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("status=" + status );
        if (null != data) {
            sb.append(" subjects:" + data.toString());
        }
        return sb.toString();
    }
}

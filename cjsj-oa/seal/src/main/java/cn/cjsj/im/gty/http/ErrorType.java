package cn.cjsj.im.gty.http;

import java.io.Serializable;

/**错误类型
 * Created by xul on 2016/12/28.
 */
public enum ErrorType implements Serializable {
    SYSTEM("系统错误"),
    USER("用户错误");

    private String name;
    ErrorType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

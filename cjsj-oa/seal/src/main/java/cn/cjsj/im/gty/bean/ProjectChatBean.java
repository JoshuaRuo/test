package cn.cjsj.im.gty.bean;

/**
 * Created by LuoYang on 2018/8/8 16:28
 * 项目讨论Bean
 */
public class ProjectChatBean {
    public static final int SEND_ITEM = 0;//发送
    public static final int RECEIVE_ITEM = 1;//收到

    public int type;
    public String message;
    public String name;
    public String time;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

package cn.cjsj.im.gty.bean;

import java.util.List;

/**
 * Created by LuoYang on 2018/11/27 16:20
 * 发送人Bean
 */
public class SelectPeopleBean {

    private String nodeName;
    private String userType;
    private List<SysUserBean> sysUserList;
    private String nodeId;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<SysUserBean> getSysUserList() {
        return sysUserList;
    }

    public void setSysUserList(List<SysUserBean> sysUserList) {
        this.sysUserList = sysUserList;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
}

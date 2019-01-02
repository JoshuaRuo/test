package cn.cjsj.im.gty.bean;

import java.util.List;

/**
 * Created by LuoYang on 2018/12/7 14:50
 * 项目详情
 */
public class ProjectDetailResponse {

    private List<ProjectDetailMemberResponse> memberList;
    private ProjectListBeanResponse project;
    private List<ProjectDetailMyMajorResponse> myTaskList;

    public List<ProjectDetailMemberResponse> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<ProjectDetailMemberResponse> memberList) {
        this.memberList = memberList;
    }

    public ProjectListBeanResponse getProject() {
        return project;
    }

    public void setProject(ProjectListBeanResponse project) {
        this.project = project;
    }

    public List<ProjectDetailMyMajorResponse> getMyTaskList() {
        return myTaskList;
    }

    public void setMyTaskList(List<ProjectDetailMyMajorResponse> myTaskList) {
        this.myTaskList = myTaskList;
    }

    @Override
    public String toString() {
        return "ProjectDetailResponse{" +
                "memberList=" + memberList +
                ", project=" + project +
                ", myTaskList=" + myTaskList +
                '}';
    }
}

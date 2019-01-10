package cn.cjsj.im.gty.http;


import com.alibaba.fastjson.JSONObject;

import java.util.List;

import cn.cjsj.im.gty.bean.AgendaResponse;
import cn.cjsj.im.gty.bean.ApprovalPlanModel;
import cn.cjsj.im.gty.bean.ApprovalPlanResponse;
import cn.cjsj.im.gty.bean.CheckResponse;
import cn.cjsj.im.gty.bean.CheckStatisticsResponse;
import cn.cjsj.im.gty.bean.CustomAllResponse;
import cn.cjsj.im.gty.bean.DailyPaperModel;
import cn.cjsj.im.gty.bean.DepartmentDispatchDetailBean;
import cn.cjsj.im.gty.bean.DepartmentDispatchListBean;
import cn.cjsj.im.gty.bean.DepartmentStaffBean;
import cn.cjsj.im.gty.bean.EarlyArrvalRankListResponse;
import cn.cjsj.im.gty.bean.GroupDispatchDetailBean;
import cn.cjsj.im.gty.bean.GroupDispatchResponseBean;
import cn.cjsj.im.gty.bean.HttpResult;
import cn.cjsj.im.gty.bean.IntegralBean;
import cn.cjsj.im.gty.bean.NewsListResponse;
import cn.cjsj.im.gty.bean.NewsResponse;
import cn.cjsj.im.gty.bean.NewsStatisticsResponse;
import cn.cjsj.im.gty.bean.NoticeAndIntegralBean;
import cn.cjsj.im.gty.bean.NoticeDispatchSupport;
import cn.cjsj.im.gty.bean.OAUserBean;
import cn.cjsj.im.gty.bean.OrganizationBean;
import cn.cjsj.im.gty.bean.PerformanceBean;
import cn.cjsj.im.gty.bean.ProjectDetailMemberResponse;
import cn.cjsj.im.gty.bean.ProjectDetailResponse;
import cn.cjsj.im.gty.bean.ProjectListBeanResponse;
import cn.cjsj.im.gty.bean.ReWorkDetailResponse;
import cn.cjsj.im.gty.bean.ReWorkHistoryResponse;
import cn.cjsj.im.gty.bean.SelectPeopleBean;
import cn.cjsj.im.gty.bean.SelectPeopleCcResponse;
import cn.cjsj.im.gty.bean.SysUserBean;
import cn.cjsj.im.gty.bean.VersionBean;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by LuoYang on 17/2/23.
 * RetroFit业务类
 */
public interface ConnectionService {

    //登录
    @POST("mobile/login/token")
    Observable<HttpResult<JSONObject>> postToken(@Query("phone") String phone, @Query("token") String token, @Query("registrationId") String registrationId, @Query("cookie") String cookie);

    //账号密码登录
    @POST("mobile/login/")
    Observable<HttpResult<JSONObject>> login(@Query("phone") String phone, @Query("password") String password, @Query("registrationId") String registrationId);

    //待办日志
    @GET("bpm/myTask/{type}/{pageSize}/{currentPage}")
    Observable<HttpResult<AgendaResponse>> getBackLog(@Header("token") String token, @Path("type") int type, @Path("pageSize") int pageSize, @Path("currentPage") int currentPage);

    //我的请求
    @GET("bpm/myRequest/{type}/{pageSize}/{currentPage}")
    Observable<HttpResult<AgendaResponse>> getMyRequest(@Header("token") String token, @Path("type") int type, @Path("pageSize") int pageSize, @Path("currentPage") int currentPage);


    //消息列表
    @GET("bpm/news/{pageSize}/{currentPage}")
    Observable<HttpResult<List<NewsResponse>>> getNewS(@Header("token") String token, @Path("pageSize") int pageSize, @Path("currentPage") int currentPage);

    //上传头像
    @POST("mine/headPortrait")
    Observable<HttpResult<String>> uploadHeadFile(@Body RequestBody requestBody, @Header("token") String token);

    //获取公告列表
    @GET("notice/listType/{type}/{pageSize}/{currentPage}")
    Observable<HttpResult<NoticeAndIntegralBean>> getNotice(@Header("token") String token, @Path("type") int type, @Path("pageSize") int pageSize, @Path("currentPage") int currentPage);

    //获取模板信息
    @GET("custom/all")
    Observable<HttpResult<List<CustomAllResponse>>> getCustomAll(@Header("token") String token);

    //登出
    @POST("mobile/login/out")
    Observable<HttpResult<Object>> logOut(@Header("token") String token);

    //用户来源验证
    @GET("mobile/login/check/{phone}")
    Observable<HttpResult<String>> verifyAccount(@Path("phone") String phone);

    //修改OA密码
    @PUT("mobile/login/password")
    Observable<HttpResult<String>> updatePassword(@Query("phone") String phone, @Query("password") String password);

    //version
    @GET("version/")
    Observable<HttpResult<VersionBean>> getVersion();

    //获取用户信息
    @GET("mine/personalInformation")
    Observable<HttpResult<OAUserBean>> getUserInfo(@Header("token") String token);

    //获取积分
    @GET("integral/list/{pageSize}/{currentPage}/{type}")
    Observable<HttpResult<IntegralBean>> getMineIntegral(@Header("token") String token, @Path("pageSize") int pageSize, @Path("currentPage") int currentPage, @Path("type") int type);

    //意见反馈
    @POST("feeback/save")
    Observable<HttpResult<String>> sendFeedback(@Header("token") String token, @Query("content") String content);

    //获取绩效
    @GET("probonus/listSelf/{year}/{pageSize}/{currentPage}")
    Observable<HttpResult<PerformanceBean>> getPerformance(@Header("token") String token, @Path("year") String year, @Path("pageSize") int pageSize, @Path("currentPage") int currentPage);

    //获取是否加入考勤组
    @GET("check/isCheck")
    Observable<HttpResult<Integer>> getIsJoinWorkGroup(@Header("token") String token);

    //查询考勤
    @GET("check/{lot}/{lat}/")
    Observable<HttpResult<CheckResponse>> getCheck(@Header("token") String token, @Path("lot") double lot, @Path("lat") double lat);

    @GET("dailyPaper/list/{type}/{pageSize}/{currentPage}")
    Observable<HttpResult<DailyPaperModel>> getDailyPaper(@Header("token") String token, @Path("type") byte type, @Path("pageSize") int pageSize, @Path("currentPage") int currentPage);

    //检查考勤
    @GET("check/today")
    Observable<HttpResult<JSONObject>> getCheckToday(@Header("token") String token);

    //获取集团发文
    @GET("groupNotice/{pageSize}/{currentPage}")
    Observable<HttpResult<GroupDispatchResponseBean>> getGroupDisPatch(@Header("token") String token, @Path("pageSize") int pageSize, @Path("currentPage") int currentPage);

    //获取部门发文
    @GET("deptNotice/{pageSize}/{currentPage}")
    Observable<HttpResult<DepartmentDispatchListBean>> getDepartmentDispatch(@Header("token") String token, @Path("pageSize") int pageSize, @Path("currentPage") int currentPage);

    //公告详情
    @GET("notice/detail/{id}")
    Observable<HttpResult<NoticeDispatchSupport>> getNoticeDetail(@Header("token") String token, @Path("id") long id);

    //部门发文详情
    @GET("deptNotice/detail/{id}")
    Observable<HttpResult<DepartmentDispatchDetailBean>> getDepartmentDispatchDetail(@Header("token") String token, @Path("id") long id);

    //集团发文详情
    @GET("groupNotice/details/{id}")
    Observable<HttpResult<GroupDispatchDetailBean>> getGroupDispatchDetail(@Header("token") String token, @Path("id") long id);

    //项目列表
    @POST("project/list")
    Observable<HttpResult<List<ProjectListBeanResponse>>> getProjectList(@Header("token") String token, @Body RequestBody requestBody);

    //关注取消项目
    @GET("project/attention/{xmid}/{action}")
    Observable<HttpResult<String>> putAttention(@Header("token") String token, @Path("xmid") long xmid, @Path("action") String action);

    //获取部门组织
    @GET("contacts/getOrgList")
    Observable<HttpResult<List<OrganizationBean>>> getOrgList(@Header("token") String token);

    //获取部门人员
    @GET("contacts/getSameDepPeople/{orgId}")
    Observable<HttpResult<List<DepartmentStaffBean>>> getDepartmentStaff(@Header("token") String token, @Path("orgId") long orgId);

    //获取员工详情
    @GET("contacts/getUserDetail/{userId}")
    Observable<HttpResult<SysUserBean>> getStaffDetail(@Header("token") String token, @Path("userId") long userId);

    //搜索员工
    @GET("contacts/search/{searchParameter}")
    Observable<HttpResult<List<DepartmentStaffBean>>> searchStaff(@Header("token") String token, @Path("searchParameter") String searchParameter);

    //提交考勤
    @POST("check/")
    Observable<HttpResult<String>> postCheckData(@Header("token") String token, @Query("address") String address, @Query("lot") double lot, @Query("lat") double lat, @Query("type") String type, @Query("operation") int operation, @Query("notes") String notes);

    //查询考勤统计
    @GET("check/statistics/")
    Observable<HttpResult<CheckStatisticsResponse>> getCheckStatistics(@Header("token") String token, @Query("yearMonth") String yearMonth);

    //获取早到榜
    @GET("check/statistics/earlyRank/{yearMonthDay}/{pageSize}/{currentPage}")
    Observable<HttpResult<EarlyArrvalRankListResponse>> getEarlyArrvalList(@Header("token") String token, @Path("yearMonthDay") String yearMonthDay, @Path("pageSize") int pageSize, @Path("currentPage") int currentPage);

    //获取发送人列表
    @POST("sysUserSelector/startNode")
    Observable<HttpResult<SelectPeopleBean>> getFlowPeopleList(@Header("token") String token, @Query("actDefId") String actDefId, @Query("name") String name);

    //获取抄送人员列表
    @POST("sysUserSelector/allUser")
    Observable<HttpResult<SelectPeopleCcResponse>> getCCPeopleList(@Header("token") String token, @Query("name") String name, @Query("pageSize") int pageSize, @Query("currentPage") int currentPage);

    //查询补卡历史
    @GET("check/remakeCard/list/{type}/{pageSize}/{currentPage}")
    Observable<HttpResult<List<ReWorkHistoryResponse>>> getReWorkHisList(@Header("token") String token, @Path("type") int type, @Path("pageSize") int pageSize, @Path("currentPage") int currentPage);

    //补卡详情
    @GET("check/remakeCard/detail/{id}")
    Observable<HttpResult<ReWorkDetailResponse>> getReWorkDetail(@Header("token") String token, @Path("id") long id);

    //审批进度
    @GET("bpm/flowChart/{businessKey}")
    Observable<HttpResult<ApprovalPlanResponse>> getApprovalPlan(@Header("token") String token, @Path("businessKey") long businessKey);

    //审批历史
    @GET("bpm/approvalHistory/{businessKey}")
    Observable<HttpResult<ApprovalPlanResponse>> getApprovalHistory(@Header("token") String token, @Path("businessKey") long businessKey);

    //提交补卡
//    @POST("check/remakeCard/run")
//    Observable<HttpResult<JSONObject>> postReAttendance(@Header("token")String token,@Query("type")int type,@Query("cardDate")String cardDate,@Query("reason")String reason,@Query("lastDestTaskIdsRun")String lastDestTaskIdsRun
//    ,@Query("lastDestTaskUidsRun")String lastDestTaskUidsRun,@Query("copyPersonID")String copyPersonID,@Query("copyPerson")String copyPerson);
    @POST("check/remakeCard/run")
    Observable<HttpResult<String>> postReAttendance(@Header("token") String token, @Body RequestBody requestBody);

    //项目详情
    @GET("project/detail/{id}")
    Observable<HttpResult<ProjectDetailResponse>> getProjectDetail(@Header("token") String token, @Path("id") long id);

    //关闭项目
    @POST("project/close")
    Observable<HttpResult<String>> closeProject(@Header("token") String token, @Query("xmid") long id);

    //修改进度
    @POST("project/updateXmjd")
    Observable<HttpResult<String>> updateProgress(@Header("token")String token,@Query("xmid")long id,@Query("xmjd")double progressPercent);

    //项目成员
    @GET("project/memberList/{id}")
    Observable<HttpResult<ProjectDetailResponse>> getMemberList(@Header("token")String token, @Path("id")long id);

    //日历日报
    @GET("dailyPaper/checkDailyPaper/{date}")
    Observable<HttpResult<List<String>>> getDailyPaperDefault(@Header("token")String token,@Path("date") String date);

    //新通知未读条数
    @GET("message/unreadCount")
    Observable<HttpResult<Integer>> getNews203Count(@Header("token")String token);

    //获取消息通知外部信息
    @GET("message/statistics")
    Observable<HttpResult<List<NewsStatisticsResponse>>> getNewsOutValue(@Header("token")String token);

    //获取通知列表
    @GET("message/detail/{type}/{pageIndex}/{pageSize}")
    Observable<HttpResult<NewsListResponse>> getNews203List(@Header("token")String token, @Path("type") int type, @Path("pageIndex")int pageIndex, @Path("pageSize")int pageSize);

}

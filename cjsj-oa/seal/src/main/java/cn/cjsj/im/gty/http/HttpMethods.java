package cn.cjsj.im.gty.http;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.cjsj.im.App;
import cn.cjsj.im.gty.LogUtils;
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
import cn.cjsj.im.gty.bean.NewsResponse;
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
import cn.cjsj.im.server.utils.NLog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by LuoYang on 17/2/23.
 */
public class HttpMethods {

//                        public static final String BASE_URL = "http://47.97.26.127:8080/app/";  //公网环境
//    public static final String BASE_URL = "http://192.168.17.57:8080/"; //本地
//    public static final String BASE_URL = "http://192.168.17.252:8080/"; // 詹
    public static final String BASE_URL = "http://192.168.15.140:8082/app/";  //内网测试环境

    private static final int DEFAULT_TIMEOUT = 30;

    private Retrofit retrofit;
    private ConnectionService connectionService;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间和Cookie

        OkHttpClient okHttpClient = new OkHttpClient.Builder()

//                .addInterceptor(new SaveCookieInterCeptor())
//                .addInterceptor(new ReadCookieinterCeptor())
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        LogUtils.debug(message);
                        NLog.d(message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY))
//
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                // 对http请求结果进行统一的预处理 GosnResponseBodyConvert
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ResponseConvertFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        connectionService = retrofit.create(ConnectionService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /*********************************************以下是请求业务函数***************************************************************/
    /**
     * 登录
     *
     * @param subscriber 由调用者传过来的观察者对象
     * @param phone      账号
     * @param token      会话ID
     */
    public void postToken(Subscriber<JSONObject> subscriber, String phone, String token, String jPushId, String cookie) {


        Observable observable = connectionService.postToken(phone, token, jPushId, cookie)
                .map(new HttpResultFunc<JSONObject>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 账号密码登录
     *
     * @param subscriber
     * @param phone
     * @param password
     * @param jPushId
     */
    public void login(Subscriber<JSONObject> subscriber, String phone, String password, String jPushId) {
        Observable observable = connectionService.login(phone, password, jPushId)
                .map(new HttpResultFunc<JSONObject>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 待办
     *
     * @param subscriber
     * @param token
     */
    public void getBackLog(Subscriber<AgendaResponse> subscriber, String token) {
        Observable observable = connectionService.getBackLog(App.getInstance().getToken(), 0, 99, 1)
                .map(new HttpResultFunc<AgendaResponse>());

        toSubscribe(observable, subscriber);
    }


    /**
     * 我的请求
     *
     * @param subscriber
     * @param token
     */
    public void getMyRequest(Subscriber<AgendaResponse> subscriber, String token) {
        Observable observable = connectionService.getMyRequest(App.getInstance().getToken(), 2, 99, 1)
                .map(new HttpResultFunc<AgendaResponse>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 消息列表
     *
     * @param subscriber
     * @param pageSize
     * @param currentPage
     */
    public void getNewS(Subscriber<List<NewsResponse>> subscriber, String token, int pageSize, int currentPage) {
        Observable observable = connectionService.getNewS(App.getInstance().getToken(), pageSize, currentPage)
                .map(new HttpResultFunc<List<NewsResponse>>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 上传图片
     *
     * @param subscriber
     * @param file
     */
    public void getToPicUpdata(Subscriber<String> subscriber, File file, String token) {
        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();

        Observable observable = connectionService.uploadHeadFile(requestBody, App.getInstance().getToken())
                .map(new HttpResultFunc<String>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 公告
     *
     * @param subscriber
     * @param token
     */
    public void getNotice(Subscriber<NoticeAndIntegralBean> subscriber, String token, int type, int pageSize, int currentPage) {
        Observable observable = connectionService.getNotice(App.getInstance().getToken(), type, pageSize, currentPage)
                .map(new HttpResultFunc<NoticeAndIntegralBean>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 获取所有模块信息
     *
     * @param subscriber
     * @param token
     */
    public void getCustomAll(Subscriber<List<CustomAllResponse>> subscriber, String token) {
        Observable observable = connectionService.getCustomAll(App.getInstance().getToken())
                .map(new HttpResultFunc<List<CustomAllResponse>>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 登出
     *
     * @param subscriber
     * @param token
     */
    public void logOut(Subscriber<Object> subscriber, String token) {
        Observable observable = connectionService.logOut(App.getInstance().getToken())
                .map(new HttpResultFunc<Object>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 验证用户来源
     *
     * @param subscriber
     * @param phone
     */
    public void verifyAccount(Subscriber<String> subscriber, String phone) {
        Observable observable = connectionService.verifyAccount(phone)
                .map(new HttpResultFunc<String>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 修改OA密码
     *
     * @param subscriber
     * @param phone
     * @param password
     */
    public void updatePassword(Subscriber<String> subscriber, String phone, String password) {
        Observable observable = connectionService.updatePassword(phone, password)
                .map(new HttpResultFunc<String>());

        toSubscribe(observable, subscriber);
    }


    /**
     * 获取版本信息
     *
     * @param subscriber
     */
    public void getVersion(Subscriber<VersionBean> subscriber) {
        Observable observable = connectionService.getVersion()
                .map(new HttpResultFunc<VersionBean>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 获取用户信息
     *
     * @param subscriber
     * @param token
     */
    public void getUserInfo(Subscriber<OAUserBean> subscriber, String token) {
        Observable observable = connectionService.getUserInfo(App.getInstance().getToken())
                .map(new HttpResultFunc<OAUserBean>());

        toSubscribe(observable, subscriber);
    }


    /**
     * 获取积分
     *
     * @param subscriber
     * @param token
     */
    public void getIntegral(Subscriber<IntegralBean> subscriber, String token) {
        Observable observable = connectionService.getMineIntegral(App.getInstance().getToken(), 5, 1, 0)
                .map(new HttpResultFunc<IntegralBean>());

        toSubscribe(observable, subscriber);
    }


    /**
     * 发送意见反馈
     *
     * @param subscriber
     * @param token
     * @param content
     */
    public void sendFeedback(Subscriber<String> subscriber, String token, String content) {
        Observable observable = connectionService.sendFeedback(App.getInstance().getToken(), content)
                .map(new HttpResultFunc<String>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 获取绩效
     *
     * @param subscriber
     * @param token
     */
    public void getPerformance(Subscriber<PerformanceBean> subscriber, String token) {
        Observable observable = connectionService.getPerformance(App.getInstance().getToken(), "1", 1, 1)
                .map(new HttpResultFunc<PerformanceBean>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 获取是否加入考勤组
     *
     * @param subscriber
     * @param token
     */
    public void getIsJoinWorkGroup(Subscriber<Integer> subscriber, String token) {
        Observable observable = connectionService.getIsJoinWorkGroup(App.getInstance().getToken())
                .map(new HttpResultFunc<Integer>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 查询考勤
     *
     * @param subscriber
     * @param token
     * @param lot
     * @param lat
     */
    public void getCheck(Subscriber<CheckResponse> subscriber, String token, double lot, double lat) {
        Observable observable = connectionService.getCheck(App.getInstance().getToken(), lot, lat)
                .map(new HttpResultFunc<CheckResponse>());

        toSubscribe(observable, subscriber);

    }

    /**
     * 查询日报填写
     *
     * @param subscriber
     * @param token
     * @param type
     * @param pageSize
     * @param currentPage
     */
    public void getDailyPaper(Subscriber<DailyPaperModel> subscriber, String token, byte type, int pageSize, int currentPage) {
        Observable observable = connectionService.getDailyPaper(App.getInstance().getToken(), type, pageSize, currentPage)
                .map(new HttpResultFunc<DailyPaperModel>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 检查考勤
     *
     * @param subscriber
     * @param token
     */
    public void getCheckToday(Subscriber<JSONObject> subscriber, String token) {
        Observable observable = connectionService.getCheckToday(App.getInstance().getToken())
                .map(new HttpResultFunc<JSONObject>());

        toSubscribe(observable, subscriber);
    }


    /**
     * 获取集团发文
     *
     * @param subscriber
     * @param token
     * @param pageSize
     * @param currentPage
     */
    public void getGroupDisPatch(Subscriber<GroupDispatchResponseBean> subscriber, String token, int pageSize, int currentPage) {
        Observable observable = connectionService.getGroupDisPatch(App.getInstance().getToken(), pageSize, currentPage)
                .map(new HttpResultFunc<GroupDispatchResponseBean>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 获取部门发文
     *
     * @param subscriber
     * @param token
     * @param pageSize
     * @param currentPage
     */
    public void getDepartmentDispatch(Subscriber<DepartmentDispatchListBean> subscriber, String token, int pageSize, int currentPage) {
        Observable observable = connectionService.getDepartmentDispatch(App.getInstance().getToken(), pageSize, currentPage)
                .map(new HttpResultFunc<DepartmentDispatchListBean>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 公告详情
     *
     * @param subscriber
     * @param token
     * @param id
     */
    public void getNoticesDetail(Subscriber<NoticeDispatchSupport> subscriber, String token, long id) {
        Observable observable = connectionService.getNoticeDetail(App.getInstance().getToken(), id)
                .map(new HttpResultFunc<NoticeDispatchSupport>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 集团发文详情
     *
     * @param subscriber
     * @param token
     * @param id
     */
    public void getGroupDispatchDetail(Subscriber<GroupDispatchDetailBean> subscriber, String token, long id) {
        Observable observable = connectionService.getGroupDispatchDetail(App.getInstance().getToken(), id)
                .map(new HttpResultFunc<GroupDispatchDetailBean>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 部门发文详情
     *
     * @param subscriber
     * @param token
     * @param id
     */
    public void getDepartmentDispatchDetail(Subscriber<DepartmentDispatchDetailBean> subscriber, String token, long id) {
        Observable observable = connectionService.getDepartmentDispatchDetail(App.getInstance().getToken(), id)
                .map(new HttpResultFunc<DepartmentDispatchDetailBean>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 获取项目列表
     *
     * @param subscriber
     * @param token
     */
    public void getProjectList(Subscriber<List<ProjectListBeanResponse>> subscriber, String token, RequestBody body) {
        Observable observable = connectionService.getProjectList(App.getInstance().getToken(), body)
                .map(new HttpResultFunc<List<ProjectListBeanResponse>>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 关注取消项目
     *
     * @param subscriber
     * @param token
     * @param xmid       项目ID
     * @param action     (项目关注 action=add|取消关注action=del)
     */
    public void putAttention(Subscriber<String> subscriber, String token, long xmid, String action) {
        Observable observable = connectionService.putAttention(App.getInstance().getToken(), xmid, action)
                .map(new HttpResultFunc<String>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 获取组织结构
     *
     * @param subscriber
     * @param token
     */
    public void getOrgList(Subscriber<List<OrganizationBean>> subscriber, String token) {
        Observable observable = connectionService.getOrgList(App.getInstance().getToken())
                .map(new HttpResultFunc<List<OrganizationBean>>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 获取部门人员列表信息
     *
     * @param subscriber
     * @param token
     * @param orgId
     */
    public void getDepartmentStaff(Subscriber<List<DepartmentStaffBean>> subscriber, String token, long orgId) {
        Observable observable = connectionService.getDepartmentStaff(App.getInstance().getToken(), orgId)
                .map(new HttpResultFunc<List<DepartmentStaffBean>>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 获取员工详情
     *
     * @param subscriber
     * @param token
     * @param userId
     */
    public void getStaffDetail(Subscriber<SysUserBean> subscriber, String token, long userId) {
        Observable observable = connectionService.getStaffDetail(App.getInstance().getToken(), userId)
                .map(new HttpResultFunc<SysUserBean>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 搜索员工
     *
     * @param subscriber
     * @param token
     * @param searchParameter
     */
    public void searchStaff(Subscriber<List<DepartmentStaffBean>> subscriber, String token, String searchParameter) {
        Observable observable = connectionService.searchStaff(App.getInstance().getToken(), searchParameter)
                .map(new HttpResultFunc<List<DepartmentStaffBean>>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 提交考勤数据
     *
     * @param subscriber
     * @param token
     * @param address
     * @param lot
     * @param lat
     * @param type       考勤类型(PUNCHING_TIME_CARD 考勤打卡 FIELD_CLOCK 外勤打卡)
     * @param operation  操作(0上班 1下班)
     */
    public void postCheckData(Subscriber<String> subscriber, String token, String address, double lot, double lat, String type, int operation, String notes) {
        Observable observable = connectionService.postCheckData(App.getInstance().getToken(), address, lot, lat, type, operation, notes)
                .map(new HttpResultFunc<String>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 获取考勤统计
     *
     * @param subscriber
     * @param token
     * @param yearMonth
     */
    public void getCheckStatistics(Subscriber<CheckStatisticsResponse> subscriber, String token, String yearMonth) {
        Observable observable = connectionService.getCheckStatistics(App.getInstance().getToken(), yearMonth)
                .map(new HttpResultFunc<CheckStatisticsResponse>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 获取早到排行榜
     *
     * @param subscriber
     * @param token
     * @param yearMonthDay
     * @param pageSize
     * @param currentPage
     */
    public void getEarlyArrvalList(Subscriber<EarlyArrvalRankListResponse> subscriber, String token, String yearMonthDay, int pageSize, int currentPage) {
        Observable observable = connectionService.getEarlyArrvalList(App.getInstance().getToken(), yearMonthDay, pageSize, currentPage)
                .map(new HttpResultFunc<EarlyArrvalRankListResponse>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 获取流程发送人列表
     *
     * @param subscriber
     * @param token
     * @param actDeId
     * @param name
     */
    public void getFlowPeopleList(Subscriber<SelectPeopleBean> subscriber, String token, String actDeId, String name) {
        Observable observable = connectionService.getFlowPeopleList(App.getInstance().getToken(), actDeId, name)
                .map(new HttpResultFunc<SelectPeopleBean>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 获取抄送人员列表
     *
     * @param subscriber
     * @param token
     * @param name
     * @param pageSize
     * @param currentPage
     */
    public void getCCPeopleList(Subscriber<SelectPeopleCcResponse> subscriber, String token, String name, int pageSize, int currentPage) {
        Observable observable = connectionService.getCCPeopleList(App.getInstance().getToken(), name, pageSize, currentPage)
                .map(new HttpResultFunc<SelectPeopleCcResponse>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 查询考勤历史记录
     *
     * @param subscriber
     * @param token
     * @param type        0:我发出的,1:我收到的
     * @param pageSize
     * @param currentPage
     */
    public void getReWorkHisList(Subscriber<List<ReWorkHistoryResponse>> subscriber, String token, int type, int pageSize, int currentPage) {
        Observable observable = connectionService.getReWorkHisList(App.getInstance().getToken(), type, pageSize, currentPage)
                .map(new HttpResultFunc<List<ReWorkHistoryResponse>>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 补卡详情
     *
     * @param subscriber
     * @param token
     * @param id
     */
    public void getReWorkDetail(Subscriber<ReWorkDetailResponse> subscriber, String token, long id) {
        Observable observable = connectionService.getReWorkDetail(App.getInstance().getToken(), id)
                .map(new HttpResultFunc<ReWorkDetailResponse>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 获取审批进度
     *
     * @param subscriber
     * @param token
     * @param businessKey
     */
    public void getApprovalPlan(Subscriber<ApprovalPlanResponse> subscriber, String token, long businessKey) {
        Observable observable = connectionService.getApprovalPlan(App.getInstance().getToken(), businessKey)
                .map(new HttpResultFunc<ApprovalPlanResponse>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取审批进度
     *
     * @param subscriber
     * @param token
     * @param businessKey
     */
    public void getApprovalHistory(Subscriber<ApprovalPlanResponse> subscriber, String token, long businessKey) {
        Observable observable = connectionService.getApprovalHistory(App.getInstance().getToken(), businessKey)
                .map(new HttpResultFunc<ApprovalPlanResponse>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 提交补卡申请
     * @param subscriber
     * @param token
     */
    public void postReAttendance(Subscriber<String> subscriber, String token,RequestBody body) {
        Observable observable = connectionService.postReAttendance(App.getInstance().getToken(), body)
                .map(new HttpResultFunc<String>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 获取项目详情
     * @param subscriber
     * @param token
     * @param id
     */
    public void getProjectDetail (Subscriber<ProjectDetailResponse> subscriber, String token, long id){
        Observable observable = connectionService.getProjectDetail(App.getInstance().getToken(),id)
                .map(new HttpResultFunc<ProjectDetailResponse>());

        toSubscribe(observable,subscriber);
    }

    /**
     * 关闭项目
     * @param subscriber
     * @param token
     * @param id
     */
    public void closeProject(Subscriber<String> subscriber,String token,long id){
        Observable observable = connectionService.closeProject(token,id)
                .map(new HttpResultFunc<String>());

        toSubscribe(observable,subscriber);
    }


    /**
     * 修改项目进度
     * @param subscriber
     * @param token
     * @param id
     * @param progressPercent 项目进度百分比
     */
    public void updateProgress(Subscriber<String>subscriber,String token,long id, double progressPercent){
        Observable observable = connectionService.updateProgress(token,id,progressPercent)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable,subscriber);
    }

    /**
     * 获取项目成员列表
     * @param subscriber
     * @param token
     * @param id
     */
    public void getMemberList(Subscriber<ProjectDetailResponse> subscriber, String token, long id){
        Observable observable = connectionService.getMemberList(token,id)
                .map(new HttpResultFunc<ProjectDetailResponse>());

        toSubscribe(observable,subscriber);
    }

    /************************************************************************************************************/
    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }


    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getStatus().equals("ERROR")) {
//                throw new ApiException(100);
            }
            return httpResult.getSubjects();
        }
    }

}

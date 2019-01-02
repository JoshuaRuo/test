package cn.cjsj.im.gty.http;

import java.io.Serializable;

/**错误码
 * Created by xul on 2016/12/28.
 */
public enum ErrorCode implements Serializable {
    SYSTEM_ERROR(ErrorType.SYSTEM,ErrorType.SYSTEM.getName()),
    SYSTEM_ERROR_PAY(ErrorType.SYSTEM,"支付失败"),
    SYSTEM_ERROR_GET_OPENID(ErrorType.SYSTEM,"openid获取失败"),
    SYSTEM_ERROR_CODE_REDIRECT(ErrorType.SYSTEM,"code重定向失败"),
    ERROR_OLD_PASSWORD(ErrorType.USER,"原密码错误"),
    ERROR_PHONE_BINDING_WECHAT(ErrorType.USER,"手机号已经绑定微信"),
    ERROR_NO_BINDING_PHONE(ErrorType.USER,"用户没有绑定手机号"),
    ERROR_PLATE_NUMBER_FORMAT(ErrorType.USER,"车牌格式格式错误"),
    ERROR_BERTH_CODE_FORMAT(ErrorType.USER,"泊位号格式格式错误"),
    ERROR_LOGIN_TIMES(ErrorType.USER,"登陆错误次数超限"),
    REPETITION_PHONE(ErrorType.USER,"用户已注册"),
    ERROR_DATABASE(ErrorType.SYSTEM,"数据库错误"),
    ERROR_NOTE_CODE(ErrorType.SYSTEM,"短信验证码错误"),
    ERROR_NOTE_TYPE(ErrorType.SYSTEM,"短信类型格式错误"),
    ERROR_CAR_PARK_OTHER(ErrorType.USER,"车辆已经停放在别的泊位"),
    ERROR_PAYMENT_AMOUNT_ZERO(ErrorType.USER,"支付金额为0"),
    ERROR_FILE_UPLOAD(ErrorType.SYSTEM,"文件上传错误"),
    ERROR_FILE_TYPE(ErrorType.USER,"文件类型错误"),
    ERROR_PHONE(ErrorType.USER,"手机号格式错误"),
    ERROR_PASSWORD_FORMAT(ErrorType.USER,"密码格式错误"),
    ERROR_NO_DELETE_DEFAULT_PLATE_NUMBER(ErrorType.USER,"默认车牌不能删除"),
    NULL_PARAMETER(ErrorType.USER,"参数不能为空"),
    ERROR_PARAMETER_FORMAT(ErrorType.USER,"参数格式错误"),
    NO_LOGIN(ErrorType.USER,"用户未登陆"),
    NO_ACCOUNT(ErrorType.USER,"账号不存在"),
    NO_AUTH(ErrorType.USER,"用户无权限"),
    NO_ORDER(ErrorType.USER,"没有订单"),
    ERROR_ORDER_HELP(ErrorType.USER,"该订单已提交过申诉，请耐心等待客服处理"),
    NOT_SUFFICIENT_FUNDS(ErrorType.USER,"余额不足"),
    ERROR_PASSWORD(ErrorType.USER,"账号或密码错误"),
    REPETITION_NOTE(ErrorType.USER,"重复的验证码发送请求"),
    REPETITION_COMMENT(ErrorType.USER,"重复的评价"),
    REPETITION_FEEDBACK(ErrorType.USER,"重复的反馈"),
    REPETITION_PLATE_NUMBER_BOUND(ErrorType.USER,"重复的车牌绑定"),
    NULL_BOUND_PLATE_NUMBER(ErrorType.USER,"未绑定车牌"),
    NULL_DEFAULT_PLATE_NUMBER(ErrorType.USER,"未设置默认车牌"),
    NULL_CAR_PARK_STALL(ErrorType.USER,"泊位上没有车辆"),
    ERROR_STALL_CODE(ErrorType.USER,"泊位号输入错误"),
    OCCUPATION_CAR_PARK_STALL(ErrorType.USER,"泊位上已被占用"),
    REPETITION_PLATE_NUMBER(ErrorType.USER,"该车牌已被其他用户绑定,请联系客服");


    private ErrorType errorType;//错误类型
    private String errorValue;//错误信息
    ErrorCode(ErrorType errorType, String errorValue){
        this.errorType = errorType;
        this.errorValue = errorValue;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public String getErrorValue() {
        return errorValue;
    }

    public void setErrorValue(String errorValue) {
        this.errorValue = errorValue;
    }

}
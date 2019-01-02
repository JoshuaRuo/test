package cn.cjsj.im.gty;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cjsj.im.R;
import cn.cjsj.im.gty.common.ConstantValue;
import cn.cjsj.im.gty.home.base.Common;
import cn.cjsj.im.ui.activity.AgendaDetailActivity;
import cn.cjsj.im.ui.activity.CallOnRecordActivity;
import cn.cjsj.im.ui.activity.CheckWorkTabActivity;
import cn.cjsj.im.ui.activity.DailyPaperActivity;
import cn.cjsj.im.ui.activity.MainActivity;
import cn.cjsj.im.ui.activity.MonthlyActivity;
import cn.cjsj.im.ui.activity.NoticeDetailActivity;
import cn.cjsj.im.ui.activity.ProductionLogActivity;
import cn.cjsj.im.ui.activity.TaskActivity;
import cn.cjsj.im.ui.activity.VoteActivity;
import cn.cjsj.im.ui.activity.WeeklyLogActivity;
import cn.jpush.android.api.JPushInterface;

/**
 * 获取推送的消息 并跳转至相应的页面
 *
 * @author wangxy
 * @version 1.0.1
 * @date 2016/10/17
 */
public class PushReceiver extends BroadcastReceiver {

    private static final String TAG = "JPush";

    /**
     * Notification构造器
     */
    NotificationCompat.Builder mBuilder;

    private List<Map<String, String>> mList = new ArrayList<>();
    private String mMessageString;
    private com.alibaba.fastjson.JSONArray mObjArray;
    private String msgMark = "0";
    private String businessKey;
    private String status;


    private static int badgerCount = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[PushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        HashMap<String, String> mapData = getHashMapData(bundle);
//        initNotificationManager(context);
//        initNotificationBuilder(context,mapData);
        context.sendBroadcast(new Intent(Common.Notification.NOTIFY_REFRESH_MAIN_LIST_DATA));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[PushReceiver][ACTION_REGISTRATION_ID] 接收Registration Id : " + regId);

            ConstantValue.JPUSH_REGISTER_ID = regId;

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[PushReceiver][ACTION_MESSAGE_RECEIVED] 接收到推送下来的自定义消息1: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            Log.d(TAG, "[PushReceiver][ACTION_MESSAGE_RECEIVED] 接收到推送下来的自定义消息2: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
//            processCustomMessage(context, bundle);
            //if (mapData.get("type").equals("1")) {
//            sendCatBroadcastREceiver(context, bundle, mapData);
            //
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[PushReceiver][ACTION_NOTIFICATION_RECEIVED] 接收到推送下来的通知A:" + bundle.getString("cn.jpush.android.ALERT"));
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[PushReceiver][ACTION_NOTIFICATION_RECEIVED] 接收到推送下来的通知的ID: " + notifactionId);
            //if (mapData.get("type").equals("1")) {
//            sendCatBroadcastREceiver(context, bundle, mapData);
            //}
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[PushReceiver][ACTION_NOTIFICATION_OPENED] 接收到的消息：11111");
            if (mapData != null) {
                try {
                    context.startActivity(startActivity(context, mapData));
                }catch (NullPointerException nException){

                }

//                badgerCount = BadgerCountLoadUtils.getInstance().getLoadTag();
//                badgerCount++;
//                BadgerCountLoadUtils.getInstance().setLoadTag(badgerCount);
//                setBadger(context, badgerCount);
            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[PushReceiver][ACTION_RICHPUSH_CALLBACK] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
            // 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[PushReceiver][ACTION_CONNECTION_CHANGE]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[PushReceiver][else] Unhandled intent - " + intent.getAction());
        }
    }

    //type  0待办  1任务交办
    private Intent startActivity(Context context, HashMap<String, String> map) {
        Intent intent = null;
        String actDefId = map.get("actDefId");
//        if (!"1".equals(actDefId)) {
//            String businessKey = map.get("businessKey");
//            String status = map.get("status");
//
//            intent = new Intent(context, AgendaDetailActivity.class);
//            intent.putExtra("actDefId", actDefId);
//            intent.putExtra("businessKey", businessKey + "");
//            intent.putExtra("status", status + "");
//            intent.putExtra("runId", businessKey + "");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        } else {
//            intent = new Intent(context, TaskActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        }

        switch (actDefId) {
            case "0":
                intent = new Intent(context, CheckWorkTabActivity.class);
                intent.putExtra("actDefId", "bksq:1:10000002508861");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                break;

            case "1":
                businessKey = map.get("businessKey");
                intent = new Intent(context, TaskActivity.class);
                intent.putExtra("runId", businessKey + "");
                intent.putExtra("itsFromPush", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;

            case "2":
                intent = new Intent(context, WeeklyLogActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case "3":
                intent = new Intent(context, MonthlyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case "4":
                intent = new Intent(context, CallOnRecordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case "5":
                intent = new Intent(context, ProductionLogActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;

            case "6":
                intent = new Intent(context, DailyPaperActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;

            case "7"://部门发文
                intent = new Intent(context, NoticeDetailActivity.class);
                businessKey = map.get("businessKey");
                intent.putExtra("id", Long.parseLong(businessKey));
                intent.putExtra("dispatchType", "department");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                break;

            case "8"://集团发文

                intent = new Intent(context, NoticeDetailActivity.class);
                businessKey = map.get("businessKey");
                intent.putExtra("id", Long.parseLong(businessKey));
                intent.putExtra("dispatchType", "group");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;

            case "9"://投票

                businessKey = map.get("businessKey");
                status = map.get("status");

                intent = new Intent(context, AgendaDetailActivity.class);
                intent.putExtra("businessKey", businessKey + "");
                intent.putExtra("actDefId", "vote:100001");
                intent.putExtra("status", status + "");
                intent.putExtra("runId", businessKey + "");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;

            case "-1":

                break;
            default:
                businessKey = map.get("businessKey");
                status = map.get("status");

                intent = new Intent(context, AgendaDetailActivity.class);
                intent.putExtra("actDefId", actDefId);
                intent.putExtra("businessKey", businessKey + "");
                intent.putExtra("status", status + "");
                intent.putExtra("runId", businessKey + "");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;

        }
        return intent;
    }




    // send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey1:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey2:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                // 保存消息内容
                sb.append("\nkey3:" + key + ", value:" + bundle.getString(key));
            } else {
                sb.append("\nkey4:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    @SuppressWarnings("unused")
    private HashMap<String, String> getHashMapData(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        HashMap<String, String> map = new HashMap<String, String>();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                // 保存消息内容
                map = toMap(bundle.getString(key));
            }
        }
        return map;
    }

    private static HashMap<String, String> toMap(String json) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (TextUtils.isEmpty(json)) {
            return map;
        }
        Gson gson = new Gson();
        map = gson.fromJson(json, new TypeToken<HashMap<String, String>>() {
        }.getType());
        return map;

    }

    NotificationManager mNotificationManager;

    private void sendCatBroadcastREceiver(Context context, Bundle bundle,
                                          HashMap<String, String> map) {
        String rytMsgType = map.get("title");
        Log.d(TAG, "[PushReceiver][rytMsgType + operateType] 接收Registration Id : " + map.get("title"));
        shake(context);
        Intent intent = new Intent();
        intent.setAction("com.easypark.customer.GETOUT");
        intent.putExtra("rytMsgType", rytMsgType);
        if ("4".equals(rytMsgType)) {
            context.getApplicationContext().sendBroadcast(intent);
        }

        Intent markIconIntent = new Intent();
        markIconIntent.setAction("com.easypark.customer.MarkIcon");
        context.getApplicationContext().sendBroadcast(markIconIntent);


//        try {
//            FileUtils fileUtils = new FileUtils();
//            try {
//                mObjArray = JSON.parseArray(fileUtils.loadData("messageno"));
//                msgMark = fileUtils.loadData("messageMark");
//
//                if (mObjArray != null) {
//                    for (int i = 0; i < mObjArray.size(); i++) {
//                        mList.add((Map<String, String>) mObjArray.get(i));
//                    }
//                }
//            } catch (NullPointerException e) {
//
//            }
//
//            mList.add(map);
//            mMessageString = JSON.toJSONString(mList);
//            fileUtils.saveData("messageno", mMessageString);
//            msgMark = (Integer.parseInt(msgMark) + 1) + "";
//
//            fileUtils.saveData("messageMark", msgMark);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 显示通知栏
     */
    @SuppressLint("InlinedApi")
    public void showNotify(int notifyId, String content, String name) {
        mBuilder.setContentTitle(name).setContentText(content)
                .setTicker("您收到一条新消息").setWhen(System.currentTimeMillis());
        mNotificationManager.notify(notifyId, mBuilder.build());
        // mNotification.notify(getResources().getString(R.string.app_name),
        // notiId, mBuilder.build());
        // mBuilder.setContentTitle("测试标题")
        // .setContentText("测试内容")
        // .setContentIntent(
        // getDefalutIntent(context, Notification.FLAG_AUTO_CANCEL))
        // // .setNumber(number)//显示数量
        // .setTicker("测试通知来啦")// 通知首次出现在通知栏，带上升动画效果的
        // .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
        // .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
        // // .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
        // .setOngoing(false)//
        // ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
        // .setDefaults(Notification.DEFAULT_VIBRATE)//
        // 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
        // // Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
        // // requires VIBRATE permission
        // .setSmallIcon(R.drawable.ic_launcher_work);
    }

    @SuppressWarnings({"static-access", "unused"})
    private void initNotificationManager(Context context) {
        mNotificationManager = (NotificationManager) context.getApplicationContext().getSystemService(context.NOTIFICATION_SERVICE);
    }

    @SuppressWarnings("unused")
    private void initNotificationBuilder(Context context,
                                         HashMap<String, String> map) {
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setOngoing(false)
                .setContentIntent(
                        getDefalutIntent(context, map,
                                Notification.FLAG_AUTO_CANCEL))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.oa_notification_icon);
        mNotificationManager.notify(0, mBuilder.build());


    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT 点击去除：
     * Notification.FLAG_AUTO_CANCEL
     */
    public PendingIntent getDefalutIntent(Context context,
                                          HashMap<String, String> map, int flags) {
        String other = map.get("other");
        String ID = map.get("ID");
        if (TextUtils.isEmpty(ID)) {
            ID = toMap(other).get("ID");
        }
        Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), MainActivity.class);
        intent.putExtra("ID", ID);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context.getApplicationContext(), 1, intent, flags);
        return pendingIntent;
    }

    /**
     * 清除当前创建的通知栏
     */
    public void clearNotify(int notifyId) {
        mNotificationManager.cancel(notifyId);// 删除一个特定的通知ID对应的通知
        // mNotification.cancel(getResources().getString(R.string.app_name));
    }

    /**
     * 清除所有通知栏
     */
    public void clearAllNotify() {
        mNotificationManager.cancelAll();// 删除你发的所有通知
    }

    /**
     * 震动
     */
    private void shake(Context context) {
        Vibrator vibrator = (Vibrator) context.getApplicationContext()
                .getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400}; // 停止 开启 停止 开启
        vibrator.vibrate(pattern, -1);
    }


}

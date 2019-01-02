package cn.cjsj.im.server.broadcast;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import java.io.File;

import cn.cjsj.im.App;
import cn.cjsj.im.gty.LogUtils;

/**
 * 下载文件完成后的接受
 *  文件下载完成以后会发送一个广播 ACTION_DOWNLOAD_COMPLETE
 * @author LuoYang
 * @version 1.0.1
 * @date 2016/12/2
 */
public class DownloadFileBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtils.debug("The DownloadFileBroadcastReceiver action is :" + action);
        if(action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

            //下载完成 移除下载的TAG
            App.getInstance().clearLoadTag();

            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (reference != -1){

                DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);

                //获取下载的地址
                DownloadManager.Query myDownloadQuery = new DownloadManager.Query();
                myDownloadQuery.setFilterById(reference);

                Cursor cursor = downloadManager.query(myDownloadQuery);
                String fileUri = null;
                String fileNamePath = null;
                if (cursor.moveToFirst()) {
//                    int fileNameIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                    int fileUriIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);

//                    fileNamePath = cursor.getString(fileNameIdx);
                    fileUri = cursor.getString(fileUriIdx);
                    fileNamePath = Uri.parse(fileUri).getPath();
                    LogUtils.debug("fileNamePath : " + fileNamePath);
                }
                cursor.close();


                //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ryt-admin.apk");
                File file = new File(fileNamePath);
                Intent i = new Intent();
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setAction(Intent.ACTION_VIEW);
                String type = "application/vnd.android.package-archive";
                i.setDataAndType(Uri.fromFile(file), type);
                context.startActivity(i);
            }
        }
    }
}

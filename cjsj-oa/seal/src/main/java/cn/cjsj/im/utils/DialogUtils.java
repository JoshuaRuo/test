package cn.cjsj.im.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.cjsj.im.R;


/**
 * 对话框 - Utils
 *
 * @author wangxy
 *
 * @version 2.1.0
 */
public class DialogUtils {

    private Activity activity;

    private Dialog dialog;

    public DialogUtils(Activity activity) {
        this.activity = activity;
    }

    public Dialog show() {
        dialog.show();
        return dialog;
    }

    public void dismiss(){
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    /**
     * 确认OR取消弹窗
     * @return
     */
    public void showSelectDialog(String titleContent, View.OnClickListener onClickListener) {
        dialog = new Dialog(this.activity, R.style.edit_AlertDialog_style);
        dialog.setContentView(R.layout.dialog_delete_item_confirm);

        TextView update_version_title = (TextView) dialog.findViewById(R.id.update_version_title);
        update_version_title.setText(titleContent);

        Button dialog_delete_cancel = (Button) dialog.findViewById(R.id.dialog_delete_cancel);
        dialog_delete_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        Button dialog_delete_confirm = (Button) dialog.findViewById(R.id.dialog_delete_confirm);
        dialog_delete_confirm.setOnClickListener(onClickListener);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        dialog.show();
    }





    /**
     * 富弹窗 包含TITLE，左右键自定义内容
     * @param titleContent
     * @param leftContent
     * @param rightContent
     * @param cancelListener 取消
     * @param confirmListener 确认
     */
    public void showRichSelectDialog(String titleContent,
                                     String leftContent, String rightContent, View.OnClickListener confirmListener,View.OnClickListener cancelListener) {

        dialog = new Dialog(this.activity, R.style.edit_AlertDialog_style);
        dialog.setContentView(R.layout.dialog_delete_item_confirm);

        TextView title = (TextView) dialog.findViewById(R.id.update_version_title);
        title.setText(titleContent);

        Button leftCancel = (Button) dialog.findViewById(R.id.dialog_delete_cancel);
        leftCancel.setText(leftContent);
        leftCancel.setOnClickListener(cancelListener);

        Button rightConfirm = (Button) dialog.findViewById(R.id.dialog_delete_confirm);
        rightConfirm.setText(rightContent);
        rightConfirm.setOnClickListener(confirmListener);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);

        dialog.show();
    }

//    /**
//     * 签到弹窗
//     * @param onClickListener
//     */
//    public void showSignDialog(View.OnClickListener onClickListener) {
//
//        dialog = new Dialog(this.activity, R.style.edit_AlertDialog_style);
//        dialog.setContentView(R.layout.layout_dialog_sign);
//
//        Button signBtn = (Button) dialog.findViewById(R.id.dialog_sign_btn);
//        signBtn.setOnClickListener(onClickListener);
//
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//
//        dialog.show();
//    }

    /**
     * 确认OR取消弹窗
     * @return
     */
    public void showUpgradeDialog(String title, String content, View.OnClickListener onConfirmClickListener, View.OnClickListener onCancelClickListener) {
        dialog = new Dialog(this.activity, R.style.edit_AlertDialog_style);
        dialog.setContentView(R.layout.dialog_upgrade_confirm);

        TextView titleTxt = (TextView) dialog.findViewById(R.id.dialog_upgrade_title);
        titleTxt.setText(title);

        TextView contentTxt = (TextView) dialog.findViewById(R.id.dialog_upgrade_content);
        contentTxt.setText(content);

        Button cancelBtn = (Button) dialog.findViewById(R.id.dialog_upgrade_cancel);
        cancelBtn.setOnClickListener(onCancelClickListener);

        Button confirmBtn = (Button) dialog.findViewById(R.id.dialog_upgrade_confirm);
        confirmBtn.setOnClickListener(onConfirmClickListener);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.show();
    }

    /**
     * 判断是否正在展示
     * @return
     */
    public boolean isShowing () {
        if (dialog != null && dialog.isShowing()) {
            return true;
        }
        return false;
    }
}

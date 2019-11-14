package com.etv.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ys.etv.R;
import com.etv.activity.BaseActivity;
import com.etv.util.KeyBoardUtil;
import com.etv.util.MyLog;
import com.etv.util.SharedPerManager;
import com.etv.util.system.LeaderBarUtil;
import com.etv.view.MyToastView;

/***
 * 带一个输入框的
 */
public class EditTextDialog {
    private Activity context;
    private Dialog dialog;
    EditTextDialogListener dialogClick;
    public Button btn_modify, btn_del_all;
    public TextView dialog_title;
    EditText et_username_edit;

    public EditTextDialog(final Activity context) {
        this.context = context;
        dialog = new Dialog(context, R.style.MyDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View dialog_view = View.inflate(context, R.layout.dialog_edit_commit, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SharedPerManager.getScreenWidth(), SharedPerManager.getScreenHeight());
        dialog.setContentView(dialog_view, params);
        btn_modify = (Button) dialog_view.findViewById(R.id.btn_modify);
        et_username_edit = (EditText) dialog_view.findViewById(R.id.et_username_edit);
        dialog_title = (TextView) dialog_view.findViewById(R.id.tv_dialog_title);
        btn_del_all = (Button) dialog_view.findViewById(R.id.btn_del_all);

        btn_del_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                et_username_edit.setText("");
            }
        });

        btn_modify.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (dialogClick != null) {
                    String modifyName = et_username_edit.getText().toString().trim();
                    if (modifyName.contains(" ")) { //去掉空格
                        modifyName = modifyName.replace(" ", "");
                    }
                    if (TextUtils.isEmpty(modifyName)) {
                        MyToastView.getInstance().Toast(context, "请输入！");
                        return;
                    }
                    if (modifyName.trim().length() > 50) {
                        MyToastView.getInstance().Toast(context, "请输入小于50位数字符");
                        return;
                    }
                    dialogClick.commit(modifyName);
                }
                dissmiss();
            }
        });

        et_username_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    KeyBoardUtil.showKeyBord(view);
                } else {
                    KeyBoardUtil.hiddleBord(view);
                }
            }
        });
        RelativeLayout rela_bgg = (RelativeLayout) dialog_view.findViewById(R.id.rela_bgg);
        rela_bgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyBoardUtil.hiddleBord(view);
            }
        });
        LeaderBarUtil.hiddleLeaderBar(context);
    }

    public void show(String title, String content, String commit) {
        dialog_title.setText(title);
        et_username_edit.setText(content);
        btn_modify.setText(commit);
        et_username_edit.setSelection(et_username_edit.getText().toString().length());
        dialog.show();
    }

    public void dissmiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void setOnDialogClickListener(EditTextDialogListener dc) {
        dialogClick = dc;
    }

    public interface EditTextDialogListener {
        void commit(String content);
    }
}

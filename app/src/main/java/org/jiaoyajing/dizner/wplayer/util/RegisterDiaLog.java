package org.jiaoyajing.dizner.wplayer.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.javabean.UserBean;
import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;


/**
 * Created by Dizner on 2017/1/7.
 * 自定义 全局 Dialog
 */

public class RegisterDiaLog {
    private EditText userName;
    private EditText userPass;
    private EditText userPass2;
    private TextView dLiftBtn;
    private TextView dRightBtn;
    private AlertDialog dialog;
    private Activity context;
    private View view;
    private OnEasyDialogBtnClickListener listener;
    private DbManager db;
    private int width;

    public RegisterDiaLog(Activity context) {
        this.context = context;
        db = x.getDb(new DbManager.DaoConfig());
        width = context.getWindowManager().getDefaultDisplay().getWidth();
        view = LayoutInflater.from(context).inflate(R.layout.reg_dialog_layout, null);
        userName = (EditText) view.findViewById(R.id.et_input_name);
        userPass = (EditText) view.findViewById(R.id.et_input_pass);
        userPass2 = (EditText) view.findViewById(R.id.et_input_pass2);
        dLiftBtn = (TextView) view.findViewById(R.id.tv_dialog_lift_btn);
        dRightBtn = (TextView) view.findViewById(R.id.tv_dialog_right_btn);
        dialog = new AlertDialog.Builder(context)
                .setView(view)
                .create();
        dialog.setCanceledOnTouchOutside(false);
    }

    private void initView() {
        dLiftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLiftBtnClilck(v);
                cencle();
            }
        });
        dRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRightBtnClick(v);
                login();
//                cencle();
            }
        });
    }

    public void login() {
        if (TextUtils.isEmpty(userName.getText())) {
            Toast.makeText(context, "请输入账户", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(userPass.getText())) {
            Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show();

        }
        if (TextUtils.isEmpty(userPass2.getText())) {
            Toast.makeText(context, "请确认密码", Toast.LENGTH_SHORT).show();

        }
        if (!userPass.getText().toString().equals(userPass2.getText().toString())) {
            Toast.makeText(context, "两次输入密码不一致，请检查", Toast.LENGTH_SHORT).show();

        } else {
            try {
                UserBean userBean = new UserBean();
                userBean.setName(userName.getText().toString());
                userBean.setPass(userPass.getText().toString());
                db.replace(userBean);
                List<UserBean> all = db.selector(UserBean.class).where(WhereBuilder.b("userName", "=", userName.getText().toString()).and(WhereBuilder.b("passWord", "=", userPass.getText().toString()))).findAll();
                if (all.size() > 0) {
                    listener.onLoging(true);
                } else {
                    listener.onLoging(false);
                }
                cencle();
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    public void show() {
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.drawable.dialog_bg);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (width / 7) * 6;
        lp.height = (width / 7) * 5;
//        lp.y= DensityUtil.dip2px(context,100);
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
    }

    public void cencle() {
        dialog.cancel();
    }

    public void setOnBtnClickListener(OnEasyDialogBtnClickListener listener) {
        this.listener = listener;
        initView();
    }


    public String getLeftBtnString() {
        return dLiftBtn.getText().toString();
    }

    /**
     * 设置左边按钮的文字
     *
     * @param leftBtnString
     */
    public void setLeftBtnString(String leftBtnString) {
        dLiftBtn.setText(leftBtnString);
    }

    public String getRightBtnString() {
        return dRightBtn.getText().toString();
    }

    /**
     * 设置右边按钮的文字
     *
     * @param rightBtnString
     */
    public void setRightBtnString(String rightBtnString) {
        dRightBtn.setText(rightBtnString);
    }

    public interface OnEasyDialogBtnClickListener {
        void onLiftBtnClilck(View v);

        void onRightBtnClick(View v);

        void onLoging(boolean isRight);
    }
}

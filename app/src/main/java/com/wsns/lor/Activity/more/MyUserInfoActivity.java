package com.wsns.lor.Activity.more;


import com.wsns.lor.App.OnlineUserInfo;
import com.wsns.lor.R;
import com.wsns.lor.utils.SetAliasAndTagUtil;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;

import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import im.sdk.debug.RegisterAndLoginActivity;
import im.sdk.debug.activity.setting.UpdateUserAvatar;

/**
 * 个人信息修改界面
 * （头像，昵称，性别）
 */
public class MyUserInfoActivity extends Activity {

    private RelativeLayout re_avatar;
    private RelativeLayout re_name;

    private RelativeLayout re_sex;
    private ImageView iv_avatar;
    private TextView tv_name;
    private Button bt_logout;
    private TextView tv_sex;

    private UserInfo.Gender sex;
    private String nick;
 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        initView();
    }

    private void initView() {

        

        nick = OnlineUserInfo.myInfo.getNickname();
        sex = OnlineUserInfo.myInfo.getGender();
        bt_logout = (Button) findViewById(R.id.bt_exit);
        re_avatar = (RelativeLayout) this.findViewById(R.id.re_avatar);
        re_name = (RelativeLayout) this.findViewById(R.id.re_name);
        re_sex = (RelativeLayout) this.findViewById(R.id.re_sex);

        re_avatar.setOnClickListener(new MyListener());
        re_name.setOnClickListener(new MyListener());
        re_sex.setOnClickListener(new MyListener());

        // 头像
        iv_avatar = (ImageView) this.findViewById(R.id.iv_avatar);
        tv_name = (TextView) this.findViewById(R.id.tv_name);
        tv_sex = (TextView) this.findViewById(R.id.tv_sex);
        tv_name.setText(nick);
        loadAvatar();


        if (UserInfo.Gender.male.equals(sex)) {
            tv_sex.setText("男");
        } else if (UserInfo.Gender.female.equals(sex)) {
            tv_sex.setText("女");
        } else {
            tv_sex.setText("未知");
        }

        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**#################    用户登出    #################*/
                ProgressDialog mProgressDialog = ProgressDialog.show(MyUserInfoActivity.this, "提示：", "正在加载中。。。");

                Intent intent = new Intent();
                if (OnlineUserInfo.myInfo != null) {
                    JMessageClient.logout();
                    mProgressDialog.dismiss();
                    SetAliasAndTagUtil adt = new SetAliasAndTagUtil(MyUserInfoActivity.this);
                    adt.setAlias("null");
                    Toast.makeText(getApplicationContext(), "登出成功", Toast.LENGTH_SHORT).show();
                    intent.setClass(MyUserInfoActivity.this, RegisterAndLoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MyUserInfoActivity.this, "登出失败", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_avatar:
                    Intent intent = new Intent(MyUserInfoActivity.this, UpdateUserAvatar.class);
                    startActivity(intent);
                    break;
                case R.id.re_name:
                    showNikDialog();
                    break;
                case R.id.re_sex:
                    showSexDialog();
                    break;
            }
        }
    }

    private void showNikDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.activity_myinfo_nick_dialog);
        dlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        final EditText tv_title = (EditText) window.findViewById(R.id.tv_nick);
        Button ok = (Button) window.findViewById(R.id.btn_ok);
        Button cancel = (Button) window.findViewById(R.id.btn_cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nick = tv_title.getText().toString();
                tv_name.setText(nick);
                updateNick();
                dlg.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }

    private void showSexDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.activity_myinfo_sex_dialog);
        LinearLayout ll_title = (LinearLayout) window
                .findViewById(R.id.ll_title);
        ll_title.setVisibility(View.VISIBLE);
        TextView tv_title = (TextView) window.findViewById(R.id.tv_title);
        tv_title.setText("性别");
        // 为确认按钮添加事件,执行退出应用操作
        TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
        tv_paizhao.setText("男");
        tv_paizhao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tv_sex.setText("男");
                OnlineUserInfo.myInfo.setGender(UserInfo.Gender.male);
                updateSex();
                dlg.cancel();
            }
        });
        TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
        tv_xiangce.setText("女");
        tv_xiangce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tv_sex.setText("女");
                OnlineUserInfo.myInfo.setGender(UserInfo.Gender.female);
                updateSex();
                dlg.cancel();
            }
        });
    }


    public void back(View view) {
        finish();
    }


    public void updateNick() {
        /**=================     调用sdk 更新nickName    =================*/
        OnlineUserInfo.myInfo.setNickname(nick);
        JMessageClient.updateMyInfo(UserInfo.Field.nickname,  OnlineUserInfo.myInfo, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    Toast.makeText(MyUserInfoActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateSex() {
        if (        OnlineUserInfo.myInfo!= null) {
            /**=================     调用sdk 更新gender    =================*/
            JMessageClient.updateMyInfo(UserInfo.Field.gender,  OnlineUserInfo.myInfo, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i == 0) {
                        Toast.makeText(MyUserInfoActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(MyUserInfoActivity.this, "更新失败info == null", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadAvatar(){
        OnlineUserInfo.myInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                if (bitmap != null)
                    iv_avatar.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        loadAvatar();
        super.onResume();
    }
}

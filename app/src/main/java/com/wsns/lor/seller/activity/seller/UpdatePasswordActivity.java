package com.wsns.lor.seller.activity.seller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wsns.lor.seller.R;
import com.wsns.lor.seller.entity.User;
import com.wsns.lor.seller.http.HttpMethods;
import com.wsns.lor.seller.http.subscribers.ProgressSubscriber;
import com.wsns.lor.seller.http.subscribers.SubscriberOnNextListener;
import com.wsns.lor.seller.utils.MD5;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class UpdatePasswordActivity extends Activity {



    Button btnUpdate;
    ImageView ivback;
    EditText etPassword;
    EditText etRepatedPassword;
    User user;
    SubscriberOnNextListener updatePasswordOnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        init();

    }

    void init() {
        btnUpdate = (Button) findViewById(R.id.btn_next);
        ivback = (ImageView) findViewById(R.id.iv_back);
        etPassword = (EditText) findViewById(R.id.et_mobile);
        etRepatedPassword = (EditText) findViewById(R.id.et_repated_password);
        btnUpdate.setText("确认");
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.slide_out_left);
            }
        });
        updatePasswordOnNext= new SubscriberOnNextListener<User>() {
            @Override
            public void onNext(User user) {
                UpdatePasswordActivity.this.user=user;
                updatePasswordJM();
            }
        };
    }

    void updatePassword() {
                final String password = etPassword.getText().toString();
                if (!password.equals(etRepatedPassword.getText().toString())) {
                    Toast.makeText(UpdatePasswordActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
        HttpMethods.getInstance().updatePassword(new ProgressSubscriber(updatePasswordOnNext, UpdatePasswordActivity.this, false),
                MD5.getMD5(etPassword.getText().toString()));
    }

    void updatePasswordJM() {
        System.out.println(user.getPasswordHash()+"jjjjjjjj"+user.getCreateDate());
        JMessageClient.updateUserPassword(user.getPasswordHash(), MD5.getMD5(etPassword.getText().toString()), new BasicCallback() {
            @Override
            public void gotResult(final int i, String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (i == 0) {
                            Toast.makeText(UpdatePasswordActivity.this, "修改密码成功,请重新登录", Toast.LENGTH_SHORT).show();
                            JMessageClient.logout();

//                            JPushInterface.setAlias(UpdatePasswordActivity.this,"",null);
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.none, R.anim.slide_out_left);
        super.onBackPressed();
    }


}

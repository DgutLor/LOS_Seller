package com.wsns.lor.seller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.wsns.lor.seller.R;
import com.wsns.lor.seller.fragment.regist.RegisterFirstFragment;
import com.wsns.lor.seller.fragment.regist.RegisterSecondFragment;
import com.wsns.lor.seller.fragment.regist.RegisterThirdFragment;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 手机注册界面
 */
public class RegistersActivity extends Activity {

    private String APPKEY = "1b0ff72e02390";
    private String APPSECURITY = "a764fdf75e7a6a9aa291e76bd603c38e";

    RegisterFirstFragment registerFirstFragment = new RegisterFirstFragment();
    RegisterSecondFragment registerSecondFragment = new RegisterSecondFragment();
    RegisterThirdFragment registerThirdFragment = new RegisterThirdFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registers);

        SMSSDK.initSDK(RegistersActivity.this,APPKEY,APPSECURITY);
        EventHandler eh=new EventHandler(){
            @Override
            public void afterEvent(int event, final int result, Object data) {

                switch (event) {
                    case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                        if (result == SMSSDK.RESULT_COMPLETE) {;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    goStep3();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegistersActivity.this,"验证码错误，请重新发送短信验证",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        break;
                    case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    goStep2();
                                }
                            });
                            //默认的智能验证是开启的,我已经在后台关闭
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegistersActivity.this,"获取验证码失败"+result,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        break;
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调

        registerFirstFragment.setOnGoNextListener(new RegisterFirstFragment.OnGoNextListener() {
            @Override
            public void onGoNext() {
                goStep2();
            }
        });
        registerFirstFragment.setOnGoBackListener(new RegisterFirstFragment.OnGoBackListener() {
            @Override
            public void onGoBack() {
                backOut();
            }
        });
        registerSecondFragment.setOnGoNextListener(new RegisterSecondFragment.OnGoNextListener() {
            @Override
            public void onGoNext() {
                goStep3();
            }
        });
        registerSecondFragment.setOnGoBackListener(new RegisterSecondFragment.OnGoBackListener() {
            @Override
            public void onGoBack() {
                goBackStep1();
            }
        });
        registerThirdFragment.setOnGoNextListener(new RegisterThirdFragment.OnGoNextListener(){
            @Override
            public void onGoNext() {
                goStep4();
            }
        });
        registerThirdFragment.setOnGoBackListener(new RegisterThirdFragment.OnGoBackListener() {
            @Override
            public void onGoBack() {
                goBackStep2();
            }
        });
        getFragmentManager().beginTransaction().replace(R.id.container, registerFirstFragment).commit();
    }

    void backOut(){
        finish();
        overridePendingTransition(R.anim.none,R.anim.slide_out_bottom);
    }

    void goBackStep1(){

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.slide_in_left,
                        R.animator.slide_out_right,
                        R.animator.slide_in_right,
                        R.animator.slide_out_left)
                .replace(R.id.container, registerFirstFragment)
                .commit();
    }
    void goStep2(){

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.slide_in_right,
                        R.animator.slide_out_left,
                        R.animator.slide_in_left,
                        R.animator.slide_out_right)
                .replace(R.id.container, registerSecondFragment)
                .addToBackStack(null)
                .commit();
    }
    void goBackStep2(){

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.slide_in_left,
                        R.animator.slide_out_right,
                        R.animator.slide_in_right,
                        R.animator.slide_out_left)
                .replace(R.id.container, registerSecondFragment)
                .commit();
    }
    void goStep3(){

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.slide_in_right,
                        R.animator.slide_out_left,
                        R.animator.slide_in_left,
                        R.animator.slide_out_right)
                .replace(R.id.container, registerThirdFragment)
                .addToBackStack(null)
                .commit();
    }
    void goStep4(){
        Intent itnt = new Intent(RegistersActivity.this,RegisterSuccessActivity.class);
        startActivity(itnt);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.none);
    }


}

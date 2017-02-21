package com.wsns.lor.seller.activity.seller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wsns.lor.seller.application.OnlineUserInfo;
import com.wsns.lor.seller.R;
import com.wsns.lor.seller.entity.Poi;
import com.wsns.lor.seller.http.HttpMethods;
import com.wsns.lor.seller.http.subscribers.ProgressSubscriber;
import com.wsns.lor.seller.http.subscribers.SubscriberOnNextListener;
import com.wsns.lor.seller.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.wsns.lor.seller.application.OnlineUserInfo.poi;

/**
 * 商家信息编辑界面
 */
public class SellerInfoEditActivity extends Activity {


    @Bind(R.id.jmui_commit_btn)
    Button poiCommitBtn;

    SubscriberOnNextListener updatePoiOnNext;
    @Bind(R.id.return_btn)
    ImageButton returnBtn;
    @Bind(R.id.et_category)
    EditText etCategory;
    @Bind(R.id.et_address)
    TextView etAddress;
    @Bind(R.id.et_hotline)
    EditText etHotline;
    @Bind(R.id.et_worktime)
    EditText etWorktime;
    @Bind(R.id.et_service)
    EditText etService;
    @Bind(R.id.tv_discount)
    TextView tvDiscount;
    @Bind(R.id.tv_notice)
    EditText tvNotice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellerinfo_edit);
        ButterKnife.bind(this);


        etCategory.setText(poi.getRepairsTypes());
        tvNotice.setText(poi.getNotice());
        etService.setText(poi.getService() + "");
        etHotline.setText(poi.getHotline());
        etWorktime.setText(poi.getWorktime());
        tvDiscount.setText(poi.getTradeTypes() == 1 ? "上门+邮寄" : "邮寄");

        updatePoiOnNext = new SubscriberOnNextListener<Poi>() {
            @Override
            public void onNext(Poi poi) {
                OnlineUserInfo.poi = poi;
                ToastUtil.show(SellerInfoEditActivity.this, "修改成功");
            }
        };


    }

    @Override
    protected void onResume() {
        super.onResume();

        etAddress.setText(poi.getAddress());

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick({R.id.jmui_commit_btn, R.id.et_address, R.id.return_btn, R.id.tv_discount})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.jmui_commit_btn:
                poi.setRepairsTypes(etCategory.getText().toString());
                poi.setHotline(etHotline.getText().toString());
                poi.setWorktime(etWorktime.getText().toString());
                poi.setService(Double.valueOf(etService.getText().toString()));
                poi.setNotice(tvNotice.getText().toString());

                HttpMethods.getInstance().updatePoi(new ProgressSubscriber(updatePoiOnNext, this, true),
                        poi);
                break;
            case R.id.et_address:
                Intent intent = new Intent(this, SellerAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.return_btn:
                finish();
                break;
            case R.id.tv_discount:
                setPaywayPopupView(view);
                break;

        }
    }

    private void setPaywayPopupView(View view) {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        View popupView = getLayoutInflater().inflate(R.layout.dialog_trade_way, null);

        final PopupWindow mPopupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.mystyle);
        mPopupWindow.getContentView().setFocusableInTouchMode(true);
        mPopupWindow.getContentView().setFocusable(true);
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);

        mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        TextView payOnline = (TextView) popupView.findViewById(R.id.tv_pay_online);
        TextView payOutline = (TextView) popupView.findViewById(R.id.tv_pay_outline);
        payOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDiscount.setText("邮寄");
                mPopupWindow.dismiss();
                poi.setTradeTypes(2);
            }
        });
        payOutline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDiscount.setText("上门+邮寄");
                poi.setTradeTypes(1);
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, -200);
    }
}

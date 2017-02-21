package com.wsns.lor.seller.activity.seller;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.wsns.lor.seller.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.wsns.lor.seller.application.OnlineUserInfo.address;
import static com.wsns.lor.seller.application.OnlineUserInfo.latitude;
import static com.wsns.lor.seller.application.OnlineUserInfo.longitude;
import static com.wsns.lor.seller.application.OnlineUserInfo.poi;

/*
*地址信息修改界面（点击信息修改界面的地址栏进入）
 */
public class SellerAddressActivity extends Activity {

    @Bind(R.id.jmui_commit_btn)
    Button jmuiCommitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_address);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.jmui_commit_btn)
    public void onClick() {

        if (address != null) {
            poi.setAddress(address);
            ArrayList list = new ArrayList();
            list.add(longitude);
            list.add(latitude);

            poi.setLocation(list);

        }
        finish();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

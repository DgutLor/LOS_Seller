package com.wsns.lor.seller.activity.seller;

import android.app.Activity;
import android.os.Bundle;

import com.wsns.lor.seller.R;

/**
 * 商品（品牌和型号）编辑界面，具体实现在fragment里面
 */
public class SellerGoodsEditActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_goods_edit);
    }
}

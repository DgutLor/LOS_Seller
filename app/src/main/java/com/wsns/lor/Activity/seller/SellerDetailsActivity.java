package com.wsns.lor.Activity.seller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;

import com.wsns.lor.R;
import com.wsns.lor.entity.Seller;
import com.wsns.lor.http.subscribers.LoadImageTask;
import com.wsns.lor.utils.ToastUtil;

import java.util.HashMap;


/**
 * Created by cuiyang on 15/12/16.
 */
public class SellerDetailsActivity extends Activity implements CloudSearch.OnCloudSearchListener {


    private String mTableID = "580706aeafdf523f4f0ff73b";
    private String mId; // 用户table 行编号

    TextView tvmonthlytrading;
    TextView tvcomment;

    TextView tvstorename;
    TextView tvstoretype;
    String commentNum;

    private CloudSearch mCloudSearch;
    Seller seller;

    CoordinatorLayout root_layout;
    LinearLayout linearLayout;
    ImageButton ibchat;
    View animationLayout;
    FrameLayout contentLayout;
    ImageView icon;
    LinearLayout transLayout;
    LinearLayout alphaLayout;
    LinearLayout tabLayout;
    TradeFragment tradeFragment;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbar;
    ImageView backButton;
    TextView tvtitle;
    RatingBar star;
    LoadImageTask loadImageTask;
    private CollapsingToolbarLayoutState state;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    private final DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator(2f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellerdetails);
        mCloudSearch = new CloudSearch(this);
        mCloudSearch.setOnCloudSearchListener(this);

        mId = getIntent().getStringExtra("ID");
        searchById();
        initView();
        initViewPagerAndTabs();
        loadImageTask = new LoadImageTask(this);
        loadImageTask.setmViewGroup((ViewGroup) findViewById(android.R.id.content));


    }

    private void initView() {

        root_layout = (CoordinatorLayout) findViewById(R.id.root_layout);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        linearLayout = (LinearLayout) findViewById(R.id.ll_comment);
        ibchat = (ImageButton) findViewById(R.id.ib_chat);
        animationLayout = findViewById(R.id.animation_layout);
        contentLayout = (FrameLayout) findViewById(R.id.content_layout);
        icon = (ImageView) findViewById(R.id.iv_icon);
        transLayout = (LinearLayout) findViewById(R.id.trans_layout);
        alphaLayout = (LinearLayout) findViewById(R.id.alpha_layout);
        tabLayout = (LinearLayout) findViewById(R.id.tab_layout);
        tvcomment = (TextView) findViewById(R.id.tv_comment);
        tvstorename = (TextView) findViewById(R.id.tv_name);
        tvstoretype = (TextView) findViewById(R.id.tv_category);
        backButton = (ImageView) findViewById(R.id.iv_back);
        star = (RatingBar) findViewById(R.id.star);
        tvmonthlytrading = (TextView) findViewById(R.id.tv_monthlytrading);
        tvtitle = (TextView) findViewById(R.id.tv_title);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        if (collapsingToolbar != null) {
            //设置隐藏图片时候ToolBar的颜色
            collapsingToolbar.setContentScrimColor(Color.parseColor("#f49056"));
            //设置工具栏标题
            collapsingToolbar.setTitleEnabled(false);
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        collapsingToolbar.setTitle("");//设置title不显示
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                        tvtitle.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == CollapsingToolbarLayoutState.COLLAPSED) {
                            tvtitle.setVisibility(View.GONE);//由折叠变为中间状态时隐藏播放按钮
                        }
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });

        root_layout.setVisibility(View.INVISIBLE);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(SellerDetailsActivity.this, "评论");
            }
        });
        ibchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(SellerDetailsActivity.this, "咨询");
            }
        });
    }

    private void setDefaultFragment() {

        tradeFragment = new TradeFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        //add（）方法：在当前显示时，点击Back键不出现白板。是正确的相应Back键，即退出我们的Activity
        transaction.add(R.id.fragment_content, tradeFragment);
        transaction.commit();
        tradeFragment.setSeller(seller);
    }


    private void initViewPagerAndTabs() {
        //等待绘图完成再进行动画这样才能取到控件的宽高 oncreate中只是绘制
        root_layout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                root_layout.getViewTreeObserver().removeOnPreDrawListener(this);
                startCircularReveal();
                return true;
            }
        });
    }

    /**
     * 模仿createCircularReveal方法实现的效果.
     */
    //  ViewAnimationUtils.createCircularReveal(contentLayout, 0, 0, 0, (float) Math.hypot(contentLayout.getWidth(), contentLayout.getHeight())).setDuration(1500).start();不能在主线程直接.目前试验只能在点击事件里才行.
    private void startCircularReveal() {
        ViewPropertyAnimator animator = animationLayout.animate();
        animator.scaleY(contentLayout.getHeight()).scaleX(contentLayout.getWidth()).setDuration(800).setStartDelay(300).setInterpolator(new AccelerateInterpolator()).start();
        animator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startTitleViewAimation();
            }
        });
    }

    private void startTitleViewAimation() {
        root_layout.setVisibility(View.VISIBLE);

        icon.setTranslationY(-icon.getHeight());
        icon.animate().translationY(0f).setDuration(800).setInterpolator(mDecelerateInterpolator);

        transLayout.setTranslationY(-transLayout.getHeight());
        transLayout.animate().translationY(0f).setDuration(1300).setInterpolator(mDecelerateInterpolator);

        ObjectAnimator animator1 = new ObjectAnimator().ofFloat(alphaLayout, "alpha", 0, 1f);
        ObjectAnimator animator2 = new ObjectAnimator().ofFloat(tabLayout, "alpha", 0, 1f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator1, animator2);
        set.setDuration(1000);
        set.setInterpolator(mDecelerateInterpolator);
        set.start();
    }


    public void searchById() {
        mCloudSearch.searchCloudDetailAsyn(mTableID, mId);

    }

    @Override
    public void onCloudSearched(CloudResult cloudResult, int i) {

    }

    @Override
    public void onCloudItemDetailSearched(CloudItemDetail item, int rCode) {
        if (rCode == 1000 && item != null) {
            setDate(item);

        } else {
            ToastUtil.showerror(this, rCode);
        }
    }

    public void back(View view) {
        finish();
    }

    private void setDate(CloudItemDetail detail) {
        HashMap<String, String> map = detail.getCustomfield();

        seller = new Seller();
        seller.setID(detail.getID());
        seller.setNick(detail.getTitle());
        seller.setAvatar(detail.getCloudImage().get(0).getPreurl());
        seller.setTelNumber(map.get("tel"));
        seller.setType(map.get("type"));
        seller.setWorktime(map.get("worktime"));
        seller.setStoreAddress(detail.getSnippet());
        seller.setNocice(map.get("notice"));
        seller.setEvents(map.get("events"));
        seller.setMonth(map.get("month"));
        seller.setStar(map.get("star"));
        tvcomment.setText(map.get("comment"));
        tvstorename.setText(seller.getNick());
        tvstoretype.setText("维修类别:" + seller.getType());
        tvtitle.setText(seller.getNick());//设置title
        star.setRating(Float.parseFloat(seller.getStar()));
        tvmonthlytrading.setText(seller.getMonth());
        if (!seller.getAvatar().equals("")) {
            icon.setTag(seller.getAvatar());
            loadImageTask.loadBitmaps(icon,seller.getAvatar());
        }
        setDefaultFragment();
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}

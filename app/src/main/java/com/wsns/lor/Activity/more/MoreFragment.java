package com.wsns.lor.Activity.more;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wsns.lor.R;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 *更多设置页Fragment
 */
public class MoreFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private LinearLayout lly1, lly2, lly3, lly4, lly5, lly6;
    private LinearLayout llyMy;
    private String nick;
    private ImageView iv_avatar;
    private TextView tv_name;
    private LinearLayout llmore;
    private UserInfo myInfo;
    
    public MoreFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView= inflater.inflate(R.layout.fragment_more, container, false);
        initView();
        return mView;
    }

    private void initView() {
        llmore = (LinearLayout) mView.findViewById(R.id.ll_more);
        lly1 = (LinearLayout) mView.findViewById(R.id.lly_1);
        lly2 = (LinearLayout) mView.findViewById(R.id.lly_2);
        lly3 = (LinearLayout) mView.findViewById(R.id.lly_3);
        lly4 = (LinearLayout) mView.findViewById(R.id.lly_4);
        lly5 = (LinearLayout) mView.findViewById(R.id.lly_5);
        lly6 = (LinearLayout) mView.findViewById(R.id.lly_6);
        llyMy = (LinearLayout) mView.findViewById(R.id.lly_my);
        lly1.setOnClickListener(this);
        lly2.setOnClickListener(this);
        lly3.setOnClickListener(this);
        lly4.setOnClickListener(this);
        lly5.setOnClickListener(this);
        lly6.setOnClickListener(this);
        llyMy.setOnClickListener(this);
        iv_avatar = (ImageView) mView.findViewById(R.id.iv_icon);
        tv_name = (TextView) mView.findViewById(R.id.tv_name);

        setAvatarAndNickName();

    }

    //        ImageLoadUtil mAdapter=new ImageLoadUtil(Images.imageThumbUrls,R.layout.photo_layout,this,lv);

    public void setAvatarAndNickName(){
        myInfo = JMessageClient.getMyInfo();
        if (myInfo.getNickname().equals(""))
            nick=myInfo.getUserName();
        else
            nick=myInfo.getNickname();
        tv_name.setText(nick);

        myInfo = JMessageClient.getMyInfo();
        myInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                if (bitmap!=null)
                    iv_avatar.setImageBitmap(bitmap);
            }
        });
    }


    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        switch (v.getId()) {
            case R.id.lly_1:
                break;
            case R.id.lly_2:
                break;
            case R.id.lly_3:
                break;
            case R.id.lly_4:
                break;
            case R.id.lly_5:
                break;
            case R.id.lly_6:
                break;
            case R.id.lly_my:
                startActivity(new Intent(getActivity(),
                        MyUserInfoActivity.class));
                break;
        }
    }

    @Override
    public void onResume() {
        setAvatarAndNickName();
        super.onResume();
    }
}

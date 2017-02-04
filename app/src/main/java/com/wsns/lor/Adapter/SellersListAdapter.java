package com.wsns.lor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.wsns.lor.Activity.seller.MyItemClickListener;
import com.wsns.lor.R;
import com.wsns.lor.entity.Seller;
import com.wsns.lor.http.HttpMethods;
import com.wsns.lor.utils.PxtDipTransform;

import java.util.List;

/**
 * Created by Administrator on 2016/12/21.
 */

public class SellersListAdapter extends BaseAdapter {
    ImageView albumsImg;
    Context context;
    List<Seller> mSellers;
    int height, width;
    TextView storename;
    TextView storeType;
    TextView body;
    TextView head;
    TextView body2;
    TextView head2;
    TextView body3;
    TextView head3;
    RatingBar star;
    TextView msales;
    TextView distance;
    ImageView icon;
    private int[] valueViewID = {R.id.tv_storename, R.id.star, R.id.tv_mSales, R.id.tv_storeType, R.id.iv_icon, R.id.tv_activity_head1, R.id.tv_activity_body1, R.id.tv_activity_head2, R.id.tv_activity_body2, R.id.tv_activity_head3, R.id.tv_activity_body3, R.id.tv_distance};

    public SellersListAdapter(Context context, List<Seller> mSellers) {
        this.context = context;
        this.mSellers = mSellers;
    }

    @Override
    public int getCount() {
        return mSellers == null ? 0 : mSellers.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = null;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.recyclerview_item_seller, null);
        } else {
            view = convertView;
        }
        Seller seller = mSellers.get(i);

        storename = (TextView) view.findViewById(valueViewID[0]);
        star = (RatingBar) view.findViewById(valueViewID[1]);
        msales = (TextView) view.findViewById(valueViewID[2]);
        storeType = (TextView) view.findViewById(valueViewID[3]);
        icon = (ImageView) view.findViewById(valueViewID[4]);

        head = (TextView) view.findViewById(valueViewID[5]);
        body = (TextView) view.findViewById(valueViewID[6]);

        head2 = (TextView) view.findViewById(valueViewID[7]);
        body2 = (TextView) view.findViewById(valueViewID[8]);

        head3 = (TextView) view.findViewById(valueViewID[9]);
        body3 = (TextView) view.findViewById(valueViewID[10]);

        distance = (TextView) view.findViewById(valueViewID[11]);
        String avatarUrl = HttpMethods.BASE_URL + seller.getAvatar();

        Picasso.with(context).load(avatarUrl).fit().error(R.drawable.unknow_avatar).into(icon);


        return view;
    }


}

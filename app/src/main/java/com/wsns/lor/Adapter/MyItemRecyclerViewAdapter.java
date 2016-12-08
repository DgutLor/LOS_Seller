package com.wsns.lor.Adapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.wsns.lor.Activity.seller.MyItemClickListener;
import com.wsns.lor.R;
import com.wsns.lor.entity.Seller;
import com.wsns.lor.http.subscribers.LoadImageTask;
import com.wsns.lor.utils.StringDivide;
import com.wsns.lor.utils.ToastUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.DiskLruCache;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String[] keyString = {"storename", "star", "mSales", "storeType", "imgpath", "describe", "storeuserID"};
    private int[] valueViewID = {R.id.tv_storename, R.id.star, R.id.tv_mSales, R.id.tv_storeType, R.id.iv_icon, R.id.tv_activity_head1, R.id.tv_activity_body1, R.id.tv_activity_head2, R.id.tv_activity_body2, R.id.tv_activity_head3, R.id.tv_activity_body3, R.id.tv_distance};
    private MyItemClickListener mItemClickListener;
    private List<Seller> mValues;
    LoadImageTask loadImageTask;

    public MyItemRecyclerViewAdapter(List<Seller> items, Context context) {
        mValues = items;
        loadImageTask = new LoadImageTask(context, this);

    }

    public void setData(List<Seller> datas) {
        mValues = datas;
    }

    public void addDatas(List<Seller> datas) {
        mValues.addAll(datas);
    }

    public Seller getDataItem(int position) {
        return mValues.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_seller, parent, false);
        loadImageTask.setmViewGroup(parent);
        return new ViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        mHolder.mItem = mValues.get(position);

        String name = mHolder.mItem.getNick();
        String star = mHolder.mItem.getStar();
        mHolder.storename.setText(name);
        if (!star.equals(""))
        mHolder.star.setRating(Float.valueOf(star));
        mHolder.msales.setText("月交易：" + mHolder.mItem.getMonth() + "单");
        mHolder.storeType.setText(mHolder.mItem.getType());
        mHolder.distance.setText(mHolder.mItem.getDistance());


        if (mHolder.mItem.getAvatar() != null) {
            mHolder.icon.setTag(mHolder.mItem.getAvatar());
            mHolder.icon.setImageResource(R.drawable.empty_photo);
            loadImageTask.loadBitmaps(mHolder.icon, mHolder.mItem.getAvatar());
        }

        if (!mHolder.mItem.getEvents().equals("") ){
            StringDivide dd = new StringDivide(mHolder.mItem.getEvents());
            int count = dd.getCount();
            if (count > 0) {

                mHolder.head.setText(dd.getFirstWord(0));
                mHolder.body.setText(dd.getContent(0));
                mHolder.head.setVisibility(View.VISIBLE);
                mHolder.body.setVisibility(View.VISIBLE);
                mHolder.head2.setVisibility(View.GONE);
                mHolder.body2.setVisibility(View.GONE);
                mHolder.head3.setVisibility(View.GONE);
                mHolder.body3.setVisibility(View.GONE);
            }
            if (count > 1) {
                mHolder.head2.setText(dd.getFirstWord(1));
                mHolder.body2.setText(dd.getContent(1));
                mHolder.head2.setVisibility(View.VISIBLE);
                mHolder.body2.setVisibility(View.VISIBLE);
                mHolder.head3.setVisibility(View.GONE);
                mHolder.body3.setVisibility(View.GONE);
            }
            if (count > 2) {
                mHolder.head3.setText(dd.getFirstWord(2));
                mHolder.body3.setText(dd.getContent(2));
                mHolder.head3.setVisibility(View.VISIBLE);
                mHolder.body3.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View mView;
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

        public Seller mItem;
        private MyItemClickListener mListener;

        public ViewHolder(View view, MyItemClickListener listener) {
            super(view);
            mView = view;
            this.mListener = listener;
            view.setOnClickListener(this);
            storename = (TextView) mView.findViewById(valueViewID[0]);
            star = (RatingBar) mView.findViewById(valueViewID[1]);
            msales = (TextView) mView.findViewById(valueViewID[2]);
            storeType = (TextView) mView.findViewById(valueViewID[3]);
            icon = (ImageView) mView.findViewById(valueViewID[4]);

            head = (TextView) mView.findViewById(valueViewID[5]);
            body = (TextView) mView.findViewById(valueViewID[6]);

            head2 = (TextView) mView.findViewById(valueViewID[7]);
            body2 = (TextView) mView.findViewById(valueViewID[8]);

            head3 = (TextView) mView.findViewById(valueViewID[9]);
            body3 = (TextView) mView.findViewById(valueViewID[10]);

            distance = (TextView) mView.findViewById(valueViewID[11]);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {

                mListener.onItemClick(v, getPosition());
            }
        }
    }


}

package com.wsns.lor.seller.activity.seller;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wsns.lor.seller.R;
import com.wsns.lor.seller.activity.BaseActivity;
import com.wsns.lor.seller.activity.LoginActivity;
import com.wsns.lor.seller.activity.picture.CropImageActivity;
import com.wsns.lor.seller.application.IMDebugApplication;
import com.wsns.lor.seller.chatting.CircleImageView;
import com.wsns.lor.seller.chatting.utils.BitmapLoader;
import com.wsns.lor.seller.chatting.utils.DialogCreator;
import com.wsns.lor.seller.chatting.utils.FileHelper;
import com.wsns.lor.seller.chatting.utils.HandleResponseCode;
import com.wsns.lor.seller.chatting.utils.SharePreferenceManager;
import com.wsns.lor.seller.entity.Poi;
import com.wsns.lor.seller.entity.PoiCreate;
import com.wsns.lor.seller.entity.ServerDate;
import com.wsns.lor.seller.entity.User;
import com.wsns.lor.seller.http.HttpMethods;
import com.wsns.lor.seller.http.subscribers.ProgressSubscriber;
import com.wsns.lor.seller.http.subscribers.SubscriberOnNextListener;
import com.wsns.lor.seller.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.wsns.lor.seller.application.OnlineUserInfo.latitude;
import static com.wsns.lor.seller.application.OnlineUserInfo.longitude;

/**
 * 第一次登录，需要编辑头像（可选），店名，地址
 */
public class FixProfileActivity extends BaseActivity {

    private static final String TAG = "FixProfileActivity";
    String ak = "IwiUF0Rcfn1ckrPLeABUw4r9OwXEL6NP";

    private Button mFinishBtn;
    private EditText mNickNameEt;
    private ImageView mAvatarIv;
    private String mPath;
    private ProgressDialog mDialog;
    private Dialog mSetAvatarDialog;
    private Context mContext;
    // 裁剪后图片的宽(X)和高(Y), 720 X 720的正方形。
    private static int OUTPUT_X = 720;
    private static int OUTPUT_Y = 720;
    private Uri mUri;

    SubscriberOnNextListener createSellerOnNext;
    SubscriberOnNextListener updateAvatarOnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != savedInstanceState) {
            String nickName = savedInstanceState.getString("savedNickName");
            mNickNameEt.setText(nickName);
        }
        setContentView(R.layout.activity_fix_profile);
        mContext = this;
        mNickNameEt = (EditText) findViewById(R.id.nick_name_et);
        mAvatarIv = (CircleImageView) findViewById(R.id.jmui_avatar_iv);
        mFinishBtn = (Button) findViewById(R.id.finish_btn);
        mAvatarIv.setOnClickListener(listener);
        mFinishBtn.setOnClickListener(listener);
        JMessageClient.getUserInfo(JMessageClient.getMyInfo().getUserName(), null);
        SharePreferenceManager.setCachedFixProfileFlag(true);
        mNickNameEt.requestFocus();
        createSellerOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                ToastUtil.show(FixProfileActivity.this,"初始化完成，请重新登录");
                reLoginActivity();
            }
        };

        updateAvatarOnNext=new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                ToastUtil.show(FixProfileActivity.this,"修改成功");
            }
        };

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstancedState) {
        savedInstancedState.putString("savedNickName", mNickNameEt.getText().toString());
        super.onSaveInstanceState(savedInstancedState);
    }

    ProgressDialog dialog;
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.jmui_avatar_iv:
                    showSetAvatarDialog();
                    break;
                case R.id.finish_btn:
                    final String nickName = mNickNameEt.getText().toString().trim();
                    if (nickName != null && !nickName.equals("")) {
                        dialog = new ProgressDialog(mContext);
                        dialog.setMessage(mContext.getString(R.string.saving_hint));
                        dialog.show();
                        dialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
                        final UserInfo myUserInfo = JMessageClient.getMyInfo();
                        myUserInfo.setNickname(nickName);

                        JMessageClient.updateMyInfo(UserInfo.Field.nickname, myUserInfo, new BasicCallback() {
                            @Override
                            public void gotResult(final int status, String desc) {
                                //更新跳转标志
                                SharePreferenceManager.setCachedFixProfileFlag(false);
                                if (ak != null) {
                                    Poi poi = new Poi();
                                    ArrayList list = new ArrayList();
                                    list.add(longitude);
                                    list.add(latitude);
                                    poi.setLocation(list);
                                    poi.setAddress(myUserInfo.getAddress());
                                    poi.setTitle(nickName);


                                    HttpMethods.getInstance().createSeller(
                                            new ProgressSubscriber<PoiCreate>(createSellerOnNext, FixProfileActivity.this, false),
                                            poi

                                    );
                                }

                                if (status != 0) {
                                    Toast.makeText(mContext, mContext.getString(R.string.nickname_save_failed),
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    } else {
                        Toast.makeText(FixProfileActivity.this, FixProfileActivity.this
                                .getString(R.string.nickname_not_null_toast), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
            }
        }
    };

    public void showSetAvatarDialog() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.jmui_take_photo_btn:
                        mSetAvatarDialog.cancel();
                        takePhoto();
                        break;
                    case R.id.jmui_pick_picture_btn:
                        mSetAvatarDialog.cancel();
                        selectImageFromLocal();
                        break;
                }
            }
        };
        mSetAvatarDialog = DialogCreator.createSetAvatarDialog(this, listener);
        mSetAvatarDialog.show();
        mSetAvatarDialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void reLoginActivity() {
        Intent intent = new Intent();
        intent.setClass(FixProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void takePhoto() {
        if (FileHelper.isSdCardExist()) {
            mPath = FileHelper.createAvatarPath(JMessageClient.getMyInfo().getUserName());
            File file = new File(mPath);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(intent, IMDebugApplication.REQUEST_CODE_TAKE_PHOTO);
        } else {
            Toast.makeText(this, this.getString(R.string.jmui_sdcard_not_exist_toast), Toast.LENGTH_SHORT).show();
        }
    }

    public void selectImageFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, IMDebugApplication.REQUEST_CODE_SELECT_PICTURE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == IMDebugApplication.REQUEST_CODE_TAKE_PHOTO) {
            if (mPath != null) {
                mUri = Uri.fromFile(new File(mPath));
//                cropRawPhoto(mUri);
                Intent intent = new Intent();
                intent.putExtra("filePath", mUri.getPath());
                intent.setClass(this, CropImageActivity.class);
                startActivityForResult(intent, IMDebugApplication.REQUEST_CODE_CROP_PICTURE);
            }
        } else if (requestCode == IMDebugApplication.REQUEST_CODE_SELECT_PICTURE) {
            if (data != null) {
                Uri selectedImg = data.getData();
                if (selectedImg != null) {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.getContentResolver()
                            .query(selectedImg, filePathColumn, null, null, null);
                    try {
                        if (null == cursor) {
                            String path = selectedImg.getPath();
                            File file = new File(path);
                            if (file.isFile()) {
                                copyAndCrop(file);
                                return;
                            } else {
                                Toast.makeText(this, this.getString(R.string.picture_not_found),
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else if (!cursor.moveToFirst()) {
                            Toast.makeText(this, this.getString(R.string.picture_not_found),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String path = cursor.getString(columnIndex);
                        if (path != null) {
                            File file = new File(path);
                            if (!file.isFile()) {
                                Toast.makeText(this, this.getString(R.string.picture_not_found),
                                        Toast.LENGTH_SHORT).show();
                                cursor.close();
                            } else {
                                //如果是选择本地图片进行头像设置，复制到临时文件，并进行裁剪
                                copyAndCrop(file);
                                cursor.close();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        } else if (requestCode == IMDebugApplication.REQUEST_CODE_CROP_PICTURE) {
//            uploadUserAvatar(mUri.getPath());
            String path = data.getStringExtra("filePath");
            if (path != null) {
                uploadUserAvatar(path);
            }
        }
    }

    /**
     * 裁剪图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", OUTPUT_X);
        intent.putExtra("outputY", OUTPUT_Y);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        this.startActivityForResult(intent, IMDebugApplication.REQUEST_CODE_CROP_PICTURE);
    }

    /**
     * 复制后裁剪文件
     *
     * @param file 要复制的文件
     */
    private void copyAndCrop(final File file) {
        FileHelper.getInstance().copyFile(file, this, new FileHelper.CopyFileCallback() {
            @Override
            public void copyCallback(Uri uri) {
                mUri = uri;
//                cropRawPhoto(mUri);
                Intent intent = new Intent();
                intent.putExtra("filePath", mUri.getPath());
                intent.setClass(mContext, CropImageActivity.class);
                startActivityForResult(intent, IMDebugApplication.REQUEST_CODE_CROP_PICTURE);
            }
        });
    }

    /**
     * 上传头像
     *
     * @param path 要上传的文件路径
     */
    private void uploadUserAvatar(final String path) {
        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);
        mDialog.setMessage(this.getString(R.string.updating_avatar_hint));
        mDialog.show();
        JMessageClient.updateUserAvatar(new File(path), new BasicCallback() {
            @Override
            public void gotResult(int status, final String desc) {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                if (status == 0) {
                    Log.i(TAG, "Update avatar succeed path " + path);
                    loadUserAvatar(path);
                    Toast.makeText(FixProfileActivity.this,
                            FixProfileActivity.this.getString(R.string.avatar_modify_succeed_toast),
                            Toast.LENGTH_SHORT).show();
                    updateAvatar(path);
                    //如果头像上传失败，删除剪裁后的文件
                } else {
                    HandleResponseCode.onHandle(mContext, status, false);
                    File file = new File(path);
                    if (file.delete()) {
                        Log.d(TAG, "Upload failed, delete cropped file succeed");
                    }
                }
            }
        });
    }

    private void loadUserAvatar(String path) {
        final Bitmap bitmap = BitmapLoader.getBitmapFromFile(path, (int) (100 * mDensity),
                (int) (100 * mDensity));
        mAvatarIv.setImageBitmap(bitmap);
    }

    /**
     * 上传头像
     */
    public void updateAvatar(String path) {

        if (path != null) {
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/*"),new File(path));
            HttpMethods.getInstance().updateAvatar(new ProgressSubscriber<ServerDate<User>>(updateAvatarOnNext, this, true), imgFile);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

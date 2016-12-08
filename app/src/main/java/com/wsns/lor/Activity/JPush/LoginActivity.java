//package com.wsns.lor;
//
//import android.annotation.TargetApi;
//import android.app.ProgressDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.app.LoaderManager.LoaderCallbacks;
//
//import android.content.CursorLoader;
//import android.content.Loader;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.AsyncTask;
//import com.wsns.lor.R;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.ContactsContract;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.EditText;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 登录界面
// */
//public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
//
//
//    private static final int REQUEST_READ_CONTACTS = 0;
//
//
//    private static final String[] DUMMY_CREDENTIALS = new String[]{
//            "2:q", "3:q"
//    };
//
//    private UserLoginTask mAuthTask = null;
//
//    private AutoCompleteTextView mPhoneView;
//    private EditText mPasswordView;
//    private ProgressDialog mProgressView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        getLoaderManager().initLoader(0, null, this);
//        mPhoneView = (AutoCompleteTextView) findViewById(R.id.phone);
//
//
//        mPasswordView = (EditText) findViewById(R.id.password);
//
//
//        Button mphoneSignInButton = (Button) findViewById(R.id.phone_sign_in_button);
//        mphoneSignInButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                attemptLogin();
//            }
//        });
//
//        mProgressView = new ProgressDialog(this);
//    }
//
//
//
//    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }
//
//        // Reset errors.
//        mPhoneView.setError(null);
//        mPasswordView.setError(null);
//
//        // Store values at the time of the login attempt.
//        String phone = mPhoneView.getText().toString();
//        String password = mPasswordView.getText().toString();
//
//        boolean cancel = false;
//        View focusView = null;
//
//        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }
//
//        // Check for a valid phone address.
//        if (TextUtils.isEmpty(phone)) {
//            mPhoneView.setError(getString(R.string.error_field_required));
//            focusView = mPhoneView;
//            cancel = true;
//        } else if (!isphoneValid(phone)) {
//            mPhoneView.setError(getString(R.string.error_invalid_phone));
//            focusView = mPhoneView;
//            cancel = true;
//        }
//
//        if (cancel) {
//            // There was an error; don't attempt login and focus the first
//            // form field with an error.
//            focusView.requestFocus();
//        } else {
//            // Show a progress spinner, and kick off a background task to
//            // perform the user login attempt.
//            showProgress(true);
//            mAuthTask = new UserLoginTask(phone, password);
//            mAuthTask.execute((Void) null);
//        }
//    }
//    //正则表达式该字符串是否纯数字
//    private boolean isphoneValid(String phone) {
//        return phone.matches("[0-9]+");
//    }
//
//    private boolean isPasswordValid(String password) {
//        return password.length() > 0;
//    }
//
//
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    private void showProgress(final boolean show) {
//            mProgressView.setMessage("正在登录...");
//            mProgressView.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            if(show )
//                mProgressView.show();
//            else
//                mProgressView.dismiss();
//
//    }
//
//    @Override
//    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//        return new CursorLoader(this,
//                // Retrieve data rows for the device user's 'profile' contact.
//                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
//                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
//
//                // Select only phone .
//                ContactsContract.Contacts.Data.MIMETYPE +
//                        " = ?", new String[]{ContactsContract.CommonDataKinds.Phone
//                .CONTENT_ITEM_TYPE},
//
//                // Show primary phone addresses first. Note that there won't be
//                // a primary phone address if the user hasn't specified one.
//                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
//        List<String> phones = new ArrayList<>();
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            phones.add(cursor.getString(ProfileQuery.NUMBER));
//            cursor.moveToNext();
//        }
//
//        addphonesToAutoComplete(phones);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> cursorLoader) {
//
//    }
//
//    private void addphonesToAutoComplete(List<String> phoneAddressCollection) {
//        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<>(LoginActivity.this,
//                        android.R.layout.simple_dropdown_item_1line, phoneAddressCollection);
//
//        mPhoneView.setAdapter(adapter);
//    }
//
//
//    private interface ProfileQuery {
//        String[] PROJECTION = {
//                ContactsContract.CommonDataKinds.Phone.NUMBER,
//                ContactsContract.CommonDataKinds.Phone.IS_PRIMARY,
//        };
//
//        int NUMBER = 0;
//        int IS_PRIMARY = 1;
//    }
//
//    /**
//     * 异步处理登录网络请求
//     */
//    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
//
//        private final String mphone;
//        private final String mPassword;
//
//        UserLoginTask(String phone, String password) {
//            mphone = phone;
//            mPassword = password;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//
//            try {
//                // Simulate network access.
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }
//
//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mphone)) {
//                    return pieces[1].equals(mPassword);
//                }
//            }
//
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success) {
//
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }
//}
//

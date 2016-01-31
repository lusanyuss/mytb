package com.mytb;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.button)
    Button button;
    private String mChosenAccountName = "";
    public static final String ACCOUNT_KEY = "accountName";
    GoogleAccountCredential credential;

    private static final String REQUEST_AUTHORIZATION_INTENT = "com.google.example.yt.RequestAuth";
    private static final String REQUEST_AUTHORIZATION_INTENT_PARAM = "com.google.example.yt.RequestAuth.param";
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
    private static final int REQUEST_GMS_ERROR_DIALOG = 1;
    private static final int REQUEST_ACCOUNT_PICKER = 2;
    private static final int REQUEST_AUTHORIZATION = 3;
    private static final int RESULT_PICK_IMAGE_CROP = 4;
    private static final int RESULT_VIDEO_CAP = 5;
    private static final int REQUEST_DIRECT_TAG = 6;
//    private UploadBroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


//        credential = GoogleAccountCredential.usingOAuth2(
//                getApplicationContext(), Arrays.asList(YoutubeUtil.SCOPES));
//        // set exponential backoff policy
//        credential.setBackOff(new ExponentialBackOff());
//
//        if (savedInstanceState != null) {
//            mChosenAccountName = savedInstanceState.getString(ACCOUNT_KEY);
//        } else {
//            loadAccount();
//        }
//        credential.setSelectedAccountName(mChosenAccountName);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            YoutubeUtil.search(MainActivity.this, "music");
                            YoutubeUtil.searchChannels(MainActivity.this, "UC0DCw3l8UnWIGQO7eCGONIQ");
                            YoutubeUtil.searchVideoList(MainActivity.this, "PLFgquLnL59amAlpdLTWJTX6BIPunJ8s-A");
//                    YoutubeUtil.searchVideoLists(MainActivity.this, "");
                        }

//                        catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
//                            showGooglePlayServicesAvailabilityErrorDialog(availabilityException
//                                    .getConnectionStatusCode());
//                        } catch (UserRecoverableAuthIOException userRecoverableException) {
//                            startActivityForResult(
//                                    userRecoverableException.getIntent(),
//                                    REQUEST_AUTHORIZATION);
//                        } catch (IOException e) {
//                        }


                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute();
            }
        });
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
////        if (broadcastReceiver == null)
////            broadcastReceiver = new UploadBroadcastReceiver();
////        IntentFilter intentFilter = new IntentFilter(
////                REQUEST_AUTHORIZATION_INTENT);
////        LocalBroadcastManager.getInstance(this).registerReceiver(
////                broadcastReceiver, intentFilter);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (broadcastReceiver != null) {
//            LocalBroadcastManager.getInstance(this).unregisterReceiver(
//                    broadcastReceiver);
//        }
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_refresh:
//                break;
//            case R.id.menu_accounts:
//                chooseAccount();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    private void chooseAccount() {
//        startActivityForResult(credential.newChooseAccountIntent(),
//                REQUEST_ACCOUNT_PICKER);
//    }
//
//    private void loadAccount() {
//        SharedPreferences sp = PreferenceManager
//                .getDefaultSharedPreferences(this);
//        mChosenAccountName = sp.getString(ACCOUNT_KEY, null);
//        invalidateOptionsMenu();
//    }
//
//    private void saveAccount() {
//        SharedPreferences sp = PreferenceManager
//                .getDefaultSharedPreferences(this);
//        sp.edit().putString(ACCOUNT_KEY, mChosenAccountName).commit();
//    }
//
//    public void showGooglePlayServicesAvailabilityErrorDialog(
//            final int connectionStatusCode) {
//        runOnUiThread(new Runnable() {
//            public void run() {
//                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
//                        connectionStatusCode, MainActivity.this,
//                        REQUEST_GOOGLE_PLAY_SERVICES);
//                dialog.show();
//            }
//        });
//    }
//
//
//    private boolean checkGooglePlayServicesAvailable() {
//        final int connectionStatusCode = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(this);
//        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
//            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
//            return false;
//        }
//        return true;
//    }
//
//    private void haveGooglePlayServices() {
//        // check if there is already an account selected
//        if (credential.getSelectedAccountName() == null) {
//            // ask user to choose account
//            chooseAccount();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case REQUEST_GMS_ERROR_DIALOG:
//                break;
//
//            case REQUEST_GOOGLE_PLAY_SERVICES:
//                if (resultCode == Activity.RESULT_OK) {
//                    haveGooglePlayServices();
//                } else {
//                    checkGooglePlayServicesAvailable();
//                }
//                break;
//            case REQUEST_AUTHORIZATION:
//                if (resultCode != Activity.RESULT_OK) {
//                    chooseAccount();
//                }
//                break;
//            case REQUEST_ACCOUNT_PICKER:
//                if (resultCode == Activity.RESULT_OK && data != null
//                        && data.getExtras() != null) {
//                    String accountName = data.getExtras().getString(
//                            AccountManager.KEY_ACCOUNT_NAME);
//                    if (accountName != null) {
//                        mChosenAccountName = accountName;
//                        credential.setSelectedAccountName(mChosenAccountName);
//                        saveAccount();
//                    }
//                }
//                break;
//        }
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString(ACCOUNT_KEY, mChosenAccountName);
//    }
//
//
//    private class UploadBroadcastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(REQUEST_AUTHORIZATION_INTENT)) {
//                Intent toRun = intent
//                        .getParcelableExtra(REQUEST_AUTHORIZATION_INTENT_PARAM);
//                startActivityForResult(toRun, REQUEST_AUTHORIZATION);
//            }
//        }
//    }
}

package org.mujeeb.mosquemanager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import org.mujeeb.mosquemanager.api.APICallCallback;
import org.mujeeb.mosquemanager.api.ApiUtil;
import org.mujeeb.mosquemanager.beans.request.BaseRequestBean;
import org.mujeeb.mosquemanager.beans.request.NamazTimeUpdateRequestBean;
import org.mujeeb.mosquemanager.beans.request.UpdateRefreshRequiredBean;
import org.mujeeb.mosquemanager.beans.response.BaseResponseBean;
import org.mujeeb.mosquemanager.util.Constants;
import org.mujeeb.mosquemanager.util.JsonUtil;
import org.mujeeb.mosquemanager.util.UIUtil;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements APICallCallback {

    protected String returnBackURL = "http://alithistech.com";

    protected Resources resources;
    protected Menu menu;

    protected String userId = "1";
    protected String password = null;

    protected WebView webView;

    protected String isScreenSaverEnabled = "false";

    protected Button btnScreenSaver;

    @Override
    public void onExecutionComplete(String result, String apiEndpoint) {

        if(apiEndpoint.equalsIgnoreCase(ApiUtil.API_ENDPOINT_UPDATE_REFRESH_REQUIRED)) {

            BaseResponseBean response = (BaseResponseBean) JsonUtil.objectFromJson(result, BaseResponseBean.class);
            if(response.getResultCode() != 0) {

                UIUtil.showToast(HomeActivity.this, response.getDescription(), Toast.LENGTH_LONG);
                return;
            }

            UIUtil.showToast(HomeActivity.this
                    , resources.getString(R.string.message_refresh_clock_successfully)
                    , Toast.LENGTH_LONG);
        } else if(apiEndpoint.equalsIgnoreCase(ApiUtil.API_ENDPOINT_GET_NAMAZ_TIMES)) {

            Map<String,String> namazTimes = (Map<String,String>) JsonUtil.objectFromJson(result, Map.class);
            isScreenSaverEnabled = namazTimes.get(Constants.KEY_SCREEN_SAVER_ENABLED);

            updateScreenSaverButton();

        } else if(apiEndpoint.equalsIgnoreCase(ApiUtil.API_ENDPOINT_UPDATE_NAMAZ_TIME)) {

            BaseResponseBean response = (BaseResponseBean) JsonUtil.objectFromJson(result, BaseResponseBean.class);
            if(response.getResultCode() != 0) {

                UIUtil.showToast(HomeActivity.this, response.getDescription(), Toast.LENGTH_LONG);
                return;
            }

            if(isScreenSaverEnabled == null || isScreenSaverEnabled.isEmpty() || isScreenSaverEnabled.equals("false")) {

                UIUtil.showToast(HomeActivity.this
                        , resources.getString(R.string.message_screen_saver_enable_successful)
                        , Toast.LENGTH_LONG);
            } else {

                UIUtil.showToast(HomeActivity.this
                        , resources.getString(R.string.message_screen_saver_disable_successful)
                        , Toast.LENGTH_LONG);
            }
            makeGetNamazTimesAPICall();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resources = getResources();

        String apiHost = resources.getString(R.string.api_host);
//        UIUtil.showToast(HomeActivity.this, "API Host: " + apiHost, Toast.LENGTH_LONG);
        ApiUtil.setApiHost(apiHost);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UIUtil.goToUrl(HomeActivity.this, resources.getString(R.string.contactus_email_link));
            }
        });

        Button btnUpdateHijriAdjustment = findViewById(R.id.btnUpdateHijriAdjustment);
        Button btnUpdateNamazTimes = findViewById(R.id.btnUpdateNamazTimes);
        Button btnUpdateOccasions = findViewById(R.id.btnUpdateOccasions);
        Button btnRefreshClock = findViewById(R.id.btnRefreshClock);
        Button btnUpdateScreenSaverSchedule = findViewById(R.id.btnUpdateScreenSaverSchedule);
        btnScreenSaver = findViewById(R.id.btnScreenSaver);

        btnUpdateHijriAdjustment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
                    UIUtil.openActivity(HomeActivity.this, LoginActivity.class);
                } else {
                    UIUtil.openActivity(HomeActivity.this, UpdateHijriAdjustmentActivity.class);
                }
            }
        });

        btnUpdateNamazTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
                    UIUtil.openActivity(HomeActivity.this, LoginActivity.class);
                } else {
                    UIUtil.openActivity(HomeActivity.this, UpdateNamazTimesActivity.class);
                }
            }
        });

        btnUpdateOccasions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
                    UIUtil.openActivity(HomeActivity.this, LoginActivity.class);
                } else {
                    UIUtil.openActivity(HomeActivity.this, UpdateOccasionsActivity.class);
                }
            }
        });

        btnRefreshClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
                    UIUtil.openActivity(HomeActivity.this, LoginActivity.class);
                } else {

                    submitClockRefreshWithConfirmation();
                }
            }
        });

        btnScreenSaver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
                    UIUtil.openActivity(HomeActivity.this, LoginActivity.class);
                } else {

                    submitScreenSaverActionWithConfirmation();
                }
            }
        });

        btnUpdateScreenSaverSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
                    UIUtil.openActivity(HomeActivity.this, LoginActivity.class);
                } else {
                    UIUtil.openActivity(HomeActivity.this, UpdateScreenSaverScheduleActivity.class);
                }
            }
        });

        makeGetNamazTimesAPICall();
    }

    @Override
    public void onPostResume() {

        super.onPostResume();

        // Get the Auth Credentials
        Map<String,String> credentials = UIUtil.getAuthCredentials(HomeActivity.this);
        if(credentials != null) {
            this.userId = credentials.keySet().iterator().next();
            this.password = credentials.get(userId);
        }

        UIUtil.setLoginLogoutMenuTitle(HomeActivity.this, password, menu);

        webView = findViewById(R.id.webView);
        String url = getURL();

        webView.setInitialScale(100);
        webView.loadUrl(url);
        webView.setWebViewClient(new MyWebViewClient());

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        UIUtil.setLoginLogoutMenuTitle(HomeActivity.this, password, menu);

        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_login_logout) {

            if(userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
                UIUtil.openActivity(HomeActivity.this, LoginActivity.class);
            } else {

                password = null;
                UIUtil.storeAuthCredentials(HomeActivity.this, userId, password);
                UIUtil.setLoginLogoutMenuTitle(HomeActivity.this, password, menu);
                UIUtil.showAlert(HomeActivity.this, resources.getString(R.string.message_title_success), resources.getString(R.string.message_logout_successful));
            }

        } else if(id == R.id.action_about) {

            UIUtil.openActivity(HomeActivity.this, AboutActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    protected void makeGetNamazTimesAPICall() {

        try {
            Map<String,String> parameters = new HashMap<>();
            parameters.put("id", userId);
            UIUtil.makeAPICall(HomeActivity.this, ApiUtil.API_ENDPOINT_GET_NAMAZ_TIMES, parameters);

        } catch(Throwable ex) {

            UIUtil.showToast(HomeActivity.this, resources.getString(R.string.error_unknown)
                    , Toast.LENGTH_LONG);
        }
    }

    private void performScreenSaverAction() {

        String action;

        if(isScreenSaverEnabled == null || isScreenSaverEnabled.isEmpty() || isScreenSaverEnabled.equals("false")) {

            action = "true";
        } else {

            action = "false";
        }

        makeUpdateNamazTimeCall(action);
    }

    protected void makeUpdateNamazTimeCall(String value) {

        try {
            NamazTimeUpdateRequestBean request = new NamazTimeUpdateRequestBean(Constants.KEY_SCREEN_SAVER_ENABLED, value);
            request.setUserId(userId);
            request.setPassword(password);
            UIUtil.makeAPICall(HomeActivity.this, ApiUtil.API_ENDPOINT_UPDATE_NAMAZ_TIME
                    , JsonUtil.jsonFromObject(request));

        } catch(Throwable ex) {

            UIUtil.showToast(HomeActivity.this, resources.getString(R.string.error_unknown)
                    , Toast.LENGTH_LONG);
        }
    }

    protected void submitClockRefreshWithConfirmation() {

        UIUtil.showConfirmation(HomeActivity.this, resources.getString(R.string.message_title_confirm)
                , resources.getString(R.string.message_question_confirm_clock_refresh)
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        submitClockRefreshRequest();
                    }
                });
    }

    protected void submitScreenSaverActionWithConfirmation() {

        UIUtil.showConfirmation(HomeActivity.this, resources.getString(R.string.message_title_confirm)
                , resources.getString(R.string.message_question_confirm_screen_saver_action)
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        performScreenSaverAction();
                    }
                });
    }

    private void updateScreenSaverButton() {

        if(isScreenSaverEnabled == null || isScreenSaverEnabled.isEmpty() || isScreenSaverEnabled.equals("false")) {

            btnScreenSaver.setText(resources.getString(R.string.button_enable_screen_saver));
        } else {

            btnScreenSaver.setText(resources.getString(R.string.button_disable_screen_saver));
        }
    }

    private void submitClockRefreshRequest() {

        try {
            BaseRequestBean request = new UpdateRefreshRequiredBean(userId, password, "true");
            UIUtil.makeAPICall(HomeActivity.this, ApiUtil.API_ENDPOINT_UPDATE_REFRESH_REQUIRED
                                                    , JsonUtil.jsonFromObject(request));

        } catch(Throwable ex) {

            UIUtil.showToast(HomeActivity.this, resources.getString(R.string.error_unknown)
                    , Toast.LENGTH_LONG);
        }
    }

    protected String getURL() {

        return "https://yellow-pebble-09aa20800.azurestaticapps.net/index"
                + (userId.equals("1") || userId.equals("0") ? "" : userId)
                + "_static.html";
//        return "http://" + resources.getString(R.string.api_host) + "?id=" + userId + "&REFRESHES=false";
    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

//            UIUtil.showToast(OnlinePaymentActivity.this, url, Toast.LENGTH_LONG);

            if(returnBackURL.equalsIgnoreCase(url)) {

                Intent returnIntent = new Intent();
//                returnIntent.putExtra(Constants.KEY_PAYLOAD, "ONLINE_PAYMENT");
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }

            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}

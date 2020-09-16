package org.mujeeb.mosquemanager;

import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.mujeeb.mosquemanager.api.APICallCallback;
import org.mujeeb.mosquemanager.api.ApiUtil;
import org.mujeeb.mosquemanager.beans.request.BaseRequestBean;
import org.mujeeb.mosquemanager.beans.response.BaseResponseBean;
import org.mujeeb.mosquemanager.util.JsonUtil;
import org.mujeeb.mosquemanager.util.UIUtil;

import java.util.Map;

public class LoginActivity extends AppCompatActivity implements APICallCallback {

    protected String userId;
    protected String password;

    protected Resources resources;

    @Override
    public void onExecutionComplete(String result, String apiEndPoint) {

        if (apiEndPoint.equalsIgnoreCase(ApiUtil.API_ENDPOINT_LOGIN)) {

            BaseResponseBean response = null;
            try {
                response = (BaseResponseBean) JsonUtil.objectFromJson(result, BaseResponseBean.class);

            } catch(Throwable ex) {

                UIUtil.showToast(LoginActivity.this, resources.getString(R.string.message_login_failed), Toast.LENGTH_LONG);
                return;
            }

            if(response == null) {
                return;
            }

            if(response.getResultCode() != 0)  {
                UIUtil.showAlert(LoginActivity.this, response.getDescription());
                return;
            }

            UIUtil.storeAuthCredentials(LoginActivity.this, userId, password);
            UIUtil.showToast(LoginActivity.this, resources.getString(R.string.message_login_successful), Toast.LENGTH_LONG);
            LoginActivity.this.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        resources = getResources();

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        final EditText editUserId = (EditText) findViewById(R.id.editUserId);
        final EditText editPassword = (EditText) findViewById(R.id.editPassword);

        // Get the Auth Credentials
        Map<String,String> credentials = UIUtil.getAuthCredentials(LoginActivity.this);
        if(credentials != null) {
            this.userId = credentials.keySet().iterator().next();
        }
        if(userId != null && !userId.isEmpty()) {
            editUserId.setText(userId);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userId = editUserId.getText().toString();
                password = editPassword.getText().toString();

                if(userId == null || userId.isEmpty()) {

                    UIUtil.showAlert(LoginActivity.this, resources.getString(R.string.warning_invalid_user_id));
                    editUserId.requestFocus();
                    return;

                } else if(password == null || password.isEmpty()) {

                    UIUtil.showAlert(LoginActivity.this, resources.getString(R.string.warning_invalid_password));
                    editPassword.requestFocus();
                    return;
                }

                BaseRequestBean request = new BaseRequestBean(userId, password);
                try {
                    UIUtil.makeAPICall(LoginActivity.this, ApiUtil.API_ENDPOINT_LOGIN, JsonUtil.jsonFromObject(request));

                }catch (Exception ex) {
                    ex.printStackTrace();
                    UIUtil.showToast(LoginActivity.this, resources.getString(R.string.error_unknown), Toast.LENGTH_LONG);
                }
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        if(item.getItemId() == R.id.action_home) {
//            UIUtil.openActivity(LoginActivity.this, HomeActivity.class);
//        }

        return super.onOptionsItemSelected(item);
    }
}

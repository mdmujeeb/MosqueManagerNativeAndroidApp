package org.mujeeb.mosquemanager;

import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.mujeeb.mosquemanager.api.APICallCallback;
import org.mujeeb.mosquemanager.api.ApiUtil;
import org.mujeeb.mosquemanager.beans.request.NamazTimeUpdateRequestBean;
import org.mujeeb.mosquemanager.beans.response.BaseResponseBean;
import org.mujeeb.mosquemanager.util.JsonUtil;
import org.mujeeb.mosquemanager.util.UIUtil;

import java.util.HashMap;
import java.util.Map;

public class UpdateHijriAdjustmentActivity extends AppCompatActivity implements APICallCallback {

    public static String KEY_HIJRI_ADJUSTMENT = "HIJRI_ADJUSTMENT";

    protected String userId = null;
    protected String password = null;

    protected Resources resources;

    protected EditText editHijriAdjustment;
    protected Button btnUpdate;
    protected Button btnCancel;

    protected Map<String,String> namazTimes;


    @Override
    public void onExecutionComplete(String result, String apiEndpoint) {

        if(apiEndpoint.equalsIgnoreCase(ApiUtil.API_ENDPOINT_GET_NAMAZ_TIMES)) {

            namazTimes = (Map<String,String>) JsonUtil.objectFromJson(result, Map.class);
            updateView();

        } else if(apiEndpoint.equalsIgnoreCase(ApiUtil.API_ENDPOINT_UPDATE_NAMAZ_TIME)) {

            BaseResponseBean response = (BaseResponseBean) JsonUtil.objectFromJson(result, BaseResponseBean.class);
            if(response.getResultCode() != 0) {

                UIUtil.showToast(UpdateHijriAdjustmentActivity.this, response.getDescription(), Toast.LENGTH_LONG);
                return;
            }

            UIUtil.showToast(UpdateHijriAdjustmentActivity.this
                                    , resources.getString(R.string.message_hijri_adjustment_update_successful)
                                    , Toast.LENGTH_LONG);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hijri_adjustment);

        setTitle(R.string.title_activity_update_hijri_adjustment);

        resources = getResources();
        editHijriAdjustment = (EditText) findViewById(R.id.editHijriAdjustment);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strAdjustment = editHijriAdjustment.getText().toString().trim();

                int adjustment = -1;

                try {

                    adjustment = Integer.parseInt(strAdjustment);
                    if(adjustment < -30 || adjustment > 30) {
                        throw new Exception();
                    }

                }catch (Throwable ex) {

                    UIUtil.showToast(UpdateHijriAdjustmentActivity.this
                            , resources.getString(R.string.warning_invalid_hijri_adjustment)
                            , Toast.LENGTH_LONG);
                    return;
                }

                makeUpdateHijriAdjustmentCall(strAdjustment);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        // Get the Auth Credentials
        Map<String,String> credentials = UIUtil.getAuthCredentials(UpdateHijriAdjustmentActivity.this);
        if(credentials != null) {
            this.userId = credentials.keySet().iterator().next();
            this.password = credentials.get(userId);
        }

        makeGetHijriAdjustmentAPICall();
    }

    protected void makeGetHijriAdjustmentAPICall() {

        try {
            Map<String,String> parameters = new HashMap<String,String>();
            parameters.put("id", userId);
            UIUtil.makeAPICall(UpdateHijriAdjustmentActivity.this, ApiUtil.API_ENDPOINT_GET_NAMAZ_TIMES, parameters);

        } catch(Throwable ex) {

            UIUtil.showToast(UpdateHijriAdjustmentActivity.this, resources.getString(R.string.error_unknown)
                    , Toast.LENGTH_LONG);
        }
    }

    protected void updateView() {

        if(namazTimes == null) {
            return;
        }

        String hijriAdjustment = namazTimes.get(KEY_HIJRI_ADJUSTMENT);
        editHijriAdjustment.setText(hijriAdjustment);
    }

    protected void makeUpdateHijriAdjustmentCall(String hijriAdjustment) {

        try {
            NamazTimeUpdateRequestBean request = new NamazTimeUpdateRequestBean(KEY_HIJRI_ADJUSTMENT, hijriAdjustment);
            request.setUserId(userId);
            request.setPassword(password);
            UIUtil.makeAPICall(UpdateHijriAdjustmentActivity.this, ApiUtil.API_ENDPOINT_UPDATE_NAMAZ_TIME
                                                            , JsonUtil.jsonFromObject(request));

        } catch(Throwable ex) {

            UIUtil.showToast(UpdateHijriAdjustmentActivity.this, resources.getString(R.string.error_unknown)
                    , Toast.LENGTH_LONG);
        }

    }
}

package org.mujeeb.mosquemanager;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.mujeeb.mosquemanager.api.APICallCallback;
import org.mujeeb.mosquemanager.api.ApiUtil;
import org.mujeeb.mosquemanager.beans.request.NamazTimeUpdateRequestBean;
import org.mujeeb.mosquemanager.beans.response.BaseResponseBean;
import org.mujeeb.mosquemanager.util.Constants;
import org.mujeeb.mosquemanager.util.JsonUtil;
import org.mujeeb.mosquemanager.util.UIUtil;

import java.util.HashMap;
import java.util.Map;

public class UpdateScreenSaverScheduleActivity extends AppCompatActivity implements APICallCallback {

    protected String userId = null;
    protected String password = null;

    protected Resources resources;

    protected EditText editStartHours;
    protected EditText editStartMinutes;
    protected EditText editEndHours;
    protected EditText editEndMinutes;
    protected Button btnUpdate;
    protected Button btnCancel;

    protected String currentNamazTimeName = null;
    protected String currentNamazTime = null;

    protected String screenSaverSchedule;


    @Override
    public void onExecutionComplete(String result, String apiEndpoint) {

        if(apiEndpoint.equalsIgnoreCase(ApiUtil.API_ENDPOINT_GET_NAMAZ_TIMES)) {

            Map<String,String> namazTimes = (Map<String,String>) JsonUtil.objectFromJson(result, Map.class);
            screenSaverSchedule = namazTimes.get(Constants.KEY_SCREEN_SAVER_SCHEDULE);
            updateScheduleData();

        } else if(apiEndpoint.equalsIgnoreCase(ApiUtil.API_ENDPOINT_UPDATE_NAMAZ_TIMES)) {

            BaseResponseBean response = (BaseResponseBean) JsonUtil.objectFromJson(result, BaseResponseBean.class);
            if(response.getResultCode() != 0) {

                UIUtil.showToast(UpdateScreenSaverScheduleActivity.this, response.getDescription(), Toast.LENGTH_LONG);
                return;
            }

            UIUtil.showToast(UpdateScreenSaverScheduleActivity.this
                                    , resources.getString(R.string.message_screen_saver_schedule_update_successful)
                                    , Toast.LENGTH_LONG);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_screen_saver_schedule);

        setTitle(R.string.button_update_screen_saver_schedule);

        resources = getResources();
        editStartHours = (EditText) findViewById(R.id.editStartHours);
        editStartMinutes = (EditText) findViewById(R.id.editStartMinutes);
        editEndHours = (EditText) findViewById(R.id.editEndHours);
        editEndMinutes = (EditText) findViewById(R.id.editEndMinutes);
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

                String strStartHours = editStartHours.getText().toString().trim();
                String strStartMinutes = editStartMinutes.getText().toString().trim();
                String strEndHours = editEndHours.getText().toString().trim();
                String strEndMinutes = editEndMinutes.getText().toString().trim();

                int startHours = -1;
                int startMinutes = -1;
                try {

                    startHours = Integer.parseInt(strStartHours);
                    if(startHours < 0 || startHours > 23) {
                        throw new Exception();
                    }

                }catch (Throwable ex) {

                    UIUtil.showToast(UpdateScreenSaverScheduleActivity.this
                                        , resources.getString(R.string.warning_invalid_namaz_hours).replace("{1}", "Start time")
                                        , Toast.LENGTH_LONG);
                    return;
                }

                try {

                    startMinutes = Integer.parseInt(strStartMinutes);
                    if(startMinutes < 0 || startMinutes > 59) {
                        throw new Exception();
                    }

                }catch (Throwable ex) {

                    UIUtil.showToast(UpdateScreenSaverScheduleActivity.this
                            , resources.getString(R.string.warning_invalid_namaz_minutes).replace("{1}", "Start time")
                            , Toast.LENGTH_LONG);
                    return;
                }

                int endHours = -1;
                int endMinutes = -1;
                try {

                    endHours = Integer.parseInt(strEndHours);
                    if(endHours < 0 || endHours > 23) {
                        throw new Exception();
                    }

                }catch (Throwable ex) {

                    UIUtil.showToast(UpdateScreenSaverScheduleActivity.this
                            , resources.getString(R.string.warning_invalid_namaz_hours).replace("{1}", "End time")
                            , Toast.LENGTH_LONG);
                    return;
                }

                try {

                    endMinutes = Integer.parseInt(strEndMinutes);
                    if(endMinutes < 0 || endMinutes > 59) {
                        throw new Exception();
                    }

                }catch (Throwable ex) {

                    UIUtil.showToast(UpdateScreenSaverScheduleActivity.this
                            , resources.getString(R.string.warning_invalid_namaz_minutes).replace("{1}", "End time")
                            , Toast.LENGTH_LONG);
                    return;
                }

                currentNamazTimeName = Constants.KEY_SCREEN_SAVER_SCHEDULE;
                currentNamazTime = strStartHours + ":" + strStartMinutes + "," + strEndHours + ":" + strEndMinutes;
                makeUpdateScreenSaverScheduleCall();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        // Get the Auth Credentials
        Map<String,String> credentials = UIUtil.getAuthCredentials(UpdateScreenSaverScheduleActivity.this);
        if(credentials != null) {
            this.userId = credentials.keySet().iterator().next();
            this.password = credentials.get(userId);
        }

        makeGetNamazTimesAPICall();
    }

    protected void makeGetNamazTimesAPICall() {

        try {
            Map<String,String> parameters = new HashMap<String,String>();
            parameters.put("id", userId);
            UIUtil.makeAPICall(UpdateScreenSaverScheduleActivity.this, ApiUtil.API_ENDPOINT_GET_NAMAZ_TIMES, parameters);

        } catch(Throwable ex) {

            UIUtil.showToast(UpdateScreenSaverScheduleActivity.this, resources.getString(R.string.error_unknown)
                    , Toast.LENGTH_LONG);
        }
    }

    protected void updateScheduleData() {

        if(screenSaverSchedule == null) {
            return;
        }

        String[] schedule = screenSaverSchedule.split(",");
        String[] startTime = schedule[0].split(":");
        String[] endTime = schedule[1].split(":");
        editStartHours.setText(startTime[0]);
        editStartMinutes.setText(startTime[1]);
        editEndHours.setText(endTime[0]);
        editEndMinutes.setText(endTime[1]);
    }

    protected void makeUpdateScreenSaverScheduleCall() {

        try {
            NamazTimeUpdateRequestBean request = new NamazTimeUpdateRequestBean(currentNamazTimeName, currentNamazTime);
            request.setUserId(userId);
            request.setPassword(password);
            UIUtil.makeAPICall(UpdateScreenSaverScheduleActivity.this, ApiUtil.API_ENDPOINT_UPDATE_NAMAZ_TIMES
                                                            , JsonUtil.jsonFromObject(request));

        } catch(Throwable ex) {

            UIUtil.showToast(UpdateScreenSaverScheduleActivity.this, resources.getString(R.string.error_unknown)
                    , Toast.LENGTH_LONG);
        }

    }
}

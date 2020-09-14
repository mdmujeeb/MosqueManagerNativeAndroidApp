package org.mujeeb.mosquemanager;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.mujeeb.mosquemanager.api.APICallCallback;
import org.mujeeb.mosquemanager.api.ApiUtil;
import org.mujeeb.mosquemanager.beans.request.BaseRequestBean;
import org.mujeeb.mosquemanager.beans.request.NamazTimeUpdateRequestBean;
import org.mujeeb.mosquemanager.beans.response.BaseResponseBean;
import org.mujeeb.mosquemanager.util.JsonUtil;
import org.mujeeb.mosquemanager.util.UIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateNamazTimesActivity extends AppCompatActivity implements APICallCallback {

    protected String userId = null;
    protected String password = null;

    protected Resources resources;

    protected Spinner spnNamazTimeName;
    protected EditText editHours;
    protected EditText editMinutes;
    protected Button btnUpdate;
    protected Button btnCancel;

    protected String currentNamazTimeName = null;
    protected String currentNamazTime = null;

    protected Map<String,String> namazTimes;


    @Override
    public void onExecutionComplete(String result, String apiEndpoint) {

        if(apiEndpoint.equalsIgnoreCase(ApiUtil.API_ENDPOINT_GET_NAMAZ_TIMES)) {

            namazTimes = (Map<String,String>) JsonUtil.objectFromJson(result, Map.class);
            updateNamazTimeView();

        } else if(apiEndpoint.equalsIgnoreCase(ApiUtil.API_ENDPOINT_UPDATE_NAMAZ_TIMES)) {

            BaseResponseBean response = (BaseResponseBean) JsonUtil.objectFromJson(result, BaseResponseBean.class);
            if(response.getResultCode() != 0) {

                UIUtil.showToast(UpdateNamazTimesActivity.this, response.getDescription(), Toast.LENGTH_LONG);
                return;
            }

            UIUtil.showToast(UpdateNamazTimesActivity.this
                                    , resources.getString(R.string.message_namaz_time_update_successful)
                                    , Toast.LENGTH_LONG);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_namaz_times);

        setTitle(R.string.title_activity_update_namaz_times);

        resources = getResources();
        spnNamazTimeName = (Spinner) findViewById(R.id.spnNamazTimeName);
        editHours = (EditText) findViewById(R.id.editHours);
        editMinutes = (EditText) findViewById(R.id.editMinutes);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        // Set up fuel type Spinner
        List<String> namazTimes = new ArrayList<>();
        namazTimes.add("FAJR");
        namazTimes.add("ZOHOR");
        namazTimes.add("ASR");
        namazTimes.add("ISHA");
        namazTimes.add("JUMA");
        ArrayAdapter<String> dataAdapterNamazTimes = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, namazTimes) {};

        dataAdapterNamazTimes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNamazTimeName.setAdapter(dataAdapterNamazTimes);

        spnNamazTimeName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                makeGetNamazTimesAPICall();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strHours = editHours.getText().toString().trim();
                String strMinutes = editMinutes.getText().toString().trim();

                int hours = -1;
                int minutes = -1;
                try {

                    hours = Integer.parseInt(strHours);
                    if(hours < 1 || hours > 12) {
                        throw new Exception();
                    }

                }catch (Throwable ex) {

                    UIUtil.showToast(UpdateNamazTimesActivity.this
                                        , resources.getString(R.string.warning_invalid_namaz_hours)
                                        , Toast.LENGTH_LONG);
                    return;
                }

                try {

                    minutes = Integer.parseInt(strMinutes);
                    if(minutes < 0 || minutes > 59) {
                        throw new Exception();
                    }

                }catch (Throwable ex) {

                    UIUtil.showToast(UpdateNamazTimesActivity.this
                            , resources.getString(R.string.warning_invalid_namaz_minutes)
                            , Toast.LENGTH_LONG);
                    return;
                }

                currentNamazTimeName = (String) spnNamazTimeName.getSelectedItem();
                currentNamazTime = strHours + ":" + strMinutes;
                makeUpdateNamazTimeCall();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        // Get the Auth Credentials
        Map<String,String> credentials = UIUtil.getAuthCredentials(UpdateNamazTimesActivity.this);
        if(credentials != null) {
            this.userId = credentials.keySet().iterator().next();
            this.password = credentials.get(userId);
        }

        spnNamazTimeName.setSelection(0);
//        makeGetNamazTimesAPICall();
    }

    protected void makeGetNamazTimesAPICall() {

        try {
            Map<String,String> parameters = new HashMap<String,String>();
            parameters.put("id", userId);
            UIUtil.makeAPICall(UpdateNamazTimesActivity.this, ApiUtil.API_ENDPOINT_GET_NAMAZ_TIMES, parameters);

        } catch(Throwable ex) {

            UIUtil.showToast(UpdateNamazTimesActivity.this, resources.getString(R.string.error_unknown)
                    , Toast.LENGTH_LONG);
        }
    }

    protected void updateNamazTimeView() {

        if(namazTimes == null) {
            return;
        }

        String timeName = (String) spnNamazTimeName.getSelectedItem();
        String[] time = namazTimes.get(timeName).split(":");
        editHours.setText(time[0]);
        editMinutes.setText(time[1]);
    }

    protected void makeUpdateNamazTimeCall() {

        try {
            NamazTimeUpdateRequestBean request = new NamazTimeUpdateRequestBean(currentNamazTimeName, currentNamazTime);
            request.setUserId(userId);
            request.setPassword(password);
            UIUtil.makeAPICall(UpdateNamazTimesActivity.this, ApiUtil.API_ENDPOINT_UPDATE_NAMAZ_TIMES
                                                            , JsonUtil.jsonFromObject(request));

        } catch(Throwable ex) {

            UIUtil.showToast(UpdateNamazTimesActivity.this, resources.getString(R.string.error_unknown)
                    , Toast.LENGTH_LONG);
        }

    }
}

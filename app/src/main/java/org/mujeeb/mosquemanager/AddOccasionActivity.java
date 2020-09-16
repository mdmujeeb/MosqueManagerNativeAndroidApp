package org.mujeeb.mosquemanager;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.mujeeb.mosquemanager.api.APICallCallback;
import org.mujeeb.mosquemanager.api.ApiUtil;
import org.mujeeb.mosquemanager.beans.request.OccasionRequestBean;
import org.mujeeb.mosquemanager.beans.response.BaseResponseBean;
import org.mujeeb.mosquemanager.util.DateUtil;
import org.mujeeb.mosquemanager.util.JsonUtil;
import org.mujeeb.mosquemanager.util.UIUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class AddOccasionActivity extends AppCompatActivity implements APICallCallback{

    protected String userId = null;
    protected String password = null;
    private Resources resources;

    private TextView txtDate;
    private EditText editDescription;
    private Button btnChangeDate;
    private Button btnCancel;
    private Button btnAddOccasion;

    private Date currentDate = new Date();
    private DatePickerDialog datePickerDialog;

    @Override
    public void onExecutionComplete(String result, String apiEndpoint) {

        if(apiEndpoint.equals(ApiUtil.API_ENDPOINT_ADD_OCCASION)) {

            BaseResponseBean response = (BaseResponseBean) JsonUtil.objectFromJson(result, BaseResponseBean.class);
            if(response.getResultCode() != 0) {

                UIUtil.showToast(AddOccasionActivity.this, response.getDescription(), Toast.LENGTH_LONG);
                return;
            }

            UIUtil.showToast(AddOccasionActivity.this
                    , resources.getString(R.string.message_add_occasion_successful)
                    , Toast.LENGTH_LONG);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_occasion);

        setTitle(R.string.title_activity_add_occasion);

        resources = getResources();

        txtDate = (TextView) findViewById(R.id.txtDate);
        editDescription = (EditText) findViewById(R.id.editDescription);
        btnChangeDate = (Button) findViewById(R.id.btnChangeDate);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnAddOccasion = (Button) findViewById(R.id.btnAddOccasion);

        updateDateView();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate();
            }
        });

        btnAddOccasion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String date = txtDate.getText().toString();
                String description = editDescription.getText().toString();
                if(description == null || description.isEmpty()) {

                    UIUtil.showToast(AddOccasionActivity.this, resources.getString(R.string.warning_invalid_description)
                                        , Toast.LENGTH_LONG);
                    return;
                }

                makeAddOccasionAPICall(date, description);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        // Get the Auth Credentials
        Map<String,String> credentials = UIUtil.getAuthCredentials(AddOccasionActivity.this);
        if(credentials != null) {
            this.userId = credentials.keySet().iterator().next();
            this.password = credentials.get(userId);
        }
    }

    private void makeAddOccasionAPICall(String date, String description) {

        try {
            OccasionRequestBean request = new OccasionRequestBean(null, date, description);
            request.setUserId(userId);
            request.setPassword(password);
            UIUtil.makeAPICall(AddOccasionActivity.this, ApiUtil.API_ENDPOINT_ADD_OCCASION
                    , JsonUtil.jsonFromObject(request));

        } catch(Throwable ex) {

            UIUtil.showToast(AddOccasionActivity.this, resources.getString(R.string.error_unknown)
                    , Toast.LENGTH_LONG);
        }
    }

    private void updateDateView() {

        txtDate.setText(DateUtil.formatSystemDate(currentDate));
    }

    private void chooseDate() {

        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                /*if(date.compareTo(currentDate) < 0 ){
                    UIUtil.showToast(DeliverySlotsActivity.this, resources.getString(R.string.warning_date_too_less)
                            , Toast.LENGTH_LONG);
                    showDatePicker(this);

                } else if(date.compareTo(DateUtil.addDaysToDate(currentDate, 60)) > 0 ){

                    UIUtil.showToast(DeliverySlotsActivity.this, resources.getString(R.string.warning_date_too_large)
                            , Toast.LENGTH_LONG);
                    showDatePicker(this);

                }else {*/

                currentDate = calendar.getTime();
                updateDateView();

                /*}*/
            }
        };

        // date picker dialog
        showDatePicker(listener);
    }

    protected void showDatePicker(DatePickerDialog.OnDateSetListener listener) {

        // calender class's instance and get current date , month and year from calender
        final Calendar calendar = Calendar.getInstance();
//        int mYear = calendar.get(Calendar.YEAR); // current year
//        int mMonth = calendar.get(Calendar.MONTH); // current month
//        int mDay = calendar.get(Calendar.DAY_OF_MONTH); // current day
        calendar.setTime(currentDate);

        datePickerDialog = new DatePickerDialog(AddOccasionActivity.this,
                listener, calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}

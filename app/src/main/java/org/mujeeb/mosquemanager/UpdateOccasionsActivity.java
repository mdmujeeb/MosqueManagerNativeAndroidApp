package org.mujeeb.mosquemanager;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.mujeeb.mosquemanager.api.APICallCallback;
import org.mujeeb.mosquemanager.api.ApiUtil;
import org.mujeeb.mosquemanager.beans.request.OccasionRequestBean;
import org.mujeeb.mosquemanager.beans.response.BaseResponseBean;
import org.mujeeb.mosquemanager.util.JsonUtil;
import org.mujeeb.mosquemanager.util.UIUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateOccasionsActivity extends AppCompatActivity implements APICallCallback {

    protected String userId = null;
    protected String password = null;

    protected Resources resources;

    protected LinearLayout layoutOccasions;
    protected Button btnAddOccasion;
    protected Button btnCancel;

    protected List<Map<String,String>> occasions;


    @Override
    public void onExecutionComplete(String result, String apiEndpoint) {

        if(apiEndpoint.equalsIgnoreCase(ApiUtil.API_ENDPOINT_GET_OCCASIONS)) {

            occasions = (List<Map<String,String>>) JsonUtil.objectFromJson(result, List.class);
            updateOccasionsView();

        } else if(apiEndpoint.equals(ApiUtil.API_ENDPOINT_DELETE_OCCASION)) {

            BaseResponseBean response = (BaseResponseBean) JsonUtil.objectFromJson(result, BaseResponseBean.class);
            if(response.getResultCode() != 0) {

                UIUtil.showToast(UpdateOccasionsActivity.this, response.getDescription(), Toast.LENGTH_LONG);
                return;
            }

            UIUtil.showToast(UpdateOccasionsActivity.this
                                , resources.getString(R.string.message_delete_occasion_successful)
                                , Toast.LENGTH_LONG);
            makeGetOccasionsAPICall();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_occasions);

        setTitle(R.string.title_activity_update_occasions);

        resources = getResources();
        layoutOccasions = (LinearLayout) findViewById(R.id.layoutOccasions);
        btnAddOccasion = (Button) findViewById(R.id.btnAddOccasion);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAddOccasion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UIUtil.openActivity(UpdateOccasionsActivity.this, AddOccasionActivity.class);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        // Get the Auth Credentials
        Map<String,String> credentials = UIUtil.getAuthCredentials(UpdateOccasionsActivity.this);
        if(credentials != null) {
            this.userId = credentials.keySet().iterator().next();
            this.password = credentials.get(userId);
        }

        makeGetOccasionsAPICall();
    }

    protected void makeGetOccasionsAPICall() {

        try {
            Map<String,String> parameters = new HashMap<String,String>();
            parameters.put("id", userId);
            UIUtil.makeAPICall(UpdateOccasionsActivity.this, ApiUtil.API_ENDPOINT_GET_OCCASIONS, parameters);

        } catch(Throwable ex) {

            UIUtil.showToast(UpdateOccasionsActivity.this, resources.getString(R.string.error_unknown)
                    , Toast.LENGTH_LONG);
        }
    }

    protected void updateOccasionsView() {

        if(occasions == null) {
            return;
        }

        layoutOccasions.removeAllViews();
        if(occasions == null || occasions.isEmpty()) {

            TextView date = new TextView(UpdateOccasionsActivity.this);
            layoutOccasions.addView(date);
            date.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            date.setText(resources.getString(R.string.message_no_occasions));
            date.setPadding(20,10,20, 10);
            date.setTypeface(null, Typeface.BOLD);
            date.setTextColor(resources.getColor(R.color.defaultTextColor));
            return;
        }

        for(final Map<String,String> occasion : occasions) {

            LinearLayout layout = new LinearLayout(UpdateOccasionsActivity.this);
            layout.setGravity(Gravity.START | Gravity.CENTER_HORIZONTAL);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                                        , LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setBackgroundColor(Color.LTGRAY);
            layoutOccasions.addView(layout);

            TextView date = new TextView(UpdateOccasionsActivity.this);
            layout.addView(date);
            date.setGravity(Gravity.START | Gravity.CENTER_HORIZONTAL);
            date.setText(occasion.get("date"));
            date.setPadding(20,10,20, 10);
            date.setTypeface(null, Typeface.BOLD);
            date.setTextColor(resources.getColor(R.color.defaultTextColor));

            TextView description = new TextView(UpdateOccasionsActivity.this);
            layout.addView(description);
            description.setGravity(Gravity.START | Gravity.CENTER_HORIZONTAL);
            description.setText(occasion.get("description"));
            description.setPadding(20,10,20, 10);
            description.setTypeface(null, Typeface.BOLD);
            description.setTextColor(resources.getColor(R.color.defaultTextColor));

            Button btnDelete = new Button(UpdateOccasionsActivity.this);
            btnDelete.setText(resources.getString(R.string.button_delete));
            description.setPadding(20,10,20, 10);
            layout.addView(btnDelete);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeDeleteOccasionCall(occasion.get("id"));
                }
            });
        }
    }

    private void makeDeleteOccasionCall(String id) {

        try {
            OccasionRequestBean request = new OccasionRequestBean(Integer.parseInt(id), null, null);
            request.setUserId(userId);
            request.setPassword(password);
            UIUtil.makeAPICall(UpdateOccasionsActivity.this, ApiUtil.API_ENDPOINT_DELETE_OCCASION
                                    , JsonUtil.jsonFromObject(request));

        } catch(Throwable ex) {

            UIUtil.showToast(UpdateOccasionsActivity.this, resources.getString(R.string.error_unknown)
                    , Toast.LENGTH_LONG);
        }
    }
}

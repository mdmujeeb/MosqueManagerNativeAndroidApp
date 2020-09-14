package org.mujeeb.mosquemanager.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.mujeeb.mosquemanager.R;
import org.mujeeb.mosquemanager.api.APICallAsyncTask;
import org.mujeeb.mosquemanager.api.APICallCallback;
import org.mujeeb.mosquemanager.api.APITaskPayloadBean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UIUtil {

    public static int convertDisplayPixelsToScreenPixels(Activity activity, int dp) {
        float density = activity.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static void underLineText(TextView view) {
        view.setPaintFlags(view.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }



    public static void makeAPICall(APICallCallback activity, String apiEndPoint, String jsonString)
                                                            throws ExecutionException, InterruptedException {

        APICallAsyncTask task = new APICallAsyncTask((Activity) activity, activity);
        task.execute(new APITaskPayloadBean(apiEndPoint, jsonString));
    }

    public static void makeAPICall(APICallCallback activity, String apiEndPoint, Map<String,String> parameters)
            throws ExecutionException, InterruptedException {

        APICallAsyncTask task = new APICallAsyncTask((Activity) activity, activity);
        task.setHttpMethod(APICallAsyncTask.HTTP_METHOD_GET);
        task.setParameters(parameters);
        task.execute(new APITaskPayloadBean(apiEndPoint, null));
    }

    public static void openActivity(Activity from, Class to) {
        Intent myIntent = new Intent(from, to);
        from.startActivity(myIntent);
    }

    public static void openActivityForResult(Activity from, Class to) {
        Intent myIntent = new Intent(from, to);
        from.startActivityForResult(myIntent, 0);
    }

    public static void goToUrl (Activity activity, String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        activity.startActivity(launchBrowser);
    }

    public static Map<String,String> getAuthCredentials(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.KEY_AUTH_CREDENTIALS, Activity.MODE_PRIVATE);
        String password = sharedPreferences.getString(Constants.KEY_PASSWORD, null);
        String userId = sharedPreferences.getString(Constants.KEY_USER_ID, null);

        if(password != null && !password.isEmpty() && userId != null && !userId.isEmpty()) {
            Map<String,String> credentials = new HashMap<String,String>();
            credentials.put(userId, password);
            return credentials;
        } else {
            return null;
        }
    }

    public static void storeAuthCredentials(Activity activity, String mobile, String authKey) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.KEY_AUTH_CREDENTIALS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_USER_ID, mobile);
        editor.putString(Constants.KEY_PASSWORD, authKey);
        editor.commit();
    }

    public static void showAlert(final Activity activity, String message) {

        showAlert(activity, activity.getResources().getString(R.string.message_title_warning), message);
    }

    public static void showAlert(final Activity activity, String title, String message) {

        showToast(activity, message, Toast.LENGTH_LONG);
    }

    public static void showConfirmation(final Activity activity, String title, String message, DialogInterface.OnClickListener confirmAction) {

        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, confirmAction)
                .setNegativeButton(android.R.string.no, null).show();
    }

    public static void showAlertDialog(final Activity activity, String title, String message
                                                    , DialogInterface.OnClickListener okAction) {

        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, okAction).show();
    }

    public static void showToast(Activity activity, String message, int length) {
        Toast.makeText(activity, message, length).show();
    }

    public static void setLoginLogoutMenuTitle(Activity activity, String authToken, Menu menu) {

        if(menu == null) {
            return;
        }

        final Resources resources = activity.getResources();

        // Change text of Login/Logout Menu
        MenuItem item = (MenuItem) menu.findItem(R.id.action_login_logout);
        if(authToken != null && !authToken.isEmpty()) {
            item.setTitle(resources.getString(R.string.button_logout));
        } else {
            item.setTitle(resources.getString(R.string.button_login));
        }
    }

    public static void selectItemInSpinner(Spinner spinner, String item) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++) {
            if(adapter.getItem(position).equals(item)) {
                spinner.setSelection(position);
                return;
            }
        }
    }

    public static void showPleaseWaitIndicator(Activity activity, boolean isShow) {

//        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
//        Resources resources = activity.getResources();
//        alertDialog.setTitle(resources.getString(R.string.message_title_processing));
//        alertDialog.setMessage(resources.getString(R.string.message_message_processing));
//        ImageView image = new ImageView(activity);
//        image.setImageResource(R.drawable.running_man);
//        alertDialog.setView(image);

//        InputStream stream = null;
//        try {
//            stream = activity.getResources().getAssets().open("running_man.gif");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        GifMovieView view = new GifMovieView(activity, stream);
//        alertDialog.setView(view);

//        alertDialog.show();
//
//        return alertDialog;

//        ScrollView scrContentScreen = activity.findViewById(R.id.scrContentScreen);
        RelativeLayout rltLoadingPanel = activity.findViewById(R.id.rltLoadingPanel);

//        scrContentScreen.setVisibility(isShow ? View.GONE : View.VISIBLE);
        rltLoadingPanel.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public static void removeAllCapsFromTabHost(TabHost host, int tabCount) {

        for(int i=0; i<tabCount; i++) {
            TextView tv = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setAllCaps(false);
        }
    }

    public static LinearLayout getPairLinearLayoutView(Activity activity
                            , String key, String value, boolean isVertical) {

        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(isVertical ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
        layout.setBackgroundColor(Color.LTGRAY);

        TextView view1 = new TextView(activity);
        if(isVertical) {
            view1.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            view1.setPadding(20,10,5, 0);
        } else {
            view1.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            view1.setPadding(20,10,5, 0);
        }
        view1.setText(key);
        layout.addView(view1);

        TextView view2 = new TextView(activity);
        view2.setTypeface(null, Typeface.BOLD);
        if(isVertical) {
            view2.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            view2.setPadding(0,10,20, 0);
        } else {
            view2.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            view2.setPadding(0,10,20, 0);
        }
        view2.setText(value);
        layout.addView(view2);

        return layout;
    }
}

package org.mujeeb.mosquemanager.api;

import android.app.Activity;
import android.os.AsyncTask;

import org.mujeeb.mosquemanager.util.UIUtil;

import java.util.Map;

/**
 * Created by Mohammad Mujeeb on 16-12-2017.
 */

public class APICallAsyncTask extends AsyncTask<APITaskPayloadBean, Integer, String> {

    public static final String HTTP_METHOD_GET = "GET";
    public static final String HTTP_METHOD_POST = "POST";

    protected Activity activity;
    protected APICallCallback callback;
    protected String response;
    protected String apiEndPoint;
    protected String httpMethod = HTTP_METHOD_POST;
    protected Map<String,String> parameters;

    public APICallAsyncTask(Activity activity, APICallCallback callback) {

        this.activity = activity;
        this.callback = callback;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        UIUtil.showPleaseWaitIndicator(activity, true);
    }

    @Override
    protected String doInBackground(APITaskPayloadBean... params){

       try {
            this.apiEndPoint = params[0].getHandlerURL();
            String response = null;
            if(httpMethod.equals(HTTP_METHOD_POST)) {

                response = ApiUtil.doPost(params[0].getHandlerURL(), params[0].getJsonString());
            } else {

                response = ApiUtil.doGet(params[0].getHandlerURL(), parameters);
            }
            this.response = response;
            return response;

        }catch(Exception ex) {
            ex.printStackTrace();;
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        // Do Something for progress update
    }

    @Override
    protected void onPostExecute(String param) {

        super.onPostExecute(param);

        // Hide Please wait indicator
        UIUtil.showPleaseWaitIndicator(activity, false);

        callback.onExecutionComplete(response, apiEndPoint);
    }
}

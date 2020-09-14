package org.mujeeb.mosquemanager.api;

import java.util.Map;

/**
 * Created by Mohammad Mujeeb on 16-12-2017.
 */

public class APITaskPayloadBean {

    protected String handlerURL;
    protected String jsonString;

    public APITaskPayloadBean() {}

    public APITaskPayloadBean(String handlerURL, String jsonString) {
        this.handlerURL = handlerURL;
        this.jsonString = jsonString;
    }

    public String getHandlerURL() {
        return handlerURL;
    }

    public void setHandlerURL(String handlerURL) {
        this.handlerURL = handlerURL;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }
}

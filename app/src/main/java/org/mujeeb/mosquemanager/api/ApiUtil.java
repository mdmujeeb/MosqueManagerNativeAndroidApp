package org.mujeeb.mosquemanager.api;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Mohammad Mujeeb on 16-12-2017.
 */

public class ApiUtil {

    public static String apiHost = "mosquemanager.azurewebsites.net";
//    public static final String API_HOST = "mujeeb.org";

    // User API Calls
    public static final String API_ENDPOINT_LOGIN                   = "authenticate";
    public static final String API_ENDPOINT_GET_NAMAZ_TIMES         = "getNamazTimes";
    public static final String API_ENDPOINT_UPDATE_NAMAZ_TIME       = "updateNamazTime";
    public static final String API_ENDPOINT_UPDATE_REFRESH_REQUIRED = "updateRefreshRequired";
    public static final String API_ENDPOINT_GET_OCCASIONS           = "getOccasions";
    public static final String API_ENDPOINT_DELETE_OCCASION         = "deleteOccasion";
    public static final String API_ENDPOINT_ADD_OCCASION            = "addOccasion";

    // Takes JSON string as parameter, posts to API endpoint and returns another JSON string
    public static String doPost(String endPointUrl, String json) {

        String strUrl = "https://" + apiHost + "/" + endPointUrl;

        try {
            URL url = new URL(strUrl);
            byte[] postDataBytes = json.getBytes();

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            IOUtils.copy(conn.getInputStream(), stream);
            conn.disconnect();
            String result = new String(stream.toByteArray());
            System.out.println(result);
            return result;

        } catch(Exception ex) {

            ex.printStackTrace();;
        }

        return null;
    }

    // Takes JSON string as parameter, posts to API endpoint and returns another JSON string
    public static String doGet(String endPointUrl, Map<String,String> parameters) {

        String strUrl = "https://" + apiHost + "/" + endPointUrl;

        try {
            StringBuilder builder = new StringBuilder("?");
            int i=0;
            for(Iterator<String> it = parameters.keySet().iterator(); it.hasNext();) {

                if(i>0) {
                    builder.append("&");
                }
                String paramName = it.next();
                String value = parameters.get(paramName);
                builder.append(paramName).append("=").append(value);
                i++;
            }

            strUrl += builder.toString();
            URL url = new URL(strUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
//            con.setRequestProperty("User-Agent", USER_AGENT);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            IOUtils.copy(con.getInputStream(), stream);
            con.disconnect();
            String result = new String(stream.toByteArray());
            System.out.println(result);
            return result;

        } catch(Exception ex) {

            ex.printStackTrace();;
        }

        return null;
    }

    public static String getApiHost() {
        return apiHost;
    }

    public static void setApiHost(String pApiHost) {
        apiHost = pApiHost;
    }
}

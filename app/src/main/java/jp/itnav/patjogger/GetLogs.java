package jp.itnav.patjogger;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by flatfisher on 3/6/16.
 */
public class GetLogs {

    GetLogListener listener;

    public GetLogs(GetLogListener listener) {
        this.listener = listener;
    }

    public interface GetLogListener{
        void onResult(List<VisitLog> result, int requestType);
        void onUserHistory(List<LatLng> result);
    }

    public void requestTimeList() {
      requestLog("http://patrol.mybluemix.net/API/1/counttime/",0);
    }

    public void  requestDateList() {
        requestLog("http://patrol.mybluemix.net/API/1/countdate/",1);
    }

    public void requestUserLog(){
        requestUser();
    }

    private void requestLog(final String requestUrl,final int type) {
        final List<VisitLog> visitLogList = new ArrayList<VisitLog>();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = null;

                Request request = new Request.Builder()
                        .url(requestUrl)
                        .get()
                        .build();

                OkHttpClient client = new OkHttpClient();

                try {
                    Response response = client.newCall(request).execute();
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONArray logArray = new JSONArray(result);
                    int length = logArray.length();
                    for (int i = 0; i < length; i++) {
                        VisitLog visitLog = new VisitLog();
                        JSONObject object = logArray.getJSONObject(i);

                        if (type == 0){
                            visitLog.date =  object.getString("time");
                        }else if(type ==1){
                            visitLog.date =  object.getString("yyyymmdd");
                        }
                        visitLog.geoHash =object.getString("geohash");
                        visitLog.count =Integer.parseInt(object.getString("count"));
                        visitLogList.add(visitLog);
                    }

                    GetLogs.this.listener.onResult(visitLogList,type);

                } catch (JSONException e) {

                }
                System.out.println("---response---");
                System.out.println(result);
                System.out.println("---response---");
            }
        }.execute();
    }

    private void requestUser() {
        final List<LatLng> userLogList = new ArrayList<LatLng>();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = null;

                Request request = new Request.Builder()
                        .url("http://patrol.mybluemix.net/API/1/locations/odaka2")
                        .get()
                        .build();

                OkHttpClient client = new OkHttpClient();

                try {
                    Response response = client.newCall(request).execute();
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONArray userArray = new JSONArray(result);
                    int length = userArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject user = userArray.getJSONObject(i);
                        double latitude = Double.parseDouble(user.getString("latitude"));
                        double longitude = Double.parseDouble(user.getString("longitude"));
                        LatLng latLng = new LatLng(latitude,longitude);
                        userLogList.add(latLng);
                    }

                    GetLogs.this.listener.onUserHistory(userLogList);

                } catch (JSONException e) {

                }
                System.out.println("---response---");
                System.out.println(result);
                System.out.println("---response---");
            }
        }.execute();
    }

    class VisitLog {
        public String date;
        public String geoHash;
        public int count;
    }

    //Example code
//    GetLogs getLogs = new GetLogs(new GetLogs.GetLogListener() {
//        @Override
//        public void onResult(List<GetLogs.VisitLog> result, int requestType) {
//            Log.i("result",result.get(0).geoHash);
//        }
//
//        @Override
//        public void onUserHistory(List<LatLng> result) {
//            Log.i("lat",""+result.get(0).latitude);
//        }
//    });
//
//    getLogs.requestTimeList();
//    getLogs.requestDateList();
//    getLogs.requestUserLog();
}

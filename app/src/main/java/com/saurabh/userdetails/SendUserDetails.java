package com.saurabh.userdetails;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class SendUserDetails extends AsyncTask<String, Void, String> {

    TextView status;
    String error;
    public SendUserDetails(TextView status) {
        this.status = status;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        status.setText("Sending");
        error = "";
    }

    @Override
    protected String doInBackground(String... strings) {
        String res = "";

        HttpURLConnection httpURLConnection = null;
        try {

            httpURLConnection = (HttpURLConnection) new URL("https://external.dev.pheramor.com/").openConnection();
            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes("PostData=" + strings[0]);
            wr.flush();
            wr.close();

            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String response;
                while ((response = br.readLine()) != null){
                    res += (response);
                }
            }
        } catch (Exception e) {
            error = "Connection Failed";
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return res;
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
        if(error.length() > 0) status.setText(error);
        else {
            JSONObject x;
            try {
                x = new JSONObject(res);
                if(x.get("status").toString().equalsIgnoreCase("true"))
                    status.setText("Success");
            } catch (JSONException e) {
                Log.d("error", "converting to json");
            }
        }
    }
}

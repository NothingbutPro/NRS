package com.ics.nrs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.nrs.R;
import com.ics.nrs.Utility.Connectivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class VerificationActivity extends AppCompatActivity {
    EditText enterUsername;
    Button submit_verify;
    String EnterUsername;
    String otp = "";
    String mobile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        enterUsername = (EditText)findViewById(R.id.enterUsername);
        submit_verify  =(Button) findViewById(R.id.submit_verify);

        submit_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EnterUsername = enterUsername.getText().toString();

                if (Connectivity.isNetworkAvailable(VerificationActivity.this)){
                    new PostVerfication().execute();
                }else {
                    Toast.makeText(VerificationActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //------------------------------------------

    public class PostVerfication extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(VerificationActivity.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://infocentroid.us/NRS_LIVE/Api/forget_password_otp");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("name", EnterUsername);

                Log.e("postDataParams", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds*/);
                conn.setConnectTimeout(15000  /*milliseconds*/);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        StringBuffer Ss = sb.append(line);
                        Log.e("Ss", Ss.toString());
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                JSONObject jsonObject = null;
                Log.e("PostVerfication", result.toString());
                try {

                    jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    JSONObject dataObj = jsonObject.getJSONObject("data");
                     otp = dataObj.getString("otp");
                     mobile = dataObj.getString("mobile");

                    if (status.equalsIgnoreCase("true")) {
                        AppPreference.setEmpotp(VerificationActivity.this, otp);
                        AppPreference.setEmpmob(VerificationActivity.this, mobile);
                       Intent intent = new Intent(VerificationActivity.this,ForgetPasswordActivity.class);
                       startActivity(intent);
                    }else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
    }

    //-------------------------------------------------

}

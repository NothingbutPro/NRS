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
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class UserLoginActivity extends AppCompatActivity {

    EditText et_mobileno,et_username;
    String Mobileno,Username;
    Button user_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        et_username = (EditText)findViewById(R.id.et_username);
        et_mobileno = (EditText)findViewById(R.id.et_mobileno);
        user_login = (Button)findViewById(R.id.user_login);

        user_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Username = et_username.getText().toString();
                Mobileno = et_mobileno.getText().toString();

              if (Connectivity.isNetworkAvailable(UserLoginActivity.this)) {
                    new SendUserLogin().execute();
                }else {
                  Toast.makeText(UserLoginActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
              }
            }
        });

        if (!AppPreference.getCustMobileno(this).equalsIgnoreCase("")) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }


    class SendUserLogin extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(UserLoginActivity.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://infocentroid.us/NRS_LIVE/Api/customer_login");


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("name", Username);
                postDataParams.put("mobile", Mobileno);


                Log.e("postDataParams", postDataParams.toString());

                URLConnection l_connection = null;

                SSLContext sslcontext = SSLContext.getInstance("TLSv1");
                sslcontext.init(null, null, null);
                SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());

                HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);
                l_connection = (HttpsURLConnection)url.openConnection();
                l_connection.connect();

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

                    BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        result.append(line);
                    }
                    r.close();
                    return result.toString();

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

                // JSONObject jsonObject = null;
                Log.e("SendLogin>>>", result.toString());
                try {


                    JSONObject jsonObject =new JSONObject(result);

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    JSONObject data = jsonObject.getJSONObject("data");

                    String id = data.getString("id");
                    String name = data.getString("name");
                    String mobile = data.getString("mobile");
                    String otp = data.getString("otp");
                    String created = data.getString("created");


                    Log.e(">>>>", jsonObject.toString() + " " + status + " " + message);

                    if (status.equalsIgnoreCase("true")) {

                        AppPreference.setCustName(UserLoginActivity.this,name);
                        AppPreference.setCustMobileno(UserLoginActivity.this,mobile);
                        AppPreference.setCustOtp(UserLoginActivity.this,otp);


                        Toast.makeText(UserLoginActivity.this,message,Toast.LENGTH_LONG).show();

                        et_username.setText("");
                        et_mobileno.setText("");


                        Intent intent = new Intent(UserLoginActivity.this, OtpActivity.class);
                        startActivity(intent);

                        finish();
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


}

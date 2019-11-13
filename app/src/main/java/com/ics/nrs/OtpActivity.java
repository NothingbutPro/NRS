package com.ics.nrs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.nrs.R;
import com.ics.nrs.SharedPreference.SessionManager;
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

public class OtpActivity extends AppCompatActivity {
    TextView forgetPassword;
    EditText et_otp;
    String Otp, Name, MobileNo, product_name;
    Button otp_verify,resetOtp;
    String getOtp, serial_no_, model_no_, pincode_;
    private SessionManager manager;
    String Token = "4568", Username = "", Password = "123456";
    String id, mobilenumber = "", address = "", email;
    private String strMob_otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        et_otp = (EditText) findViewById(R.id.et_otp);
        otp_verify = (Button) findViewById(R.id.otp_verify);
        resetOtp = (Button) findViewById(R.id.button);

        // forgetPassword = (TextView)findViewById(R.id.forgetPassword);

        manager = new SessionManager(this);
        Username = manager.getUsername();
        MobileNo = manager.getCompMobNo();
        address = manager.getAddress();
        product_name = manager.getProduct();
        serial_no_ = manager.getSerial();
        model_no_ = manager.getModel();
        pincode_ = manager.getPincode();
//        et_otp.setText(manager.getOtp());
        if (Connectivity.isNetworkAvailable(OtpActivity.this)) {
            new SendUserLogin(Username, MobileNo).execute();
        } else {
            Toast.makeText(OtpActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

        otp_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(et_otp.getText().toString());
            }
        });

        resetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_otp.setText("");
                otpDialog();
            }
        });
    }

    private void otpDialog() {

        final Dialog dialog = new Dialog(OtpActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.otp_reset_layout);
        // dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        // final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner1);
        final TextView text1 = (TextView) dialog.findViewById(R.id.text1);
        final EditText editText1 = (EditText) dialog.findViewById(R.id.editText1);

        Button button = (Button) dialog.findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                strMob_otp = editText1.getText().toString();
                if (strMob_otp.isEmpty()) {
                    editText1.setError("Mobile Number Is Required");
                    editText1.requestFocus();
                    return;
                }
                if (strMob_otp.length() != 10) {
                    editText1.setError("Mobile Number Should Be 10 Digits");
                    editText1.requestFocus();
                    return;
                }

                if (Connectivity.isNetworkAvailable(OtpActivity.this)) {
                    dialog.dismiss();
                    new GETOtp(strMob_otp).execute();
                    Log.e("OTTP", strMob_otp);
                } else {
                    Toast.makeText(OtpActivity.this, "no Internet", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    class GETOtp extends AsyncTask<String, String, String> {

        ProgressDialog dialog;
        String strMob_otp;

        public GETOtp(String strMob_otp) {
            this.strMob_otp = strMob_otp;
        }

        protected void onPreExecute() {
            dialog = new ProgressDialog(OtpActivity.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://infocentroid.us/NRS_LIVE/Api/resendotp");


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("mobile", strMob_otp);

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
                try {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equals("true")) {
//                        et_otp.setText(manager.getOtp());
                        Toast.makeText(OtpActivity.this, "Otp Send To " + strMob_otp + " Number", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(OtpActivity.this, message, Toast.LENGTH_LONG).show();

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

    private void submit(String s) {
        String opt = manager.getOtp();
        if (s.equals(manager.getOtp())) {
            Toast.makeText(getApplicationContext(), " Otp verify", Toast.LENGTH_LONG);
            if (Connectivity.isNetworkAvailable(OtpActivity.this)) {
                new SendJsonDataToServer().execute();
            } else {
                Toast.makeText(OtpActivity.this, "No Internet", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Wrong Otp", Toast.LENGTH_LONG);
        }

    }

    class SendUserLogin extends AsyncTask<String, String, String> {

        ProgressDialog dialog;
        String username;
        String mobileNo;

        public SendUserLogin(String username, String mobileNo) {
            this.username = username;
            this.mobileNo = mobileNo;
        }

        protected void onPreExecute() {
            dialog = new ProgressDialog(OtpActivity.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://infocentroid.us/NRS_LIVE/Api/customer_login");


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("name", username);
                postDataParams.put("mobile", mobileNo);


                Log.e("postDataParams", postDataParams.toString());

                URLConnection l_connection = null;

                SSLContext sslcontext = SSLContext.getInstance("TLSv1");
                sslcontext.init(null, null, null);
                SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());

                HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);
                l_connection = (HttpsURLConnection) url.openConnection();
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

                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    JSONObject data = jsonObject.getJSONObject("data");

                    String id = data.getString("id");
                    String name = data.getString("name");
                    String mobile = data.getString("mobile");
                    Otp = data.getString("otp");
                    String created = data.getString("created");


                    Log.e(">>>>", jsonObject.toString() + " " + status + " " + message);

                    if (status.equals("true")) {
//                        et_otp.setText(Otp);
                        manager.setOtp(Otp);
                        Toast.makeText(OtpActivity.this, "Otp Add Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(OtpActivity.this, "Can't Get OTP", Toast.LENGTH_LONG).show();
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

    class SendJsonDataToServer extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(OtpActivity.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://infocentroid.us/NRS_LIVE/Api/complian");


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("customer_name", Username);
                postDataParams.put("mobile_no", MobileNo);
                postDataParams.put("address", address);
                postDataParams.put("product_name", product_name);
                postDataParams.put("model_no", model_no_);
                postDataParams.put("serial_no", serial_no_);
                postDataParams.put("pin_code", pincode_);


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
                JSONObject jsonObject = null;
                Log.e("SendJsonDataToServer>>>", result.toString());
                try {
                    jsonObject = new JSONObject(result);
                    String response = jsonObject.getString("response");
                    String message = jsonObject.getString("message");
                    Log.e(">>>>", jsonObject.toString() + " " + response + " " + message);

                    if (response.equals("1")) {
                        et_otp.setText("");
                        Intent intent = new Intent(OtpActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(OtpActivity.this, "Complian Successfull ", Toast.LENGTH_LONG).show();
                    } else if (response.equals("0")) {
                        Toast.makeText(OtpActivity.this, message, Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OtpActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }
}

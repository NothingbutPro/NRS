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

import com.example.android.nrs.R;

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

public class Feedback extends AppCompatActivity {

    EditText complaintCode,payment,feedback,oldPartName,newPartName,oldPartSerial,newPartSerial,oldPartCode,newPartCode;

    String ComplaintCode,Payment,Feedback,OldPartName,NewPartName,OldPartSerial,NewPartSerial,OldPartCode,NewPartCode;
    Button submit;
    String status;
    //public LinearLayout closed,pending;

    //URL url="http://spellclasses.co.in/NRS/api/feedback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        submit=(Button)findViewById(R.id.bt_submit);
        complaintCode=(EditText)findViewById(R.id.et_complaint);
        payment=(EditText)findViewById(R.id.et_payment);
        feedback=(EditText)findViewById(R.id.et_feedback);
        oldPartName=(EditText)findViewById(R.id.oldPartName);
        newPartName=(EditText)findViewById(R.id.newPartName);
        oldPartSerial=(EditText)findViewById(R.id.oldPartSerial);
        newPartSerial=(EditText)findViewById(R.id.newPartSerial);
        oldPartCode=(EditText)findViewById(R.id.oldPartCode);
        newPartCode=(EditText)findViewById(R.id.newPartCode);

        //status=AppPreference.getStatus(Feedback.this);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ComplaintCode = complaintCode.getText().toString().trim();
                Payment = payment.getText().toString();
                Feedback = feedback.getText().toString();
                OldPartName=oldPartName.getText().toString();
                NewPartName=newPartName.getText().toString();
                OldPartSerial=oldPartSerial.getText().toString();
                NewPartSerial=newPartSerial.getText().toString();
                OldPartCode=oldPartCode.getText().toString();
                NewPartCode=newPartCode.getText().toString();

                new SendFeedback().execute();
            }
        });
    }



    class SendFeedback extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(Feedback.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://spellclasses.co.in/NRS/api/feedback");


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("complian_id", ComplaintCode);
                postDataParams.put("payment", Payment);
                postDataParams.put("feedback", Feedback);
                postDataParams.put("old_serial_no", OldPartSerial);
                postDataParams.put("new_serial_no", NewPartSerial);
                postDataParams.put("old_name_of_part", OldPartName);
                postDataParams.put("new_name_of_part", NewPartName);
                postDataParams.put("old_code_of_part", OldPartCode);
                postDataParams.put("new_code_of_part", NewPartCode);



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

                  /*  BufferedReader in = new BufferedReader(new
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
                    return sb.toString();*/

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
                Log.e("SendFeedback>>>", result.toString());
                try {


                    // result=getJSONUrl(URL);  //<< get json string from server
                   // JSONObject jsonObject = new JSONObject(result);
                    //jsonObject = new JSONObject(result);

                    JSONObject jsonObject =new JSONObject(result);

                    String response = jsonObject.getString("response");
                    String message = jsonObject.getString("message");

                    Log.e(">>>>", jsonObject.toString() + " " + response + " " + message);

                    if (response.equalsIgnoreCase("1")) {

                        complaintCode.setText("");
                        payment.setText("");
                        feedback.setText("");
                        oldPartName.setText("");
                        oldPartSerial.setText("");
                        newPartCode.setText("");
                        oldPartCode.setText("");
                        newPartName.setText("");
                        newPartSerial.setText("");

                       // Toast.makeText(Feedback.this,"Feedback Sent",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(Feedback.this, ComplaintList.class);
                        startActivity(intent);
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

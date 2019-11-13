package com.ics.nrs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.nrs.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class StaffProfile extends AppCompatActivity {

    Button update;
    ImageView profileImage;
    EditText et_email ,et_mobilenum ,et_address;
    int RESULT_LOAD_IMG=1;
    String ProfileImage ,Email, MobileNo, Address,Complain_id,Profile;
    Context context;
    TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_profile);

        Complain_id = AppPreference.getComplaintId(StaffProfile.this);
        Email = AppPreference.getEmail(StaffProfile.this);
        MobileNo = AppPreference.getMobileNo(StaffProfile.this);
        Address = AppPreference.getAddress(StaffProfile.this);
       // Profile = AppPreference.getProfile(StaffProfile.this);
        forgetPassword = (TextView)findViewById(R.id.forgetPassword);

        profileImage=(ImageView)findViewById(R.id.profile_image);
      //  Picasso.with(context).load(Profile).into(profileImage);
      //  ProfileImage=AppPreference.getProfile(StaffProfile.this);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        et_email = (EditText)findViewById(R.id.et_email);
        et_email.setEnabled(false);
        et_mobilenum = (EditText)findViewById(R.id.et_mobilenum);
        et_address = (EditText)findViewById(R.id.et_address);


        et_email.setText(Email);
        et_address.setText(Address);
        et_mobilenum.setText(MobileNo);


        update=(Button)findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MobileNo=et_mobilenum.getText().toString();
                Address = et_address.getText().toString();

                new SendUpdate().execute();
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(StaffProfile.this,VerificationActivity.class);
                startActivity(intent);
            }
        });
    }


    class SendUpdate extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(StaffProfile.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://infocentroid.us/NRS_LIVE/Api/Update_profile");


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("id",Complain_id);
                postDataParams.put("email",Email);
                postDataParams.put("mobilenumber", MobileNo);
                postDataParams.put("address", Address);


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

                // JSONObject jsonObject = null;
                Log.e("SendJsonDataToServer>>>", result.toString());
                try {


                    // result=getJSONUrl(URL);  //<< get json string from server
                    JSONObject jsonObject = new JSONObject(result);
                    //jsonObject = new JSONObject(result);
                    String response = jsonObject.getString("response");
                    String message = jsonObject.getString("message");

                    Log.e(">>>>", jsonObject.toString() + " " + response + " " + message);

                    if (response.equalsIgnoreCase("1")) {


                        Toast.makeText(StaffProfile.this,"Profile Updated",Toast.LENGTH_LONG).show();
                       /* Intent intent = new Intent(getContext(), ComplaintList.class);
                        startActivity(intent);*/
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
//******************************************************

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                profileImage.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(StaffProfile.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(StaffProfile.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
}

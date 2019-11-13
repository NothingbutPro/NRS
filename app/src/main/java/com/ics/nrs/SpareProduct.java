package com.ics.nrs;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.android.nrs.R;
import com.ics.nrs.Model.SparePart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SpareProduct extends AppCompatActivity {

    private List<SparePart> roProductList;
    private RecyclerView recyclerView;
    private SpareAdapter mAdapter;
    String server_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spare_product);
        roProductList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        new GetSpareProduct().execute();


    }


    class GetSpareProduct extends AsyncTask<String, String, String> {
        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(SpareProduct.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
            //movieList = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url ="https://infocentroid.us/NRS_LIVE/Api/getAllSPareParts";

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("sever_url>>>>>>>>>", server_url);
            output = HttpHandler.makeServiceCall(server_url);
            System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
                dialog.dismiss();
            } else {
                try {
                    dialog.dismiss();
                    JSONObject json = new JSONObject(output);
                    String response = json.getString("status");
                    String message=json.getString("message");
                    JSONArray message_array = json.getJSONArray("data");
                    for (int i=0; i<message_array.length(); i++){
                        JSONObject m = message_array.getJSONObject(i);
                        String id=m.getString("id");
                        String product_id=m.getString("product_id");
                        String ro_name = m.getString("s_name");
                        String ro_price = m.getString("price");
                        String ro_descrip = m.getString("description");
                        String ro_image = m.getString("image");

                        SparePart proCons=new SparePart();
                        proCons.setSpareName(ro_name);
                        proCons.setSparePrice(ro_price);
                        proCons.setSpareDescrip(ro_descrip);
                        proCons.setSpareImage(ro_image);

                        roProductList.add(proCons);

                    }

                    mAdapter = new SpareAdapter(roProductList,SpareProduct.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);



                    Log.e("json>>>>>>>>>",json.toString());


                    if (response.equalsIgnoreCase("true")) {
                        dialog.dismiss();


                        //  AppPreference.setStatus(ComplaintList.this, status);



                    }

                    else
                    {
                        Toast.makeText(SpareProduct.this,response,Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
                super.onPostExecute(output);
            }

        }

    }


}

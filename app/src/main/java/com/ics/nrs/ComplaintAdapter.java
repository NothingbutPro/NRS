package com.ics.nrs;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.nrs.R;

import java.util.List;

/**
 * Created by android on 5/25/2018.
 */

public class ComplaintAdapter  extends RecyclerView.Adapter<ComplaintAdapter.MyViewHolder> {

    private List<Complaint> moviesList;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;
        public TextView complaintId, productName,name,mobile_no,location;
        public ImageView product_image;
        public Button feedback_complaint;
        public LinearLayout closed,pending;
        public String status;


        public MyViewHolder(View view) {
            super(view);
            //complaintId = (TextView) view.findViewById(R.id.complaintId);
            //complaint_descrip = (TextView) view.findViewById(R.id.complaint_descrip);
           // product_image=(ImageView)view.findViewById(R.id.image);
            feedback_complaint=(Button) view.findViewById(R.id.feedback_complaint);
            name=(TextView) view.findViewById(R.id.name);
            productName=(TextView)view.findViewById(R.id.prod_name);
            mobile_no= (TextView) view.findViewById(R.id.mobile_no);
            location= (TextView) view.findViewById(R.id.location);
            closed=(LinearLayout) view.findViewById(R.id.closed);
          //  pending=(LinearLayout) view.findViewById(R.id.pending);

            view.setOnClickListener(this);

            //   status=AppPreference.getStatus(context);

            

        }


        @Override
        public void onClick(View view) {

        }
    }


    public ComplaintAdapter(List<Complaint> moviesList) {

        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.complaint_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Context context = null;
        Complaint movie = moviesList.get(position);


      //  holder.complaintId.setText(movie.getComplaint_code());
        holder.productName.setText(movie.getProductName());
        holder.name.setText(movie.getName());
        holder.mobile_no.setText(movie.getMobile_no());
        holder.location.setText(movie.getLocation());


        /*if(holder.status.equalsIgnoreCase("1"))
        {
            holder.pending.setVisibility(View.VISIBLE);
            holder.closed.setVisibility(View.GONE);
        }
        else{
            holder.pending.setVisibility(View.GONE);
            holder.closed.setVisibility(View.VISIBLE);
        }*/


        /*holder.feedback_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // new Intent(getContext(),Feedback.class);

                Intent intent = new Intent(view.getContext(),NewFeedbackActivity.class);
                view.getContext().startActivity(intent);

                *//*Intent intent = new Intent(view.getContext(),Feedback.class);
                view.getContext().startActivity(intent);*//*

                *//*holder.pending.setVisibility(View.GONE);
                holder.closed.setVisibility(View.VISIBLE);*//*

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}

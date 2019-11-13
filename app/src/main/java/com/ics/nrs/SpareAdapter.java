package com.ics.nrs;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.nrs.R;
import com.ics.nrs.Model.SparePart;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SpareAdapter extends RecyclerView.Adapter<SpareAdapter.MyViewHolder> {


    private List<SparePart> spareList;
    Context context;

    public SpareAdapter(List<SparePart> spareList,Context context)
    {
        this.context=context;
        this.spareList=spareList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.neww, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        SparePart prod= spareList.get(position);
        holder.spareName.setText(prod.getSpareName());
        holder.sparePrice.setText(prod.getSparePrice());
      //  holder.spareDescrip.setText(prod.getSpareDescrip());

        holder.spareDescrip.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                showProductDetail(spareList.get(position).getSpareImage(),spareList.get(position).getSpareName(),spareList.get(position).getSpareDescrip());


            }
        });


        Picasso.with(context)
                .load(prod.getSpareImage())
                .into(holder.spareImage);

    }


    private void showProductDetail(String image, String title, String description) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_product);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_product_detail_img);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_product_detail_title);
        TextView tv_detail = (TextView) dialog.findViewById(R.id.tv_product_detail);
        ImageView iv_dialog_cancle = (ImageView)dialog.findViewById(R.id.iv_dialog_cancle);

        tv_title.setText(title);
        tv_detail.setText(description);

        Picasso.with(context)
                .load(image)
                .into(iv_image);

        iv_dialog_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return spareList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView spareName, sparePrice,spareDescrip;
        public ImageView spareImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            spareName = (TextView) itemView.findViewById(R.id.spare_name);
            spareImage=(ImageView)itemView.findViewById(R.id.spare_image);
            sparePrice=(TextView) itemView.findViewById(R.id.spare_price);
            spareDescrip=(TextView)itemView.findViewById(R.id.description);

        }
    }
}

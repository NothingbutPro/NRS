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
import com.ics.nrs.Model.RoProduct;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>  {

    private List<RoProduct> roList;
    Context context;

    public CustomAdapter(List<RoProduct> roList,Context context)
    {
        this.context=context;
        this.roList=roList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final RoProduct prod= roList.get(position);
        holder.roName.setText(prod.getRoName());
        holder.roPrice.setText(prod.getRoPrice());
      //  holder.roDescrip.setText(prod.getRoDescrip());

        holder.roDescrip.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


            showProductDetail(roList.get(position).getProdImage(),roList.get(position).getRoName(),roList.get(position).getRoDescrip());


            }
        });

        Picasso.with(context)
                .load(prod.getProdImage())
                .into(holder.product_image);
        //holder.product_image.setImageResource(Integer.parseInt(prod.getProdImage()));


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
        return roList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView roName, roPrice,roDescrip;
        public ImageView product_image;

        public MyViewHolder(View itemView) {
            super(itemView);

            roName = (TextView) itemView.findViewById(R.id.name);
            product_image=(ImageView)itemView.findViewById(R.id.image);
            roPrice=(TextView) itemView.findViewById(R.id.price);
            roDescrip=(TextView)itemView.findViewById(R.id.description);
        }
    }
}

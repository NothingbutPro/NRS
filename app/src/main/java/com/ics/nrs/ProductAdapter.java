package com.ics.nrs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.nrs.R;

import java.util.ArrayList;

/**
 * Created by android on 5/24/2018.
 */

public class ProductAdapter extends BaseAdapter {

    Context context;
    String detailList[];
    String rateList[];
    int product_items[];
    // int flags[];
    LayoutInflater inflter;

    public ProductAdapter(Context context, String[] detailList, String[] rateList,int[] product_items) {
        this.context = context;
        this.detailList = detailList;
        this.rateList = rateList;
        this.product_items=product_items;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return detailList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.listview_layout, null);
        TextView detail = (TextView) view.findViewById(R.id.detail);
        TextView rate=(TextView)view.findViewById( R.id.rate);
        ImageView icon = (ImageView) view.findViewById(R.id.product_image);
        detail.setText(detailList[i]);
        rate.setText(rateList[i]);
        icon.setImageResource(product_items[i]);
        //icon.setImageResource(flags[i]);
        return view;
    }


}

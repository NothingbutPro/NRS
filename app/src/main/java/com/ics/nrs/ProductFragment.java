package com.ics.nrs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.android.nrs.R;
import com.ics.nrs.NestedTab.ROProduct;
import com.ics.nrs.NestedTab.SparePart;


public class ProductFragment extends Fragment  implements ROProduct.OnFragmentInteractionListener,SparePart.OnFragmentInteractionListener{


    String[] details = new String[] {
            "Refrigerator",
            "Cooler",
            "Washing Machine",
            "Television",
            "Fan",
            "AC",
            "Cooler",
            "Refrigerator",
            "Fan",
            "Television"
    };

    String[] rate = new String[]{
            "Rs.400",
            "Rs.500",
            "Rs.600",
            "Rs.400",
            "Rs.300",
            "Rs.400",
            "Rs.200",
            "Rs.400",
            "Rs.300",
            "Rs.400"
    };

    int product_items[]={R.drawable.refrige,
            R.drawable.cooler,
            R.drawable.washing_machine
            ,R.drawable.tevision,
            R.drawable.fan,
            R.drawable.ac,
            R.drawable.cooler,
            R.drawable.refrige,
            R.drawable.fan,
            R.drawable.tevision
    };


    GridView listView;
    ImageView ro_product_image,spare_product_image;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProductFragment() {
        // Required empty public constructor
    }


    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product, container, false);

        ro_product_image=(ImageView)view.findViewById(R.id.ro_product_image);
        ro_product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(),ProductRo.class);
                startActivity(intent);

            }
        });



        spare_product_image=(ImageView)view.findViewById(R.id.spare_product_image);
        spare_product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(),SpareProduct.class);
                startActivity(intent);

            }
        });

       /* viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setIcon(R.drawable.sparepart);*/

       /* listView = (GridView) view. findViewById(R.id.listView);
        ProductAdapter customAdapter = new ProductAdapter(getContext(), details, rate,product_items);
        listView.setAdapter(customAdapter);*/


        return view;
    }


  /*  private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new ROProduct(), "RO Product");
        adapter.addFragment(new SparePart(), "Spare Part");
        viewPager.setAdapter(adapter);
    }*/
    @Override
    public void onFragmentInteraction(Uri uri) {

    }


  /*  class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }*/


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

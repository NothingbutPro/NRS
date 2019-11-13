package com.ics.nrs;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.nrs.R;
import com.ics.nrs.SharedPreference.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductFragment.OnFragmentInteractionListener, ComplaintFragment.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView person, feedback_menu;
    AppPreference sessionManagement;
    private ImageView logout;
    TextView username_dashboard;
    String UsernameDashboard, EmployeeDashboard;
    private String strMobile;
    AppPreference logoutSession;
    private SessionManager manager;
    private TextView txtUserName;
    private String strOtp_Check;
    int fragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = new SessionManager(this);
        UsernameDashboard = manager.getUsername();
        strMobile = manager.getMobile();

        logout = (ImageView) findViewById(R.id.logout);
        txtUserName = findViewById(R.id.username_dashboard);
//        txtUserName.setText(UsernameDashboard);

        fragmentId = getIntent().getIntExtra("FRAGMENT_ID", 1);

        // Inside OnCreate()

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(MainActivity.this, "Logout Successful", Toast.LENGTH_LONG).show();
                manager.logoutUser();
                Intent in = new Intent(MainActivity.this, Login.class);
                startActivity(in);
                //  logout.setVisibility(View.GONE);
                finish();

               /* logoutSession.custlogoutSession();
                finish();*/
            }
        });


//        username_dashboard.setText(UsernameDashboard);
//        username_dashboard.setText(EmployeeDashboard);

        /*feedback_menu=(ImageView)findViewById(R.id.feedback_menu);
        feedback_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(MainActivity.this, feedback_menu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent=new Intent(MainActivity.this,Feedback.class);
                startActivity(intent);
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }

            });
*/
        person = (ImageView) findViewById(R.id.person);
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // go_next();

                Intent intent=new Intent(MainActivity.this,Login.class);
                startActivity(intent);
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


      /*  if (manager.isLoggedIn()){
            viewPager.setCurrentItem(1);

        }else {

        }*/
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProductFragment(), "Product");
        adapter.addFragment(new ComplaintFragment(), "Complaint");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void go_next() {

        if (!AppPreference.getComplaintId(this).equalsIgnoreCase("")) {
            Intent intent=new Intent(this, ComplaintList.class);
            intent.putExtra("Main","1");
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }

       /* sessionManagement.checkLogin();
        finish();*/
    }

    /* public void go_next_cust() {
         sessionManagement.customercheckLogin();
         finish();
     }
 */
    class ViewPagerAdapter extends FragmentPagerAdapter {
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
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //  moveTaskToBack(true);


        System.exit(0);
        finish();
    }

}

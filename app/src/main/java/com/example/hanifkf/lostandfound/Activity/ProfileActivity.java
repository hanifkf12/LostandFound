package com.example.hanifkf.lostandfound.Activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hanifkf.lostandfound.Fragment.ProfileFragment;
import com.example.hanifkf.lostandfound.Fragment.RecentFragment;
import com.example.hanifkf.lostandfound.R;
import com.example.hanifkf.lostandfound.Utils.BottomNavigationViewHelper;
import com.example.hanifkf.lostandfound.Utils.SectionPagesAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private Context mContext=ProfileActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: setting up Profile");
        setupBottomNavigationView();
        setupViewPager();
    }

    //SetupViewPager
    private void setupViewPager(){
        SectionPagesAdapter adapter=new SectionPagesAdapter(getSupportFragmentManager());
        adapter.addFragment(new RecentFragment());
        adapter.addFragment(new ProfileFragment());
        ViewPager viewPager=(ViewPager)findViewById(R.id.content_profile);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout=(TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.mipmap.ic_recent);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_profilku);
    }

    //Bottom Navigation Setup
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationVIew");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_view);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext,this,bottomNavigationViewEx);
        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(4);
        menuItem.setChecked(true);
    }

}

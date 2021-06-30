package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.codepath.apps.restclienttemplate.adapters.SampleFragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ProfileDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);


        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpagerDetail);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                ProfileDetailActivity.this));


        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs_detail);
        tabLayout.setupWithViewPager(viewPager);
    }
}

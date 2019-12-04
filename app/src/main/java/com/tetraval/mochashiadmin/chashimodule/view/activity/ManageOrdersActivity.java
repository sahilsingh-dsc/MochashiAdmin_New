package com.tetraval.mochashiadmin.chashimodule.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.tetraval.mochashiadmin.R;
import com.tetraval.mochashiadmin.chashimodule.view.fragment.ActiveChashiFragment;
import com.tetraval.mochashiadmin.chashimodule.view.fragment.DeliveryUnassignedFragment;
import com.tetraval.mochashiadmin.chashimodule.view.fragment.InactiveChashiFragment;
import com.tetraval.mochashiadmin.chashimodule.view.fragment.ServedOrdersFragment;
import com.tetraval.mochashiadmin.chashimodule.view.fragment.UnapprovedOrderFragment;

public class ManageOrdersActivity extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Need Approval", UnapprovedOrderFragment.class)
                .add("Need Delivery Channel", DeliveryUnassignedFragment.class)
                .add("Served Orders", ServedOrdersFragment.class)
                .create());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        setOrderTabSelected(0);

    }

    public void setOrderTabSelected(int i){
        viewPager.setCurrentItem(i);
    }

}

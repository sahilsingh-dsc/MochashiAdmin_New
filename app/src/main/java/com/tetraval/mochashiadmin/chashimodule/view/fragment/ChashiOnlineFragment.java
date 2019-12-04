package com.tetraval.mochashiadmin.chashimodule.view.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tetraval.mochashiadmin.R;
import com.tetraval.mochashiadmin.chashimodule.view.activity.ManageCategoryActivity;
import com.tetraval.mochashiadmin.chashimodule.view.activity.ManageChashiActivity;
import com.tetraval.mochashiadmin.chashimodule.view.activity.ManageOrdersActivity;

public class ChashiOnlineFragment extends Fragment implements View.OnClickListener {

    private TextView txtManageCategory;
    private TextView txtManageChashi;
    private TextView txtManageOrders;

    public ChashiOnlineFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chashi_online, container, false);

        txtManageCategory = view.findViewById(R.id.txtManageCategory);
        txtManageCategory.setOnClickListener(this);
        txtManageChashi = view.findViewById(R.id.txtManageChashi);
        txtManageChashi.setOnClickListener(this);
        txtManageOrders = view.findViewById(R.id.txtManageOrders);
        txtManageOrders.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == txtManageCategory){
            changeIntent(new Intent(getContext(), ManageCategoryActivity.class));
        }
        if (view == txtManageChashi){
            changeIntent(new Intent(getContext(), ManageChashiActivity.class));
        }
        if (view == txtManageOrders){
            changeIntent(new Intent(getContext(), ManageOrdersActivity.class));
        }
    }

    private void changeIntent(Intent intent){
        startActivity(intent);
    }
}

package com.tetraval.mochashiadmin.chashimodule.view.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tetraval.mochashiadmin.R;
import com.tetraval.mochashiadmin.masterdata.ShippingChargeActivity;

public class MasterDataFragment extends Fragment implements View.OnClickListener {

    private TextView txtShippingCharge;

    public MasterDataFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_master_data, container, false);

        txtShippingCharge = view.findViewById(R.id.txtShippingCharge);
        txtShippingCharge.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == txtShippingCharge){
            changeIntent(new Intent(getContext(), ShippingChargeActivity.class));
        }
    }

    private void changeIntent(Intent intent){
        startActivity(intent);
    }


}

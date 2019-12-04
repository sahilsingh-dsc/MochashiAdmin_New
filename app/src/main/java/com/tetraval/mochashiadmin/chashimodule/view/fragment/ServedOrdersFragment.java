package com.tetraval.mochashiadmin.chashimodule.view.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetraval.mochashiadmin.R;
import com.tetraval.mochashiadmin.chashimodule.model.SOrderModel;
import com.tetraval.mochashiadmin.chashimodule.view.adapter.SOrderAdapter;

import java.util.ArrayList;
import java.util.List;

public class ServedOrdersFragment extends Fragment {

    RecyclerView recyclerOrders;
    List<SOrderModel> sOrderModelList;
    SOrderAdapter sOrderAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    TextView txtNoOrders;

    public ServedOrdersFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_served_orders, container, false);

        db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        txtNoOrders = view.findViewById(R.id.txtNoOrders);
        recyclerOrders = view.findViewById(R.id.recyclerOrders);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        sOrderModelList = new ArrayList<>();
        sOrderModelList.clear();

        progressDialog.show();
        fetchOrders();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchOrders();
    }

    private void fetchOrders(){

        Query queryOrders = db.collection("chashi_orders");
        queryOrders.whereEqualTo("o_status", "Served")
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (!task.getResult().isEmpty()){
                        txtNoOrders.setVisibility(View.INVISIBLE);
                        sOrderModelList.clear();
                        for (DocumentSnapshot document : task.getResult()){
                            SOrderModel sOrderModel = new SOrderModel();
                            sOrderModel.setO_timestamp(document.getString("o_timestamp"));
                            sOrderModel.setO_uid(document.getString("o_uid"));
                            sOrderModel.setO_p_uid(document.getString("o_p_uid"));
                            sOrderModel.setO_p_category(document.getString("o_p_category"));
                            sOrderModel.setO_customer_uid(document.getString("o_customer_uid"));
                            sOrderModel.setO_customer_name(document.getString("o_customer_name"));
                            sOrderModel.setO_customer_address(document.getString("o_customer_address"));
                            sOrderModel.setO_chashi_uid(document.getString("o_chashi_uid"));
                            sOrderModel.setO_chashi_name(document.getString("o_chashi_name"));
                            sOrderModel.setO_chashi_photo(document.getString("o_chashi_photo"));
                            sOrderModel.setO_chashi_address(document.getString("o_chashi_address"));
                            sOrderModel.setO_chashi_rating(document.getString("o_chashi_rating"));
                            sOrderModel.setO_rate(document.getString("o_rate"));
                            sOrderModel.setO_quantity(document.getString("o_quantity"));
                            sOrderModel.setO_shipping(document.getString("o_shipping"));
                            sOrderModel.setO_total(document.getString("o_total"));
                            sOrderModel.setO_homedelivery(document.getString("o_homedelivery"));
                            sOrderModel.setO_pickup(document.getString("o_pickup"));
                            sOrderModel.setO_status(document.getString("o_status"));
                            sOrderModel.setO_unit(document.getString("o_unit"));
                            sOrderModel.setO_lat(document.getString("o_lat"));
                            sOrderModel.setO_long(document.getString("o_long"));
                            sOrderModelList.add(sOrderModel);
                        }
                        sOrderAdapter = new SOrderAdapter(getContext(), sOrderModelList);
                        recyclerOrders.setAdapter(sOrderAdapter);
                        sOrderAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    } else {
                        txtNoOrders.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }
                }else {
                    Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });

    }

}

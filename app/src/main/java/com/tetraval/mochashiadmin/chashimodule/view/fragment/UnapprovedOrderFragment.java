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
import com.tetraval.mochashiadmin.chashimodule.model.NAOrderModel;
import com.tetraval.mochashiadmin.chashimodule.view.adapter.NAAdapter;

import java.util.ArrayList;
import java.util.List;

public class UnapprovedOrderFragment extends Fragment {

    RecyclerView recyclerOrders;
    List<NAOrderModel> naOrderModelList;
    NAAdapter naAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    TextView txtNoOrders;

    public UnapprovedOrderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unapproved_order, container, false);

        db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        recyclerOrders = view.findViewById(R.id.recyclerOrders);
        txtNoOrders = view.findViewById(R.id.txtNoOrders);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        naOrderModelList = new ArrayList<>();
        naOrderModelList.clear();

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
        queryOrders.whereEqualTo("o_status", "Pending")
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (!task.getResult().isEmpty()){
                        txtNoOrders.setVisibility(View.INVISIBLE);
                        naOrderModelList.clear();
                        for (DocumentSnapshot document : task.getResult()){
                            NAOrderModel naOrderModel = new NAOrderModel();
                            naOrderModel.setO_timestamp(document.getString("o_timestamp"));
                            naOrderModel.setO_uid(document.getString("o_uid"));
                            naOrderModel.setO_p_uid(document.getString("o_p_uid"));
                            naOrderModel.setO_p_category(document.getString("o_p_category"));
                            naOrderModel.setO_customer_uid(document.getString("o_customer_uid"));
                            naOrderModel.setO_customer_name(document.getString("o_customer_name"));
                            naOrderModel.setO_customer_address(document.getString("o_customer_address"));
                            naOrderModel.setO_chashi_uid(document.getString("o_chashi_uid"));
                            naOrderModel.setO_chashi_name(document.getString("o_chashi_name"));
                            naOrderModel.setO_chashi_photo(document.getString("o_chashi_photo"));
                            naOrderModel.setO_chashi_address(document.getString("o_chashi_address"));
                            naOrderModel.setO_chashi_rating(document.getString("o_chashi_rating"));
                            naOrderModel.setO_rate(document.getString("o_rate"));
                            naOrderModel.setO_quantity(document.getString("o_quantity"));
                            naOrderModel.setO_shipping(document.getString("o_shipping"));
                            naOrderModel.setO_total(document.getString("o_total"));
                            naOrderModel.setO_homedelivery(document.getString("o_homedelivery"));
                            naOrderModel.setO_pickup(document.getString("o_pickup"));
                            naOrderModel.setO_status(document.getString("o_status"));
                            naOrderModel.setO_unit(document.getString("o_unit"));
                            naOrderModel.setO_lat(document.getString("o_lat"));
                            naOrderModel.setO_long(document.getString("o_long"));
                            naOrderModelList.add(naOrderModel);
                        }
                        naAdapter = new NAAdapter(getContext(), naOrderModelList);
                        recyclerOrders.setAdapter(naAdapter);
                        naAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }else {
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

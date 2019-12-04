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
import com.tetraval.mochashiadmin.chashimodule.model.NDAOrderModel;
import com.tetraval.mochashiadmin.chashimodule.view.adapter.NDAAdapter;

import java.util.ArrayList;
import java.util.List;

public class DeliveryUnassignedFragment extends Fragment {

    RecyclerView recyclerOrders;
    List<NDAOrderModel> ndaOrderModelList;
    NDAAdapter ndaAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    TextView txtNoOrders;

    public DeliveryUnassignedFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_unassigned, container, false);

        db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        txtNoOrders = view.findViewById(R.id.txtNoOrders);
        recyclerOrders = view.findViewById(R.id.recyclerOrders);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        ndaOrderModelList = new ArrayList<>();
        ndaOrderModelList.clear();

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
        queryOrders.whereEqualTo("o_status", "Unassigned")
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (!task.getResult().isEmpty()){
                        txtNoOrders.setVisibility(View.INVISIBLE);
                        ndaOrderModelList.clear();
                        for (DocumentSnapshot document : task.getResult()){
                            NDAOrderModel ndaOrderModel = new NDAOrderModel();
                            ndaOrderModel.setO_timestamp(document.getString("o_timestamp"));
                            ndaOrderModel.setO_uid(document.getString("o_uid"));
                            ndaOrderModel.setO_p_uid(document.getString("o_p_uid"));
                            ndaOrderModel.setO_p_category(document.getString("o_p_category"));
                            ndaOrderModel.setO_customer_uid(document.getString("o_customer_uid"));
                            ndaOrderModel.setO_customer_name(document.getString("o_customer_name"));
                            ndaOrderModel.setO_customer_address(document.getString("o_customer_address"));
                            ndaOrderModel.setO_chashi_uid(document.getString("o_chashi_uid"));
                            ndaOrderModel.setO_chashi_name(document.getString("o_chashi_name"));
                            ndaOrderModel.setO_chashi_photo(document.getString("o_chashi_photo"));
                            ndaOrderModel.setO_chashi_address(document.getString("o_chashi_address"));
                            ndaOrderModel.setO_chashi_rating(document.getString("o_chashi_rating"));
                            ndaOrderModel.setO_rate(document.getString("o_rate"));
                            ndaOrderModel.setO_quantity(document.getString("o_quantity"));
                            ndaOrderModel.setO_shipping(document.getString("o_shipping"));
                            ndaOrderModel.setO_total(document.getString("o_total"));
                            ndaOrderModel.setO_homedelivery(document.getString("o_homedelivery"));
                            ndaOrderModel.setO_pickup(document.getString("o_pickup"));
                            ndaOrderModel.setO_status(document.getString("o_status"));
                            ndaOrderModel.setO_unit(document.getString("o_unit"));
                            ndaOrderModel.setO_lat(document.getString("o_lat"));
                            ndaOrderModel.setO_long(document.getString("o_long"));
                            ndaOrderModelList.add(ndaOrderModel);
                        }
                        ndaAdapter = new NDAAdapter(getContext(), ndaOrderModelList);
                        recyclerOrders.setAdapter(ndaAdapter);
                        ndaAdapter.notifyDataSetChanged();
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

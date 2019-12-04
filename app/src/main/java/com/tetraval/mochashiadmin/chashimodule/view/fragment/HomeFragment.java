package com.tetraval.mochashiadmin.chashimodule.view.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetraval.mochashiadmin.R;
import com.tetraval.mochashiadmin.chashimodule.model.InactiveChashiModel;
import com.tetraval.mochashiadmin.chashimodule.model.NAOrderModel;
import com.tetraval.mochashiadmin.chashimodule.view.activity.ManageCategoryActivity;
import com.tetraval.mochashiadmin.chashimodule.view.activity.ManageChashiActivity;
import com.tetraval.mochashiadmin.chashimodule.view.activity.ManageOrdersActivity;
import com.tetraval.mochashiadmin.chashimodule.view.adapter.InactiveChashiAdapter;
import com.tetraval.mochashiadmin.chashimodule.view.adapter.NAAdapter;

public class HomeFragment extends Fragment {

    LinearLayout lvPendingOrder, lvPendingChashi;
    TextView txtPendingOrder, txtPendingChashi;
    FirebaseFirestore db;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        db = FirebaseFirestore.getInstance();

        lvPendingOrder = view.findViewById(R.id.lvPendingOrder);
        lvPendingChashi = view.findViewById(R.id.lvPendingChashi);
        txtPendingOrder = view.findViewById(R.id.txtPendingOrder);
        txtPendingChashi = view.findViewById(R.id.txtPendingChashi);

        lvPendingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ManageOrdersActivity.class));
            }
        });

        lvPendingChashi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ManageChashiActivity.class));
            }
        });

        fetchPendingOrder();
        fetchPendingChashi();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchPendingOrder();
        fetchPendingChashi();
    }

    private void fetchPendingOrder() {

        Query queryOrders = db.collection("chashi_orders");
        queryOrders.whereEqualTo("o_status", "Pending")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int pending_order_counter = 0;
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        pending_order_counter = pending_order_counter + 1;
                    }
                    txtPendingOrder.setText("" + pending_order_counter);
                }
            }
        });

    }

    private void fetchPendingChashi() {
        Query query = db.collection("vendor_profiles");
        query.whereEqualTo("p_active", "inactive").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int pending_chashi_counter = 0;
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        pending_chashi_counter = pending_chashi_counter + 1;
                    }
                    txtPendingChashi.setText("" + pending_chashi_counter);
                }
            }
        });
    }


}

package com.tetraval.mochashiadmin.chashimodule.view.fragment;


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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetraval.mochashiadmin.R;
import com.tetraval.mochashiadmin.chashimodule.model.InactiveChashiModel;
import com.tetraval.mochashiadmin.chashimodule.view.adapter.InactiveChashiAdapter;

import java.util.ArrayList;
import java.util.List;

public class InactiveChashiFragment extends Fragment {


    RecyclerView recyclerInactiveChashi;
    TextView txtNoInactiveChashi;
    InactiveChashiAdapter inactiveChashiAdapter;
    List<InactiveChashiModel> inactiveChashiModelList;
    FirebaseFirestore db;

    public InactiveChashiFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_vendor, container, false);

        recyclerInactiveChashi = view.findViewById(R.id.recyclerInactiveChashi);
        recyclerInactiveChashi.setLayoutManager(new LinearLayoutManager(getContext()));
        txtNoInactiveChashi = view.findViewById(R.id.txtNoInactiveChashi);

        inactiveChashiModelList = new ArrayList<>();
        inactiveChashiModelList.clear();

        db = FirebaseFirestore.getInstance();

        fetchInactiveChashi();

        return view;
    }

    private void fetchInactiveChashi(){
        Query query = db.collection("vendor_profiles");
        query.whereEqualTo("p_active", "inactive").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    if (!task.getResult().isEmpty()){
                        txtNoInactiveChashi.setVisibility(View.INVISIBLE);
                        for (DocumentSnapshot document : task.getResult()){

                            InactiveChashiModel inactiveChashiModel = new InactiveChashiModel();
                            inactiveChashiModel.setP_uid(document.getString("p_uid"));
                            inactiveChashiModel.setP_fname(document.getString("p_fname"));
                            inactiveChashiModel.setP_lname(document.getString("p_lname"));
                            inactiveChashiModel.setP_imagel(document.getString("p_image"));
                            inactiveChashiModel.setP_address(document.getString("p_address"));
                            inactiveChashiModel.setP_lat(String.valueOf(document.getDouble("p_lat")));
                            inactiveChashiModel.setP_long(String.valueOf(document.getDouble("p_long")));
                            inactiveChashiModel.setP_email(document.getString("p_email"));
                            inactiveChashiModel.setP_status(document.getString("p_status"));

                            inactiveChashiModelList.add(inactiveChashiModel);

                        }

                        inactiveChashiAdapter = new InactiveChashiAdapter(getContext(), inactiveChashiModelList);
                        recyclerInactiveChashi.setAdapter(inactiveChashiAdapter);

                    } else {
                        txtNoInactiveChashi.setVisibility(View.VISIBLE);
                    }

                }else {
                    Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

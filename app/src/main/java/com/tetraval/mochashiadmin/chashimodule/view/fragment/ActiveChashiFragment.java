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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetraval.mochashiadmin.R;
import com.tetraval.mochashiadmin.chashimodule.model.ActiveChashiModel;
import com.tetraval.mochashiadmin.chashimodule.view.adapter.ActiveChashiAdapter;
import com.tetraval.mochashiadmin.chashimodule.view.adapter.InactiveChashiAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActiveChashiFragment extends Fragment {

    RecyclerView recyclerActiveChashi;
    TextView txtNoActiveChashi;
    ActiveChashiAdapter activeChashiAdapter;
    List<ActiveChashiModel> activeChashiModelList;
    FirebaseFirestore db;

    public ActiveChashiFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_chashi, container, false);

        recyclerActiveChashi = view.findViewById(R.id.recyclerActiveChashi);
        recyclerActiveChashi.setLayoutManager(new LinearLayoutManager(getContext()));
        txtNoActiveChashi = view.findViewById(R.id.txtNoActiveChashi);

        activeChashiModelList = new ArrayList<>();
        activeChashiModelList.clear();

        db = FirebaseFirestore.getInstance();

        fetchInactiveChashi();

        return view;
    }

    private void fetchInactiveChashi(){
        Query query = db.collection("vendor_profiles");
        query.whereEqualTo("p_active", "active").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    if (!task.getResult().isEmpty()){
                        txtNoActiveChashi.setVisibility(View.INVISIBLE);
                        for (DocumentSnapshot document : task.getResult()){

                            ActiveChashiModel activeChashiModel = new ActiveChashiModel();
                            activeChashiModel.setP_uid(document.getString("p_uid"));
                            activeChashiModel.setP_fname(document.getString("p_fname"));
                            activeChashiModel.setP_lname(document.getString("p_lname"));
                            activeChashiModel.setP_imagel(document.getString("p_image"));
                            activeChashiModel.setP_address(document.getString("p_address"));
                            activeChashiModel.setP_lat(String.valueOf(document.getDouble("p_lat")));
                            activeChashiModel.setP_long(String.valueOf(document.getDouble("p_long")));
                            activeChashiModel.setP_email(document.getString("p_email"));
                            activeChashiModel.setP_status(document.getString("p_status"));

                            activeChashiModelList.add(activeChashiModel);

                        }

                        activeChashiAdapter = new ActiveChashiAdapter(getContext(), activeChashiModelList);
                        recyclerActiveChashi.setAdapter(activeChashiAdapter);

                    } else {
                        txtNoActiveChashi.setVisibility(View.VISIBLE);
                    }

                }else {
                    Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}

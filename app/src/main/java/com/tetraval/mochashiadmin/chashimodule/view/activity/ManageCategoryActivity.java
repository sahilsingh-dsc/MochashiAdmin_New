package com.tetraval.mochashiadmin.chashimodule.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetraval.mochashiadmin.R;
import com.tetraval.mochashiadmin.chashimodule.model.ChashiCategoryModel;
import com.tetraval.mochashiadmin.chashimodule.view.adapter.ChashiCategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class ManageCategoryActivity extends AppCompatActivity {

    FirebaseFirestore db;
    ProgressDialog progressDialog;
    private RecyclerView recyclerChashiCategory;
    private ChashiCategoryAdapter chashiCategoryAdapter;
    private List<ChashiCategoryModel> chashiCategoryModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        db = FirebaseFirestore.getInstance();

        recyclerChashiCategory = findViewById(R.id.recyclerChashiCategory);
        recyclerChashiCategory.setHasFixedSize(true);
        recyclerChashiCategory.setNestedScrollingEnabled(false);
        recyclerChashiCategory.setLayoutManager(new GridLayoutManager(this, 2));
        chashiCategoryModelList = new ArrayList<>();
        chashiCategoryModelList.clear();

        MaterialButton btnAddCategory = findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddCategoryActivity.class);
                startActivity(intent);
            }
        });

        progressDialog.show();
        fetchChashiCategory();
    }

    public void fetchChashiCategory() {
        Query queryCategories = db.collection("chashi_categories");
        queryCategories.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        chashiCategoryModelList.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                ChashiCategoryModel chashiCategoryModel = new ChashiCategoryModel(
                                        document.getString("c_uid"),
                                        document.getString("c_name"),
                                        document.getString("c_image")
                                );
                                chashiCategoryModelList.add(chashiCategoryModel);
                            }

                        }
                        chashiCategoryAdapter = new ChashiCategoryAdapter(ManageCategoryActivity.this, chashiCategoryModelList);
                        recyclerChashiCategory.setAdapter(chashiCategoryAdapter);
                        progressDialog.dismiss();
                    }
                } else {
                    Toast.makeText(ManageCategoryActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

}

package com.tetraval.mochashiadmin.chashimodule.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetraval.mochashiadmin.R;
import com.tetraval.mochashiadmin.chashimodule.model.ChashiCategoryModel;
import com.tetraval.mochashiadmin.chashimodule.view.activity.EditCategoryActivity;
import com.tetraval.mochashiadmin.chashimodule.view.activity.ManageCategoryActivity;
import com.tetraval.mochashiadmin.chashimodule.view.activity.ManageOrdersActivity;

import java.util.List;

public class ChashiCategoryAdapter extends RecyclerView.Adapter<ChashiCategoryAdapter.ChashiCategoryViewHolder> {

    Context context;
    List<ChashiCategoryModel> chashiCategoryModelList;

    public ChashiCategoryAdapter(Context context, List<ChashiCategoryModel> chashiCategoryModelList) {
        this.context = context;
        this.chashiCategoryModelList = chashiCategoryModelList;
    }

    @NonNull
    @Override
    public ChashiCategoryAdapter.ChashiCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chashi_category_list_item, parent, false);
        return new ChashiCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChashiCategoryAdapter.ChashiCategoryViewHolder holder, final int position) {
        final ChashiCategoryModel chashiCategoryModel = chashiCategoryModelList.get(position);
        Glide.with(context).load(chashiCategoryModel.getC_image()).placeholder(R.drawable.noimage).into(holder.imgCategoryImage);
        holder.txtCategoryName.setText(chashiCategoryModel.getC_name());
//        holder.lvChashiCategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putString("c_uid", chashiCategoryModel.getC_uid());
//                bundle.putString("c_name", chashiCategoryModel.getC_name());
//                Intent intent = new Intent(context, ChashiActivity.class);
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//            }
//        });

        holder.lvChashiCategory.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final LayoutInflater factory = LayoutInflater.from(context);
                final View deleteDialogView = factory.inflate(R.layout.manage_category_alert, null);
                LinearLayout lvEdit, lvDelete;
                TextView txtEdit, txtDelete;
                lvEdit = deleteDialogView.findViewById(R.id.lvEdit);
                lvDelete = deleteDialogView.findViewById(R.id.lvDelete);
                txtEdit = deleteDialogView.findViewById(R.id.txtEdit);
                txtDelete = deleteDialogView.findViewById(R.id.txtDelete);
                txtEdit.setText("Edit "+chashiCategoryModel.getC_name());
                txtDelete.setText("Delete "+chashiCategoryModel.getC_name());

                final AlertDialog deleteDialog = new AlertDialog.Builder(context).create();

                lvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteDialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString("cat_id", chashiCategoryModel.getC_uid());
                        bundle.putString("cat_name", chashiCategoryModel.getC_name());
                        bundle.putString("cat_image", chashiCategoryModel.getC_image());
                        Intent intent = new Intent(context, EditCategoryActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });

                lvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference queryCategories = db.collection("chashi_categories");
                        DocumentReference documentReference = queryCategories.document(chashiCategoryModel.getC_uid());
                        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(context, chashiCategoryModel.getC_name()+ " Deleted!", Toast.LENGTH_SHORT).show();
                                    context.startActivity(new Intent(context, ManageCategoryActivity.class));
                                    ((Activity)context).finish();
                                    deleteDialog.dismiss();
                                }else {
                                    Toast.makeText(context, "Database Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                });


                deleteDialog.setView(deleteDialogView);
                deleteDialog.show();

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return chashiCategoryModelList.size();
    }

    public class ChashiCategoryViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lvChashiCategory;
        ImageView imgCategoryImage;
        TextView txtCategoryName;

        public ChashiCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            lvChashiCategory = itemView.findViewById(R.id.lvChashiCategory);
            imgCategoryImage = itemView.findViewById(R.id.imgCategoryImage);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);

        }
    }
}

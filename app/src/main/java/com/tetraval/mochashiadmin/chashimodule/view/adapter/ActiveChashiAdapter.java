package com.tetraval.mochashiadmin.chashimodule.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetraval.mochashiadmin.R;
import com.tetraval.mochashiadmin.chashimodule.model.ActiveChashiModel;
import com.tetraval.mochashiadmin.chashimodule.view.activity.ManageChashiActivity;
import com.tetraval.mochashiadmin.chashimodule.view.activity.MapsActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveChashiAdapter extends RecyclerView.Adapter<ActiveChashiAdapter.InactiveViewHolder> {

    Context context;
    List<ActiveChashiModel> activeChashiModelList;

    public ActiveChashiAdapter(Context context, List<ActiveChashiModel> activeChashiModelList) {
        this.context = context;
        this.activeChashiModelList = activeChashiModelList;
    }

    @NonNull
    @Override
    public ActiveChashiAdapter.InactiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_chashi_list_item, parent, false);
        return new InactiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ActiveChashiAdapter.InactiveViewHolder holder, int position) {
        final ActiveChashiModel activeChashiModel = activeChashiModelList.get(position);
        Glide.with(context).load(activeChashiModel.getP_imagel()).placeholder(R.drawable.noimage).into(holder.imgChashiPhoto);
        holder.txtChashiName.setText(activeChashiModel.getP_fname()+" "+activeChashiModel.getP_lname());
        holder.txtChashiEmail.setText(activeChashiModel.getP_email());
        holder.txtChashiAddress.setText(activeChashiModel.getP_address());
        holder.btnInActivateChashi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionReference = db.collection("vendor_profiles");
                DocumentReference documentReference = collectionReference.document(activeChashiModel.getP_uid());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("p_active", "inactive");
                documentReference.update(map);
                Toast.makeText(context, "Chashi Inactivated", Toast.LENGTH_SHORT).show();
                holder.btnInActivateChashi.setEnabled(false);
                context.startActivity(new Intent(context, ManageChashiActivity.class));
                ((Activity)context).finish();
            }
        });

        holder.imgChashiMapLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("c_name", activeChashiModel.getP_fname()+" "+activeChashiModel.getP_lname());
                bundle.putString("c_address", activeChashiModel.getP_address());
                bundle.putString("c_lat", activeChashiModel.getP_lat());
                bundle.putString("c_long", activeChashiModel.getP_long());
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return activeChashiModelList.size();
    }

    public class InactiveViewHolder extends RecyclerView.ViewHolder {

        ImageView imgChashiPhoto, imgChashiMapLocation;
        TextView txtChashiName, txtChashiEmail, txtChashiAddress;
        MaterialButton btnInActivateChashi;

        public InactiveViewHolder(@NonNull View itemView) {
            super(itemView);

            imgChashiPhoto = itemView.findViewById(R.id.imgChashiPhoto);
            imgChashiMapLocation = itemView.findViewById(R.id.imgChashiMapLocation);
            txtChashiName = itemView.findViewById(R.id.txtChashiName);
            txtChashiEmail = itemView.findViewById(R.id.txtChashiEmail);
            txtChashiAddress = itemView.findViewById(R.id.txtChashiAddress);
            btnInActivateChashi = itemView.findViewById(R.id.btnInActivateChashi);

        }
    }
}

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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetraval.mochashiadmin.R;
import com.tetraval.mochashiadmin.chashimodule.model.NDAOrderModel;
import com.tetraval.mochashiadmin.chashimodule.view.activity.ManageOrdersActivity;
import com.tetraval.mochashiadmin.chashimodule.view.activity.MapsActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NDAAdapter extends RecyclerView.Adapter<NDAAdapter.NDAViewHolder> {

    Context context;
    List<NDAOrderModel> ndaOrderModelList;

    public NDAAdapter(Context context, List<NDAOrderModel> ndaOrderModelList) {
        this.context = context;
        this.ndaOrderModelList = ndaOrderModelList;
    }

    @NonNull
    @Override
    public NDAAdapter.NDAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.need_delivery_list_item, parent, false);
        return new NDAViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NDAAdapter.NDAViewHolder holder, int position) {
        final NDAOrderModel ndaOrderModel = ndaOrderModelList.get(position);
        holder.imgUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater factory = LayoutInflater.from(context);
                final View deleteDialogView = factory.inflate(R.layout.show_customer_details_alert, null);
                TextView txtCustomerID, txtCustomerName;
                MaterialButton btnShowOnMap;
                txtCustomerID = deleteDialogView.findViewById(R.id.txtCustomerID);
                txtCustomerName = deleteDialogView.findViewById(R.id.txtCustomerName);
                btnShowOnMap = deleteDialogView.findViewById(R.id.btnShowOnMap);
                txtCustomerID.setText(ndaOrderModel.getO_customer_uid());
                txtCustomerName.setText(ndaOrderModel.getO_customer_name());
                btnShowOnMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("c_name", ndaOrderModel.getO_customer_name());
                        bundle.putString("c_address", ndaOrderModel.getO_customer_address());
                        bundle.putString("c_lat", ndaOrderModel.getO_lat());
                        bundle.putString("c_long", ndaOrderModel.getO_long());
                        Intent intent = new Intent(context, MapsActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                final AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                deleteDialog.setView(deleteDialogView);
                deleteDialog.setTitle("Customer Details");
                deleteDialog.setMessage("Customer Address :\n\n"+ndaOrderModel.getO_customer_address());
                deleteDialog.show();

            }
        });
        holder.txtOrderId.setText(ndaOrderModel.getO_uid());
        holder.txtOrderDate.setText(ndaOrderModel.getO_timestamp());
        holder.txtOrderCategory.setText(ndaOrderModel.getO_p_category());
        holder.txtOrderAmount.setText("₹"+ndaOrderModel.getO_total());
        holder.txtOrderQuantity.setText(ndaOrderModel.getO_quantity() +" "+ndaOrderModel.getO_unit());
        if (ndaOrderModel.getO_homedelivery().equals("No")){
            if (ndaOrderModel.getO_pickup().equals("Yes")){
                holder.txtPickupBy.setText("Customer");
                holder.txtPickupBy.setText("Customer");
            } else if (ndaOrderModel.getO_pickup().equals("No")){
                holder.txtPickupBy.setText("Delivery Man");
            }
        } else {
            holder.txtPickupBy.setText("Chashi");
        }

        holder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionReference = db.collection("chashi_orders");
                DocumentReference documentReference = collectionReference.document(ndaOrderModel.getO_uid());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("o_status", "InTransit");
                documentReference.update(map);
                Toast.makeText(context, "Delivery Channel Assigned!", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, ManageOrdersActivity.class));
                ((ManageOrdersActivity)context).setOrderTabSelected(1);
                ((Activity)context).finish();
            }
        });

    }


    @Override
    public int getItemCount() {
        return ndaOrderModelList.size();
    }

    public class NDAViewHolder extends RecyclerView.ViewHolder {

        ImageView imgUserDetails;
        TextView txtOrderCategory, txtOrderQuantity, txtOrderAmount, txtPickupBy, txtOrderId, txtOrderDate;
        MaterialButton btnApprove;

        public NDAViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUserDetails = itemView.findViewById(R.id.imgUserDetails);
            txtOrderQuantity = itemView.findViewById(R.id.txtOrderQuantity);
            txtOrderCategory = itemView.findViewById(R.id.txtOrderCategory);
            txtOrderAmount = itemView.findViewById(R.id.txtOrderAmount);
            txtPickupBy = itemView.findViewById(R.id.txtPickupBy);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            btnApprove = itemView.findViewById(R.id.btnApprove);


        }
    }
}

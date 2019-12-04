package com.tetraval.mochashiadmin.chashimodule.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.tetraval.mochashiadmin.R;
import com.tetraval.mochashiadmin.chashimodule.model.SOrderModel;
import com.tetraval.mochashiadmin.chashimodule.view.activity.MapsActivity;

import java.util.List;

public class SOrderAdapter extends RecyclerView.Adapter<SOrderAdapter.SOrderViewHolder> {

    Context context;
    List<SOrderModel> sOrderModelList;

    public SOrderAdapter(Context context, List<SOrderModel> sOrderModelList) {
        this.context = context;
        this.sOrderModelList = sOrderModelList;
    }

    @NonNull
    @Override
    public SOrderAdapter.SOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.server_order_list_item, parent, false);
        return new SOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SOrderAdapter.SOrderViewHolder holder, int position) {
        final SOrderModel sOrderModel = sOrderModelList.get(position);
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
                txtCustomerID.setText(sOrderModel.getO_customer_uid());
                txtCustomerName.setText(sOrderModel.getO_customer_name());
                btnShowOnMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("c_name", sOrderModel.getO_customer_name());
                        bundle.putString("c_address", sOrderModel.getO_customer_address());
                        bundle.putString("c_lat", sOrderModel.getO_lat());
                        bundle.putString("c_long", sOrderModel.getO_long());
                        Intent intent = new Intent(context, MapsActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                final AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                deleteDialog.setView(deleteDialogView);
                deleteDialog.setTitle("Customer Details");
                deleteDialog.setMessage("Customer Address :\n\n"+sOrderModel.getO_customer_address());
                deleteDialog.show();

            }
        });
        holder.txtOrderId.setText(sOrderModel.getO_uid());
        holder.txtOrderDate.setText(sOrderModel.getO_timestamp());
        holder.txtOrderCategory.setText(sOrderModel.getO_p_category());
        holder.txtOrderAmount.setText("₹"+sOrderModel.getO_total());
        holder.txtOrderQuantity.setText(sOrderModel.getO_quantity() +" "+sOrderModel.getO_unit());
        if (sOrderModel.getO_homedelivery().equals("No")){
            if (sOrderModel.getO_pickup().equals("Yes")){
                holder.txtPickupBy.setText("Customer");
                holder.txtPickupBy.setText("Customer");
            } else if (sOrderModel.getO_pickup().equals("No")){
                holder.txtPickupBy.setText("Delivery Man");
            }
        } else {
            holder.txtPickupBy.setText("Chashi");
        }
    }


    @Override
    public int getItemCount() {
        return sOrderModelList.size();
    }

    public class SOrderViewHolder extends RecyclerView.ViewHolder {

        ImageView imgUserDetails;
        TextView txtOrderCategory, txtOrderQuantity, txtOrderAmount, txtPickupBy, txtOrderId, txtOrderDate;

        public SOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUserDetails = itemView.findViewById(R.id.imgUserDetails);
            txtOrderQuantity = itemView.findViewById(R.id.txtOrderQuantity);
            txtOrderCategory = itemView.findViewById(R.id.txtOrderCategory);
            txtOrderAmount = itemView.findViewById(R.id.txtOrderAmount);
            txtPickupBy = itemView.findViewById(R.id.txtPickupBy);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);

        }
    }
}

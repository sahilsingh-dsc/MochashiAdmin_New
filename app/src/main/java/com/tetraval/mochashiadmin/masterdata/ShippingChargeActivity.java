package com.tetraval.mochashiadmin.masterdata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetraval.mochashiadmin.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShippingChargeActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText tiChashiCharge, tiGroceryCharge, tiHaatCharge, tiMinShipping;
    MaterialButton btnEditUpdateCharges;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    String btn_state = "edit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_charge);

        tiChashiCharge = findViewById(R.id.tiChashiCharge);
        tiGroceryCharge = findViewById(R.id.tiGroceryCharge);
        tiHaatCharge = findViewById(R.id.tiHaatCharge);
        tiMinShipping = findViewById(R.id.tiMinShipping);

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        btnEditUpdateCharges = findViewById(R.id.btnEditUpdateCharges);
        btnEditUpdateCharges.setOnClickListener(this);


        progressDialog.show();
        fetchShippingCharges();

    }

    @Override
    public void onClick(View view) {
        if (view == btnEditUpdateCharges){

            if (btn_state.equals("update")){
                updateUI();
                btn_state = "edit";
            } else if (btn_state.equals("edit")){
                editUI();
                btn_state = "update";
            }



        }
    }

    private void fetchShippingCharges(){
        CollectionReference colRef = db.collection("system_constants");
        final DocumentReference docRef = colRef.document("shipping_charges");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    tiChashiCharge.setText(document.getString("chashi_shipping"));
                    tiGroceryCharge.setText(document.getString("grocery_shipping"));
                    tiHaatCharge.setText(document.getString("haat_shipping"));
                    tiMinShipping.setText(document.getString("min_shipping"));
                    progressDialog.dismiss();

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ShippingChargeActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateShippingCharges(String chashi_shipping, String grocery_shipping, String haat_shipping, String min_shipping){
        CollectionReference colRef = db.collection("system_constants");
        DocumentReference docRef = colRef.document("shipping_charges");
        Map<String, Object> map = new HashMap<>();
        map.put("chashi_shipping", chashi_shipping);
        map.put("grocery_shipping", grocery_shipping);
        map.put("haat_shipping", haat_shipping);
        map.put("min_shipping", min_shipping);
        docRef.set(map);
        Toast.makeText(this, "Shipping Charges Updated!", Toast.LENGTH_SHORT).show();
    }

    private void editUI(){
        tiChashiCharge.setEnabled(true);
        tiGroceryCharge.setEnabled(true);
        tiHaatCharge.setEnabled(true);
        tiMinShipping.setEnabled(true);
        btnEditUpdateCharges.setText("Update Charges");
    }

    private void updateUI(){
        tiChashiCharge.setEnabled(false);
        tiGroceryCharge.setEnabled(false);
        tiHaatCharge.setEnabled(false);
        tiMinShipping.setEnabled(false);
        btnEditUpdateCharges.setText("Edit Charges");

        String chashi_shipping = Objects.requireNonNull(tiChashiCharge.getText()).toString();
        String grocery_shipping = Objects.requireNonNull(tiGroceryCharge.getText()).toString();
        String haat_shipping = Objects.requireNonNull(tiHaatCharge.getText()).toString();
        String min_shipping = Objects.requireNonNull(tiMinShipping.getText()).toString();

            if (TextUtils.isEmpty(chashi_shipping)){
                tiChashiCharge.setError("Chashi shipping charge is required");
                return;
            }
            if (TextUtils.isEmpty(grocery_shipping)){
                tiGroceryCharge.setError("Grocery shipping charge is required");
                return;
            }
            if (TextUtils.isEmpty(haat_shipping)){
                tiHaatCharge.setError("Haat shipping charge is required");
                return;
            }
            if (TextUtils.isEmpty(min_shipping)){
                tiMinShipping.setError("Min. Shipping Charge is required");
            }

            updateShippingCharges(chashi_shipping, grocery_shipping, haat_shipping, min_shipping);

    }

}

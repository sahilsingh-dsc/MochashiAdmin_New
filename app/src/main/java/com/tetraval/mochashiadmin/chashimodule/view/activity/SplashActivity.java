package com.tetraval.mochashiadmin.chashimodule.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.tetraval.mochashiadmin.MainActivity;
import com.tetraval.mochashiadmin.R;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences preferences;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SplashActivity.this, ""+firebaseAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SplashActivity.this, "Auth Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                preferences = getSharedPreferences("token", 0);
                if (preferences.getInt("token_state", 0) == 1){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    final SharedPreferences.Editor editor = preferences.edit();
                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        return;
                                    }
                                    String token = task.getResult().getToken();
                                    editor.putString("token_id", token);
                                    editor.putInt("token_state", 1);
                                    editor.apply();
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    CollectionReference collectionReference = db.collection("system_constants");
                                    DocumentReference documentReference = collectionReference.document("admin_constants");
                                    Map<String, String> map = new HashMap<>();
                                    map.put("token_id", token);
                                    documentReference.set(map);
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    Toast.makeText(SplashActivity.this, token, Toast.LENGTH_SHORT).show();
                                }
                            });


                }

            }
        }, 3000);

    }
}

package com.tetraval.mochashiadmin.chashimodule.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tetraval.mochashiadmin.R;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditCategoryActivity extends AppCompatActivity {

    ImageView imgCatIamge;
    TextInputEditText tiCategoryName;
    MaterialButton btnUpdateCategory;
    Toolbar toolbarEditCategory;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    String imgurl;

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "SelectImageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        Bundle bundle = getIntent().getExtras();

        final String cat_id = bundle.getString("cat_id");
        String cat_name= bundle.getString("cat_name");
        imgurl = bundle.getString("cat_image");

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        imgCatIamge = findViewById(R.id.imgCatIamge);
        tiCategoryName = findViewById(R.id.tiCategoryName);
        btnUpdateCategory = findViewById(R.id.btnUpdateCategory);

        toolbarEditCategory = findViewById(R.id.toolbarEditCategory);
        toolbarEditCategory.setTitle(cat_name);
        toolbarEditCategory.setTitleTextColor(Color.WHITE);

        btnUpdateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCategory(cat_id);
            }
        });

        imgCatIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

        handlePermission();

        fetchCategory(cat_name, imgurl);

    }

    private void handlePermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //ask for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    SELECT_PICTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case SELECT_PICTURE:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                        if (showRationale) {
                            //  Show your own message here
                        } else {
                            showSettingsAlert();
                        }
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /* Choose an image from Gallery */
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (resultCode == RESULT_OK) {
                    if (requestCode == SELECT_PICTURE) {
                        // Get the url from data
                        final Uri selectedImageUri = data.getData();
                        if (null != selectedImageUri) {
                            // Get the path from the Uri
                            String path = getPathFromURI(selectedImageUri);
                            Log.i(TAG, "Image Path : " + path);
                            // Set the image in ImageView
                            findViewById(R.id.imgCatIamge).post(new Runnable() {
                                @Override
                                public void run() {
                                    ((ImageView) findViewById(R.id.imgCatIamge)).setImageURI(selectedImageUri);
                                    uploadImage(selectedImageUri);
                                }
                            });

                        }
                    }
                }
            }
        }).start();

    }

    private void uploadImage(Uri filePath) {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        if (filePath != null) {
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("categories/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(EditCategoryActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imgurl = uri.toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditCategoryActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }


    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        openAppSettings(EditCategoryActivity.this);
                    }
                });
        alertDialog.show();
    }

    public static void openAppSettings(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }


    private void fetchCategory(String cat_name, String cat_image){
        tiCategoryName.setText(cat_name);
        Glide.with(getApplicationContext()).load(cat_image).into(imgCatIamge);
    }

    private void editCategory(String cat_id){
        String cat_name = tiCategoryName.getText().toString();
        Toast.makeText(this, cat_id+" :: "+cat_name, Toast.LENGTH_SHORT).show();
        if (!cat_name.isEmpty() && !imgurl.isEmpty()){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("c_uid", cat_id);
            map.put("c_name", cat_name);
            map.put("c_image", imgurl);
            CollectionReference queryCategories = db.collection("chashi_categories");
            queryCategories.document(cat_id).update(map);
            Toast.makeText(this, "Category Edited!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), ManageCategoryActivity.class));
            finish();
        }

    }

}

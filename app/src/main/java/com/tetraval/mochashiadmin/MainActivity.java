package com.tetraval.mochashiadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tetraval.mochashiadmin.chashimodule.view.fragment.ChashiOnlineFragment;
import com.tetraval.mochashiadmin.chashimodule.view.fragment.HomeFragment;
import com.tetraval.mochashiadmin.chashimodule.view.fragment.MasterDataFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView navBottom;
    Toolbar toolbarVendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbarMain = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbarMain);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
        toolbarMain.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        Objects.requireNonNull(toolbarMain.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);

        navBottom = findViewById(R.id.navBottom);
        navBottom.setOnNavigationItemSelectedListener(MainActivity.this);
        navBottom.setSelectedItemId(R.id.menu_home);


    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to Mochashi Admin", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()){

            case R.id.menu_home :
                fragment = new HomeFragment();
                Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
                break;

            case R.id.menu_chashionline :
                fragment = new ChashiOnlineFragment();
                Objects.requireNonNull(getSupportActionBar()).setTitle("Chashi Online");
                break;

            case R.id.menu_masterdata :
                fragment = new MasterDataFragment();
                Objects.requireNonNull(getSupportActionBar()).setTitle("Master Data");
                break;

        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, fragment)
                    .commit();
            return true;
        }

        return false;
    }

}

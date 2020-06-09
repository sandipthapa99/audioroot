package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);


        bottomNav= findViewById(R.id.nav_bottom_id);

        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_id, new HomeFrag()).commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment displayedFrag=null;
            switch (menuItem.getItemId()){
                case R.id.home_id:
                    displayedFrag=new HomeFrag();
                    break;



                case R.id.message_id:
                    displayedFrag=new MessageFrag();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_id, displayedFrag).commit();
            return true;
        }
    };



}

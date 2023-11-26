package com.example.studentmanagement.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.studentmanagement.Fragment.ClassFragment;
import com.example.studentmanagement.Fragment.user.EmployeeFragment;
import com.example.studentmanagement.Fragment.HomeFragment;
import com.example.studentmanagement.Fragment.MyProfileFragment;
import com.example.studentmanagement.Fragment.student.StudentFragment;
import com.example.studentmanagement.Fragment.SubjectFragment;
import com.example.studentmanagement.Models.User;
import com.example.studentmanagement.R;
import com.example.studentmanagement.utils.DatabaseManagerUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{



    private DrawerLayout mDrawerLayout;
    private static  final int FRAGMENT_HOME = 0;
    private static  final int FRAGMENT_CLASS = 3;
    private static  final int FRAGMENT_SUBJECT = 4;
    private static  final int FRAGMENT_STUDENT = 2;
    private static  final int FRAGMENT_EMPLOYEE = 1;
    private static  final int FRAGMENT_MYPROFILE = 5;

    private static  int FRAGMENT_CURRENT = FRAGMENT_HOME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar,
                R.string.open_nav, R.string.close_nav);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        rePlaceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);




    }


    private String generateCustomPushId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        return formatter.format(now).toString();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_home){

            if(FRAGMENT_CURRENT != FRAGMENT_HOME){
                rePlaceFragment(new HomeFragment());
                FRAGMENT_CURRENT = FRAGMENT_HOME;
            }

        }else  if(id == R.id.nav_employee){

            if(FRAGMENT_CURRENT != FRAGMENT_EMPLOYEE){
                rePlaceFragment(new EmployeeFragment());
                FRAGMENT_CURRENT = FRAGMENT_EMPLOYEE;
            }

        }else  if(id == R.id.nav_student){

            if(FRAGMENT_CURRENT != FRAGMENT_STUDENT){
                rePlaceFragment(new StudentFragment());
                FRAGMENT_CURRENT = FRAGMENT_STUDENT;
            }

        }else  if(id == R.id.nav_class){

            if(FRAGMENT_CURRENT != FRAGMENT_CLASS){
                rePlaceFragment(new ClassFragment());
                FRAGMENT_CURRENT = FRAGMENT_CLASS;
            }

        }else  if(id == R.id.nav_subject){

            if(FRAGMENT_CURRENT != FRAGMENT_SUBJECT){
                rePlaceFragment(new SubjectFragment());
                FRAGMENT_CURRENT = FRAGMENT_SUBJECT;
            }

        }else  if(id == R.id.nav_myprofile){

            if(FRAGMENT_CURRENT != FRAGMENT_MYPROFILE){
                rePlaceFragment(new MyProfileFragment());
                FRAGMENT_CURRENT = FRAGMENT_MYPROFILE;
            }

        }else  if(id == R.id.nav_logout){
            UpdateHistoryLogin();
           Toast.makeText(MainActivity.this, "Log Out", Toast.LENGTH_SHORT).show();
            finish();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void UpdateHistoryLogin(){
        DatabaseManagerUser databaseManagerUser = new DatabaseManagerUser();

        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String idHistoryLogin = preferences.getString("id_historylogin", "");

        String userJson = preferences.getString("user", "");

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = dateFormat.format(currentDate);
        if (!userJson.isEmpty()) {
            Gson gson = new Gson();
            User user = gson.fromJson(userJson, User.class);

            Task<Void> updateTask = databaseManagerUser.updateHistoryLoginStartLogout(user.getEmail(), idHistoryLogin, formattedDate);
            updateTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Update successful
                    Toast.makeText(MainActivity.this, "Update HistoryLogins Success", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Update failed
                    Toast.makeText(MainActivity.this, "Update HistoryLogins failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    private  void rePlaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_,fragment);
        transaction.commit();
    }
}
package com.takecare.takecare.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.takecare.takecare.Adapter.CustomAdapter;
import com.takecare.takecare.Adapter.CustomAdapterSubProfile;
import com.takecare.takecare.Helper.FirebaseHelper;
import com.takecare.takecare.Helper.FirebaseHelperSubProfile;
import com.takecare.takecare.Model.MedicalHistory;
import com.takecare.takecare.Model.SubProfile;
import com.takecare.takecare.R;

import java.util.Calendar;

public class SubProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    DatabaseReference db, databaseReference;
    FirebaseHelperSubProfile helper;
    CustomAdapterSubProfile adapter;
    ListView lv;
    FirebaseDatabase firebaseDatabase;

    EditText fnameTxt,lnameTxt,phoneTxt;

    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ini
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = mAuth.getCurrentUser().getUid();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayInputDialog();
            }
        });
        FloatingActionButton fab_follow = (FloatingActionButton) findViewById(R.id.fab_follow);

        fab_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayInfo();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();

        lv = (ListView) findViewById(R.id.lv);

        //INITIALIZE FIRE BASE DB
        databaseReference = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelperSubProfile(databaseReference);

        //ADAPTER
        adapter = new CustomAdapterSubProfile(this,helper.retrieve());
        lv.setAdapter(adapter);
    }




    //DISPLAY INPUT DIALOG
    private void displayInfo() {
        Dialog d=new Dialog(this);
        d.setContentView(R.layout.layout_dialog_subprofile);
        fnameTxt = (EditText) d.findViewById(R.id.firstnameEditText);
        lnameTxt= (EditText) d.findViewById(R.id.lastnameEditText);
        phoneTxt= (EditText) d.findViewById(R.id.phoneEditText);


        Button saveBtn= (Button) d.findViewById(R.id.saveBtn);

        //SAVE
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //GET DATA
                String fname = fnameTxt.getText().toString();
                String lname = lnameTxt.getText().toString();
                String phone = phoneTxt.getText().toString();

                //SET DATA
                SubProfile s = new SubProfile();
                s.setFirstname(fname);
                s.setLastname(lname);
                s.setPhone(phone);
                s.setCurrentuserid(currentUserID);

                //SIMPLE VALIDATION
                if (TextUtils.isEmpty(fname)) {
                    Toast.makeText(SubProfileActivity.this, "Enter date", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(lname)) {
                    Toast.makeText(SubProfileActivity.this, "Enter diagnosis", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(SubProfileActivity.this, "Enter hospital or clinic", Toast.LENGTH_SHORT).show();
                }
                else {
                    //THEN SAVE
                    if (helper.save(s)) {
                        //IF SAVED CLEAR EDITXT
                        fnameTxt.setText("");
                        lnameTxt.setText("");
                        phoneTxt.setText("");

                        Toast.makeText(SubProfileActivity.this, "New sub profile added.", Toast.LENGTH_SHORT).show();

                        adapter = new CustomAdapterSubProfile(SubProfileActivity.this, helper.retrieve());
                        lv.setAdapter(adapter);
                    }
                }
            }
        });

        d.show();
    }

    //DISPLAY INPUT DIALOG
    private void displayInputDialog()
    {

        Dialog d=new Dialog(this);
        d.setContentView(R.layout.sms_layout);


       /* number = findViewById(R.id.inputNumber);
        messageContent = findViewById(R.id.inputMessage);
        Button btnSendTXT = findViewById(R.id.sendButton);*/

        Spinner sp = (Spinner) findViewById(R.id.spinner);
        db=FirebaseDatabase.getInstance().getReference();
        helper=new FirebaseHelperSubProfile(db);

        //sp.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item));

        adapter = new CustomAdapterSubProfile(this,helper.retrieveSpin());
        //sp.setAdapter(adapter);

       /* EditText number =(EditText) d.findViewById(R.id.inputNumber);
        EditText content =(EditText) d.findViewById(R.id.inputMessage);
        Button btnSend =(Button) d.findViewById(R.id.sendButton);*/

        d.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent navigationIntent = new Intent(SubProfileActivity.this,NavigationActivity.class);
            startActivity(navigationIntent);
        } else if (id == R.id.nav_profile) {
            Intent profileIntent = new Intent(SubProfileActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        } else if (id == R.id.nav_history) {
            Intent historyIntent = new Intent(SubProfileActivity.this, MedicalHistoryActivity.class);
            startActivity(historyIntent);
        } else if (id == R.id.nav_reminder) {

        } else if (id == R.id.nav_sub) {
            Intent subprofileIntent = new Intent(SubProfileActivity.this,SubProfileActivity.class);
            startActivity(subprofileIntent);
        } else if (id == R.id.nav_about) {
            Intent aboutIntent = new Intent(SubProfileActivity.this, AboutActivity.class);
            startActivity(aboutIntent);
        } else if (id == R.id.nav_log) {
            FirebaseAuth.getInstance().signOut(); //End user session
            startActivity(new Intent(SubProfileActivity.this, LoginActivity.class)); //Go back to home page
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateNavHeader() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.nav_username);
        TextView navUserMail = headerView.findViewById(R.id.nav_user_mail);
        ImageView navUserPhoto = headerView.findViewById(R.id.nav_user_photo);

        navUserMail.setText(currentUser.getEmail());
        navUserName.setText(currentUser.getDisplayName());

        //now we will use Glide to load user image
        //import the library

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);
    }
}

package com.example.communityhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference mRef;
    Toolbar mToolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CircleImageView profileHeaderImage;
    ProgressBar progressBar;
    TextView username;
    ProgressBar progressBarHorizontal, progressBarCicle;

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Community Hub");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("User");

        checkUserExistance();
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View v = navigationView.inflateHeaderView(R.layout.nav_header_main);
        profileHeaderImage = v.findViewById(R.id.profile_image_haeder);
        username = v.findViewById(R.id.profile_username_header);

        //prgress
        progressBarCicle = findViewById(R.id.progressBarCicle);
        progressBarHorizontal = findViewById(R.id.progressBarHorizonta);

        webView = findViewById(R.id.webView);
        webView.loadUrl("https://edition.cnn.com/");
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBarHorizontal.setVisibility(View.VISIBLE);
                progressBarHorizontal.setProgress(newProgress);
                if (newProgress > 80) {
                    progressBarCicle.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.home:
                       // Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        finish();
                        break;
                    case R.id.profile:

                       startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        break;
                    case R.id.logout:
                        mAuth.signOut();
                        sendUserToLoginActivity();
                        break;
                }
                return true;
            }
        });


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawerLayout)
                .build();

    }


    private void checkUserExistance() {
        if (mUser == null) {
            sendUserToLoginActivity();
            Toast.makeText(this, "User does Exist", Toast.LENGTH_SHORT).show();
        } else {
            mRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild("profileImage")) {
                        Toast.makeText(MainActivity.this, "Setup your Profile", Toast.LENGTH_SHORT).show();
                        sendUserToSetupActivity();
                    } else {
                        Picasso.get().load(dataSnapshot.child("profileImage").getValue().toString()).
                                placeholder(R.drawable.loader).into(profileHeaderImage);
                        username.setText(dataSnapshot.child("username").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(MainActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendUserToSetupActivity() {
        Intent intent = new Intent(MainActivity.this, SetupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    private void sendUserToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return false;
    }

    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
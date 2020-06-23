package com.example.communityhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    DatabaseReference mRef;
    CircleImageView profileImage;
    EditText username, email, phone, country;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.setup_profile_image);
        username = findViewById(R.id.inputUsername);
        phone = findViewById(R.id.inputPhone);
        country = findViewById(R.id.inputCountry);
        email = findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        mRef = FirebaseDatabase.getInstance().getReference().child("User").child(mUser.getUid());

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profileImageT=dataSnapshot.child("profileImage").getValue().toString();
                    String usernameT = dataSnapshot.child("username").getValue().toString();
                    String phoneT = dataSnapshot.child("phone").getValue().toString();
                    String countryT = dataSnapshot.child("country").getValue().toString();
                    String emailT = dataSnapshot.child("email").getValue().toString();

                    Picasso.get().load(profileImageT).placeholder(R.drawable.loader).into(profileImage);
                    username.setText(usernameT);
                    phone.setText(phoneT);
                    country.setText(countryT);
                    email.setText(emailT);
                } else {
                    Toast.makeText(ProfileActivity.this, "Data Fetching", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
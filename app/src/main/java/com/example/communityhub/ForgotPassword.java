package com.example.communityhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText inputEmail;
    Button btnReset;
    FirebaseAuth mAuth;
    ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        inputEmail=findViewById(R.id.inputEmail);
        btnReset=findViewById(R.id.btnReset);
        mAuth=FirebaseAuth.getInstance();
        mLoadingBar=new ProgressDialog(this);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=inputEmail.getText().toString();
                if(email.isEmpty() || !email.contains("@"))
                {
                    inputEmail.setError("Email not valid!");
                    inputEmail.requestFocus();
                }
                else
                {
                    mLoadingBar.setTitle("Reset Password");
                    mLoadingBar.setCanceledOnTouchOutside(false);
                    mLoadingBar.show();
                   mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful())
                           {
                               inputEmail.setText("");
                               mLoadingBar.dismiss();
                               Toast.makeText(ForgotPassword.this, "Check Email to add new Password", Toast.LENGTH_SHORT).show();
                           }
                           else
                           {
                               mLoadingBar.dismiss();
                               Toast.makeText(ForgotPassword.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
                }

            }
        });

    }
}
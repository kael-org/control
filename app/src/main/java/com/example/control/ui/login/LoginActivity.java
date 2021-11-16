package com.example.control.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.control.MainActivity;
import com.example.control.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText emailText;
    private EditText passwordText;
    private TextView signUpText;
    private Button loginButton;
    private FirebaseAuth firebaseAuth;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize class variables
        emailText = findViewById(R.id.emailAddressEditText);
        passwordText = findViewById(R.id.passwordEditText);
        signUpText = findViewById(R.id.signUpText);
        loginButton = findViewById(R.id.loginButton);

        // when the user wants to sign up
        signUpText.setOnClickListener(this :: onSignUpTextClick);

        // click on login button
        loginButton.setOnClickListener(this :: onLogInButtonClick);
    }

    private boolean onSignUpTextClick(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        return true;
    }

    private boolean onLogInButtonClick(View view) {
        if (emailText == null || emailText.getText().toString().equals("")) {
            Toast.makeText(LoginActivity.this, "Please enter your email!",
                    Toast.LENGTH_SHORT).show();
        } else if (passwordText == null || passwordText.getText().toString().equals("")) {
            Toast.makeText(LoginActivity.this, "Please enter your password!",
                    Toast.LENGTH_SHORT).show();
        } else {

            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // go to home page
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent); //switch activity
                                LoginActivity.this.finish(); // close Login activity
                            } else {
                                // If sign in fails, display a message to the user
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        return true;
    }
}

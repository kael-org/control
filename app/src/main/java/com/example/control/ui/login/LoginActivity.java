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
import com.example.control.databinding.ActivityLoginBinding;
import com.example.control.databinding.ActivityViewDayMoodBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private EditText emailText;
    private EditText passwordText;
    private TextView signUpText;
    private Button loginButton;
    private FirebaseAuth firebaseAuth;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize class variables
        emailText = binding.emailAddressEditText;
        passwordText = binding.passwordEditText;
        signUpText = binding.signUpText;
        loginButton = binding.loginButton;

        // when the user wants to sign up
        signUpText.setOnClickListener(this::onSignUpTextClick);

        // click on login button
        loginButton.setOnClickListener(this::onLogInButtonClick);
    }

    /**
     * When the sign up text is clicked, go to the sign up activity
     * @param view
     * @return
     */
    private boolean onSignUpTextClick(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    /**
     * When the log in button is clicked, perform some checks
     * When all is good, go to home fragment
     * @param view
     * @return
     */
    private boolean onLogInButtonClick(View view) {
        if (emailText.getText().toString().equals("")) {
            Toast.makeText(LoginActivity.this, "Please enter your email!",
                    Toast.LENGTH_SHORT).show();
        } else if (passwordText.getText().toString().equals("")) {
            Toast.makeText(LoginActivity.this, "Please enter your password!",
                    Toast.LENGTH_SHORT).show();
        } else {
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // go to home page
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            } else {
                                // If sign in fails, display a message to the user
                                Toast.makeText(LoginActivity.this, "Authentication failed. Please try again!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        return true;
    }
}

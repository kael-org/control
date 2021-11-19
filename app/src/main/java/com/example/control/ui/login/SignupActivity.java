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
import com.example.control.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private static final String ADD_USERNAME = "ADD_USERNAME";

    private ActivitySignupBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;

    private EditText username;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button signupButton;
    private TextView signInText;

    private final Integer passwordMin = 12;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize class variables
        username = binding.username;
        email = binding.emailAddress;
        password = binding.password;
        confirmPassword = binding.confirmPassword;
        signupButton = binding.signUpButton;
        signInText = binding.signInText;

        // when the user wants to sign in
        signInText.setOnClickListener(this::onSignInTextClick);

        // click on signup button
        signupButton.setOnClickListener(this::onSignUpButtonClick);
    }

    /**
     * When the sign in text is clicked, go to the login activity
     * @param view
     * @return
     */
    private boolean onSignInTextClick(View view) {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    /**
     * When the sign up button is clicked, perform some checks
     * If all is good, add the user to the database
     * Go to main activity after
     * @param view
     * @return
     */
    private boolean onSignUpButtonClick(View view) {
        // do some checks, if all are good, sign up the user
        if (username.getText().toString().equals("")) {
            Toast.makeText(SignupActivity.this, "Please enter a username!",
                    Toast.LENGTH_SHORT).show();
        } else if (email.getText().toString().equals("")) {
            Toast.makeText(SignupActivity.this, "Please enter your email address!",
                    Toast.LENGTH_SHORT).show();
        } else if (!checkEmail(email.getText().toString())) {
            Toast.makeText(SignupActivity.this, "Please enter a correct email address!",
                    Toast.LENGTH_SHORT).show();
        } else if (password.getText().toString().equals("")) {
            Toast.makeText(SignupActivity.this, "Please enter a password!",
                    Toast.LENGTH_SHORT).show();
        } else if (confirmPassword.getText().toString().equals("")) {
            Toast.makeText(SignupActivity.this, "Please confirm a password!",
                    Toast.LENGTH_SHORT).show();
        } else if (password.getText().toString().length() < passwordMin) {
            Toast.makeText(SignupActivity.this, "The password must be at least " + passwordMin.toString() + " characters",
                    Toast.LENGTH_SHORT).show();
        } else if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            Toast.makeText(SignupActivity.this, "The passwords don't match! Please try again.",
                    Toast.LENGTH_SHORT).show();
        } else {
            String email_str = email.getText().toString();
            String password_str = password.getText().toString();
            firebaseAuth.createUserWithEmailAndPassword(email_str, password_str)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign up is successful, add the user
                                user = firebaseAuth.getCurrentUser();

                                // complete adding the user by adding to the database
                                addUsername();
                                addUserToDatabase();

                                // go to main activity
                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                startActivity(intent);
                                SignupActivity.this.finish();
                            } else {
                                // If sign up fails, display a message to the user
                                Toast.makeText(SignupActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
        return true;
    }

    private boolean checkEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) { return false;}
        else { return pattern.matcher(email).matches();}
    }

    private void addUserToDatabase() {
        // add the user to the database
        String users_collection_name = "Users";
        String uid = user.getUid();
        HashMap<String, String> emptyMap = new HashMap<>();

        db.collection(users_collection_name).document(uid).set(emptyMap);
    }

    private void addUsername() {
        UserProfileChangeRequest addUser = new UserProfileChangeRequest.Builder()
                .setDisplayName(username.getText().toString())
                .build();

        user.updateProfile(addUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(ADD_USERNAME, "User profile updated.");
                        }
                    }
                });
    }
}

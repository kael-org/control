package com.example.control.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.control.databinding.FragmentSettingsBinding;
import com.example.control.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;

    private FirebaseFirestore db;
    private FirebaseUser user;
    private String uid;

    private TextView username;
    private TextView email;
    private Button logOutButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // initializing the database
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        // set username
        username = binding.usernameProfile;
        assert user != null;
        username.setText(user.getDisplayName());

        // set email address
        email = binding.emailProfile;
        assert user != null;
        email.setText(user.getEmail());

        // set listener for when the user wants to log out
        logOutButton = binding.logoutButton;
        logOutButton.setOnClickListener(this::onLogOutButtonClick);

        return root;
    }

    /**
     * Log out of the app
     * @param view
     * @return
     */
    private boolean onLogOutButtonClick(View view) {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        onDestroyView();
        return true;
    }

    /**
     * When the view is destroyed, set binding to null
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

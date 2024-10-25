package com.example.attempt2.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.attempt2.LoginActivity;
import com.example.attempt2.R;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class EditProfileFragment extends Fragment {

    private ImageView profileImage;
    private TextView editPhotoButton;
    private TextView removePhotoButton;
    private TextView saveButton;
    private EditText nameField;
    private EditText usernameField;
    private EditText majorField;
    private EditText currentPasswordField; // New field for current password
    private EditText newPasswordField; // New field for new password

    private FirebaseAuth mAuth;
    private DatabaseReference database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users");

        // Initialize views
        profileImage = view.findViewById(R.id.profile_image);
        editPhotoButton = view.findViewById(R.id.edit_photo_button);
        removePhotoButton = view.findViewById(R.id.remove_photo_button);
        saveButton = view.findViewById(R.id.save_button);
        nameField = view.findViewById(R.id.name_field);
        usernameField = view.findViewById(R.id.username_field);
        majorField = view.findViewById(R.id.major_field);
        currentPasswordField = view.findViewById(R.id.password_field); // current password
        newPasswordField = view.findViewById(R.id.new_password_field); // New field for new password

        // Load current user's data
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            loadUserData(uid);
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            Toast.makeText(getActivity(), "Login Required", Toast.LENGTH_SHORT).show();
        }

        // Set up save button click listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData(user.getUid());
            }
        });

        return view;
    }

    private void loadUserData(String uid) {
        database.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String username = snapshot.child("username").getValue(String.class);
                    String major = snapshot.child("degree").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    // Set data to fields
                    nameField.setText(name);
                    usernameField.setText(username);
                    majorField.setText(major);
                } else {
                    Toast.makeText(getActivity(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserData(String uid) {
        String name = nameField.getText().toString().trim();
        String username = usernameField.getText().toString().trim();
        String major= majorField.getText().toString().trim();
        String currentPassword = currentPasswordField.getText().toString().trim();
        String newPassword = newPasswordField.getText().toString().trim();
        String email = "";
        // Validate current password before updating
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        if (!currentPassword.isEmpty() && !newPassword.isEmpty()) {
                // Re-authenticate the user
                email = user.getEmail();
                user.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), currentPassword)).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Update password
                        user.updatePassword(newPassword).addOnCompleteListener(passwordUpdateTask -> {
                            if (passwordUpdateTask.isSuccessful()) {
                                Toast.makeText(getActivity(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Failed to update password", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Current password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        // Create a user object to save
        UserProfile userProfile = new UserProfile(name, username, major, email);

        // Save data to the database
        database.child(uid).setValue(userProfile).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                // You can also navigate back or close this fragment
            } else {
                Toast.makeText(getActivity(), "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //  Create a model class for user profile
    public static class UserProfile {
        public String name;
        public String username;
        public String major;
        public String email;

        public UserProfile() {} // Default constructor required for calls to DataSnapshot.getValue(UserProfile.class)

        public UserProfile(String name, String username, String major, String email) {
            this.name = name;
            this.username = username;
            this.major = major;
            this.email = email;
        }
    }
}

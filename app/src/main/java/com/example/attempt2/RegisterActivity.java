package com.example.attempt2;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attempt2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kwabenaberko.newsapilib.models.Article;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference database;

    private EditText nameField, usernameField, emailField, passwordField , majorField;
    private Button registerButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users"); // Reference to the users node

        nameField = findViewById(R.id.name_field);
        usernameField = findViewById(R.id.username_field);
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        majorField = findViewById(R.id.major_field);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String name = nameField.getText().toString().trim();
        String username = usernameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String major = majorField.getText().toString().trim();
        // Validate inputs
        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || major.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user with Firebase Auth
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // User registration successful
                        FirebaseUser user = auth.getCurrentUser();
                        String userId = user.getUid(); // Get user ID

                        // Create a User object
                        User newUser = new User(name, username, email,major);

                        // Save user data to the database
                        database.child(userId).setValue(newUser) //userID for proper database
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Registration failed
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // User model class
    public static class User {
        public String Name;
        public String Username;
        public String Email;
        public String Major;
        public float EnergyScore;
        public float PlantScore;
        public float Co2Score;
        public float FinanceScore;
        public int RewardScore;
        // New: Folder-based storage for articles
        public Map<String, List<Article>> ArticleFolders; // Folder name and list of articles

        public User() {
            ArticleFolders = new HashMap<>();
        } // Default constructor required for calls to DataSnapshot.getValue(User.class)

        public User(String name, String username, String email, String major) {
            this.Name = name;
            this.Username = username;
            this.Email = email;
            this.Major = major;
            this.EnergyScore = 0f;
            this.PlantScore = 0f;
            this.Co2Score = 0f;
            this.FinanceScore = 0f;
            this.RewardScore = 0;
            this.ArticleFolders = new HashMap<>();
        }
    }
}

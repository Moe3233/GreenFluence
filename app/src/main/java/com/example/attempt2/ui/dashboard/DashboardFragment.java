package com.example.attempt2.ui.dashboard;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.transition.Transition;
import com.example.attempt2.LoginActivity;
import com.example.attempt2.R;
import com.example.attempt2.databinding.FragmentDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users");

        // Get current user
        FirebaseUser user = mAuth.getCurrentUser();

        // Check if user is logged in
        if (user == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            Toast.makeText(getActivity(), "Login Required", Toast.LENGTH_SHORT).show();
            return root;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Reference to the user's article folder in the database
        DatabaseReference userArticlesRef = database.child(userId).child("Bookmarks");

        // Reference to the LinearLayout where articles will be displayed
        LinearLayout articlesContainer = root.findViewById(R.id.articles_container);

        // Retrieve and display articles
        userArticlesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot articleSnapshot : snapshot.getChildren()) {
                        String title = articleSnapshot.getKey();
                        // Get the nested map (url and imageUrl)
                        Map<String, Object> articleDetails = (Map<String, Object>) articleSnapshot.getValue();

                        if (articleDetails != null) {
                            String url = (String) articleDetails.get("url");
                            String imageUrl = (String) articleDetails.get("imageUrl");

                            // Create a LinearLayout to hold the title and URL
                            LinearLayout articleLayout = new LinearLayout(getContext());
                            articleLayout.setOrientation(LinearLayout.VERTICAL);
                            articleLayout.setPadding(16, 25, 16, 16); // Padding for spacing
                            articlesContainer.setBackgroundColor(getResources().getColor(android.R.color.black)); // Set background color for debugging

                            // Create a new TextView for the title
                            TextView titleView = new TextView(getContext());
                            titleView.setText("Title: " + title);
                            titleView.setTextSize(18);
                            titleView.setTextColor(getResources().getColor(android.R.color.white)); // Set text color to contrast with background

                            // Create a new TextView for the URL
                            TextView urlView = new TextView(getContext());
                            urlView.setText("URL: " + url);
                            urlView.setTextSize(16);
                            urlView.setTextColor(getResources().getColor(android.R.color.white)); // Set text color to contrast with background

                            // Add the TextViews to the LinearLayout
                            articleLayout.addView(titleView);
                            articleLayout.addView(urlView);

                            // Load the image URL as the background using Glide
                            Glide.with(getContext())
                                    .load(imageUrl)
                                    .centerCrop() // Scale the image to fill the background
                                    .into(new com.bumptech.glide.request.target.CustomTarget<Drawable>() {
                                        @Override
                                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                            // Set the loaded image as the background
                                            articleLayout.setBackground(resource);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                            // Handle the case when the image is cleared
                                        }
                                    });

                            // Add the article layout to the articles container
                            articlesContainer.addView(articleLayout);
                        }
                    }
                } else {
                    // Ensure articlesContainer is visible
                    articlesContainer.setVisibility(View.VISIBLE);
                    articlesContainer.setBackgroundColor(getResources().getColor(android.R.color.black)); // Set background color for debugging

                    // Create and display "No Bookmarks Found" message
                    TextView titleView = new TextView(getContext());
                    titleView.setText("No Bookmarks Found");
                    titleView.setTextSize(25);
                    titleView.setTextColor(getResources().getColor(android.R.color.white)); // Set text color to contrast with background
                    titleView.setPadding(16, 16, 16, 16); // Add padding to improve visibility

                    // Add the message TextView to the articles container
                    articlesContainer.addView(titleView);

                    // Show a Toast message
                    Toast.makeText(getActivity(), "No bookmarks found.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Toast.makeText(getActivity(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

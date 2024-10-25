package com.example.attempt2.ui.rewards;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.attempt2.LoginActivity;
import com.example.attempt2.R;
import com.example.attempt2.databinding.FragmentHomeBinding;
import com.example.attempt2.databinding.FragmentRewardsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;
import android.widget.Toast;

public class RewardFragment extends Fragment {

    private int currentPoints; // Starting points
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private FragmentRewardsBinding binding;

    // Utility method to show toast message
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rewards, container, false);
        binding = FragmentRewardsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Initialize Firebase Auth

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users"); // Assuming your users are stored under "users"

        // Get current user
        FirebaseUser user = mAuth.getCurrentUser();
        // Get reference to the points TextView
        TextView pointsTextView = view.findViewById(R.id.points);
        TextView redPointsTextView = view.findViewById(R.id.redeemPoints);
        // Check if user is logged in
        if (user == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            Toast.makeText(getActivity(), "Login Required", Toast.LENGTH_SHORT).show();
            return root;
        }

        String uid = user.getUid(); // Get UID of the logged-in user
        DatabaseReference rewardsRef = database.child(uid);
        rewardsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentPoints = snapshot.child("RewardScore").getValue(Integer.class);
                    pointsTextView.setText(String.valueOf(currentPoints));
                    redPointsTextView.setText(String.valueOf(currentPoints)); // Update the points display

                } else {
                    currentPoints = 0;
                    showToast("Points couldn't be loaded");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set the styled text for the title
        TextView titleTextView = view.findViewById(R.id.title);
        SpannableString spannable = new SpannableString("Greenfluence Rewards");
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // Green
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#005a9e")), 5, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // fluence Rewards
        titleTextView.setText(spannable);


        Button redeemButton = view.findViewById(R.id.redeem_button);
        Button activitiesButton = view.findViewById(R.id.activities_button);

        // Flower points ImageViews
        ImageView flowerpoints50 = view.findViewById(R.id.flowerpoints50);
        ImageView flowerpoints100 = view.findViewById(R.id.flowerpoints100);
        ImageView flowerpoints500 = view.findViewById(R.id.flowerpoints500);
        ImageView flowerpoints1000 = view.findViewById(R.id.flowerpoints1000);

        // Points Redeem section
        View pointsRedeemSection = view.findViewById(R.id.PointsRedeem);
        View pointsActSection = view.findViewById(R.id.PointsAct);

        // Find all rewards buttons
        Button rewards1 = view.findViewById(R.id.rewards1);
        Button rewards2 = view.findViewById(R.id.rewards2);
        Button rewards3 = view.findViewById(R.id.rewards3);
        Button rewards4 = view.findViewById(R.id.rewards4);
        Button rewards5 = view.findViewById(R.id.rewards5);
        Button rewards6 = view.findViewById(R.id.rewards6);

        // Find all activities buttons
        Button act1 = view.findViewById(R.id.act1);
        Button act2 = view.findViewById(R.id.act2);
        Button act3 = view.findViewById(R.id.act3);

        // Initially hide rewards buttons
        rewards1.setVisibility(View.GONE);
        rewards2.setVisibility(View.GONE);
        rewards3.setVisibility(View.GONE);
        rewards4.setVisibility(View.GONE);
        rewards5.setVisibility(View.GONE);
        rewards6.setVisibility(View.GONE);
        pointsRedeemSection.setVisibility(View.GONE); // Initially hide Points Redeem section
        pointsActSection.setVisibility(View.VISIBLE);

        // Show activities buttons initially
        act1.setVisibility(View.VISIBLE);
        act2.setVisibility(View.VISIBLE);
        act3.setVisibility(View.VISIBLE);

        // Add points on activity button clicks
        act1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increasePoints(10,redPointsTextView, pointsTextView, flowerpoints50, flowerpoints100, flowerpoints500, flowerpoints1000);
                v.setBackgroundColor(Color.parseColor("#D3D3D3")); // Set button color to grey after click
                v.setEnabled(false); // Disable the button after clicking
                ChangePoint(rewardsRef,currentPoints);
            }
        });

        act2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increasePoints(100,redPointsTextView, pointsTextView, flowerpoints50, flowerpoints100, flowerpoints500, flowerpoints1000);
                v.setBackgroundColor(Color.parseColor("#D3D3D3")); // Set button color to grey after click
                v.setEnabled(false); // Disable the button after clicking
                ChangePoint(rewardsRef,currentPoints);
            }
        });

        act3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increasePoints(50,redPointsTextView, pointsTextView, flowerpoints50, flowerpoints100, flowerpoints500, flowerpoints1000);
                v.setBackgroundColor(Color.parseColor("#D3D3D3")); // Set button color to grey after click
                v.setEnabled(false); // Disable the button after clicking
                ChangePoint(rewardsRef,currentPoints);
            }
        });

        // Hide rewards buttons when activities is clicked
        activitiesButton.setOnClickListener(v -> {
            setRewardsVisibility(View.GONE, rewards1, rewards2, rewards3, rewards4, rewards5, rewards6);
            setActivitiesVisibility(View.VISIBLE, act1, act2, act3);
            pointsRedeemSection.setVisibility(View.GONE); // Hide Points Redeem section
            pointsActSection.setVisibility(View.VISIBLE);
        });

        // Show rewards buttons when redeem is clicked
        redeemButton.setOnClickListener(v -> {
            setRewardsVisibility(View.VISIBLE, rewards1, rewards2, rewards3, rewards4, rewards5, rewards6);
            setActivitiesVisibility(View.GONE, act1, act2, act3);
            pointsRedeemSection.setVisibility(View.VISIBLE); // Show Points Redeem section
            pointsActSection.setVisibility(View.GONE);
        });

        rewards1.setOnClickListener(v -> {
            if (currentPoints >= 100) {
                deductPoints(100, redPointsTextView, pointsTextView, flowerpoints50, flowerpoints100, flowerpoints500, flowerpoints1000);
                v.setBackgroundColor(Color.parseColor("#D3D3D3")); // Set button color to grey after click
                v.setEnabled(false); // Disable the button after clicking
                ChangePoint(rewardsRef,currentPoints);
            } else {
                showToast("Insufficient points"); // Show toast if not enough points
            }
        });

        rewards2.setOnClickListener(v -> {
            if (currentPoints >= 400) {
                deductPoints(400, redPointsTextView, pointsTextView, flowerpoints50, flowerpoints100, flowerpoints500, flowerpoints1000);
                v.setBackgroundColor(Color.parseColor("#D3D3D3"));
                v.setEnabled(false);
                ChangePoint(rewardsRef,currentPoints);
            } else {
                showToast("Insufficient points");
            }
        });

        rewards3.setOnClickListener(v -> {
            if (currentPoints >= 1000) {
                deductPoints(1000, redPointsTextView, pointsTextView, flowerpoints50, flowerpoints100, flowerpoints500, flowerpoints1000);
                v.setBackgroundColor(Color.parseColor("#D3D3D3"));
                v.setEnabled(false);
                ChangePoint(rewardsRef,currentPoints);
            } else {
                showToast("Insufficient points");
            }
        });

        rewards4.setOnClickListener(v -> {
            if (currentPoints >= 100) {
                deductPoints(100, redPointsTextView, pointsTextView, flowerpoints50, flowerpoints100, flowerpoints500, flowerpoints1000);
                v.setBackgroundColor(Color.parseColor("#D3D3D3"));
                v.setEnabled(false);
                ChangePoint(rewardsRef,currentPoints);
            } else {
                showToast("Insufficient points");
            }
        });

        rewards5.setOnClickListener(v -> {
            if (currentPoints >= 200) {
                deductPoints(200, redPointsTextView, pointsTextView, flowerpoints50, flowerpoints100, flowerpoints500, flowerpoints1000);
                v.setBackgroundColor(Color.parseColor("#D3D3D3"));
                v.setEnabled(false);
                ChangePoint(rewardsRef,currentPoints);
            } else {
                showToast("Insufficient points");
            }
        });

        rewards6.setOnClickListener(v -> {
            if (currentPoints >= 150) {
                deductPoints(150, redPointsTextView, pointsTextView, flowerpoints50, flowerpoints100, flowerpoints500, flowerpoints1000);
                v.setBackgroundColor(Color.parseColor("#D3D3D3"));
                v.setEnabled(false);
                ChangePoint(rewardsRef,currentPoints);
            } else {
                showToast("Insufficient points");
            }
        });


        return view;
    }


    // Method to deduct points and prevent going below 0
    private void deductPoints(int pointsToDeduct, TextView redPointsTextView, TextView pointsTextView, ImageView flowerpoints50, ImageView flowerpoints100, ImageView flowerpoints500, ImageView flowerpoints1000) {
        if (currentPoints >= pointsToDeduct) {
            currentPoints -= pointsToDeduct;
            pointsTextView.setText(String.valueOf(currentPoints));
            redPointsTextView.setText(String.valueOf(currentPoints));
            updateFlowerIcons(flowerpoints50, flowerpoints100, flowerpoints500, flowerpoints1000); // Update flower icons after points deduction

        }
    }
    private void increasePoints(int pointsToIncrease, TextView redPointsTextView, TextView pointsTextView, ImageView flowerpoints50, ImageView flowerpoints100, ImageView flowerpoints500, ImageView flowerpoints1000) {

            currentPoints += pointsToIncrease;
            pointsTextView.setText(String.valueOf(currentPoints));
            redPointsTextView.setText(String.valueOf(currentPoints));
            updateFlowerIcons(flowerpoints50, flowerpoints100, flowerpoints500, flowerpoints1000); // Update flower icons after points deduction

    }
    // Method to update the color of flower icons based on points
    private void updateFlowerIcons(ImageView flowerpoints50, ImageView flowerpoints100, ImageView flowerpoints500, ImageView flowerpoints1000) {
        if (currentPoints >= 50) {
            flowerpoints50.setColorFilter(Color.parseColor("#00FF00")); // Green
        } else {
            flowerpoints50.setColorFilter(Color.BLACK); // Black
        }

        if (currentPoints >= 100) {
            flowerpoints100.setColorFilter(Color.parseColor("#00FF00")); // Green
        } else {
            flowerpoints100.setColorFilter(Color.BLACK);
        }

        if (currentPoints >= 500) {
            flowerpoints500.setColorFilter(Color.parseColor("#00FF00")); // Green
        } else {
            flowerpoints500.setColorFilter(Color.BLACK);
        }

        if (currentPoints >= 1000) {
            flowerpoints1000.setColorFilter(Color.parseColor("#00FF00")); // Green
        } else {
            flowerpoints1000.setColorFilter(Color.BLACK);
        }
    }

    // Utility method to set visibility of reward buttons
    private void setRewardsVisibility(int visibility, Button... rewardButtons) {
        for (Button button : rewardButtons) {
            button.setVisibility(visibility);
        }
    }

    // Utility method to set visibility of activity buttons
    private void setActivitiesVisibility(int visibility, Button... activityButtons) {
        for (Button button : activityButtons) {
            button.setVisibility(visibility);
        }
    }


    private void ChangePoint(DatabaseReference ref, int newPoints) {
        // Update Firebase
        ref.child("RewardScore").setValue(newPoints).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                showToast("Updating points in Firebase.");
            } else {
                showToast("Error updating points in Firebase.");
            }
        });
    }


}


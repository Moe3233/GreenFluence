package com.example.attempt2.ui.entry;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.attempt2.LoginActivity;
import com.example.attempt2.R;
import com.example.attempt2.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EntryFragment extends Fragment {

    private EntryViewModel mViewModel;

    private RadioGroup rgHeating, rgTravel, rgPurchase, rgPlanting;
    private Button submitButton;

    private int ePoint, cPoint, fPoint, pPoint, rPoint;
    private FirebaseAuth mAuth;
    private DatabaseReference database;

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entry, container, false);

        // Initialize the RadioGroups
        rgHeating = view.findViewById(R.id.rg_heating);
        rgTravel = view.findViewById(R.id.rg_travel);
        rgPurchase = view.findViewById(R.id.rg_purchase);
        rgPlanting = view.findViewById(R.id.rg_planting);

        // Initialize the Submit button
        submitButton = view.findViewById(R.id.button);

        // Set an OnClickListener to calculate the points when the Submit button is clicked
        submitButton.setOnClickListener(v -> calculatePoints());
        mAuth = FirebaseAuth.getInstance();
        // Assuming your users are stored under "users"

        // Get current user
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            Toast.makeText(getActivity(), "Login Required", Toast.LENGTH_SHORT).show();
            return view;
        }
        String uid = user.getUid(); // Get UID of the logged-in user
        database = FirebaseDatabase.getInstance().getReference("users").child(uid);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    double energyScore = snapshot.child("EnergyScore").getValue(double.class);
                    double plantScore = snapshot.child("PlantScore").getValue(double.class);
                    double coScore = snapshot.child("Co2Score").getValue(double.class);
                    double financeScore = snapshot.child("FinanceScore").getValue(double.class);
                    int RewardsScore = snapshot.child("RewardScore").getValue(Integer.class);
                    Toast.makeText(getActivity(), String.format("%.2f", energyScore), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), String.format("%.2f", plantScore), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), String.format("%.2f", coScore), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), String.format("%.2f", financeScore), Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(getActivity(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


    private void calculatePoints() {
        rPoint = 0;

        // Calculate points for Heating question
        int selectedHeatingId = rgHeating.getCheckedRadioButtonId();
        if (selectedHeatingId != -1) {
            RadioButton selectedHeating = getView().findViewById(selectedHeatingId);
            switch (selectedHeating.getText().toString()) {
                case "Rarely":
                    rPoint += 1;
                    ePoint = 1;
                    break;
                case "Occasionally":
                    ePoint = 0;
                    break;
                case "Frequently":
                    ePoint = -1;
                    break;
                case "No use":
                    rPoint += 3;
                    ePoint = 3;
                    break;
            }
        }

        // Calculate points for Travel question
        int selectedTravelId = rgTravel.getCheckedRadioButtonId();
        if (selectedTravelId != -1) {
            RadioButton selectedTravel = getView().findViewById(selectedTravelId);
            switch (selectedTravel.getText().toString()) {
                case "Walk/Bike":
                    rPoint += 2;
                    cPoint = 3;
                    break;
                case "Car":
                    cPoint = -1;
                    break;
                case "Bus":
                    rPoint += 1;
                    cPoint = 1;
                    break;
            }
        }

        // Calculate points for Purchase question
        int selectedPurchaseId = rgPurchase.getCheckedRadioButtonId();
        if (selectedPurchaseId != -1) {
            RadioButton selectedPurchase = getView().findViewById(selectedPurchaseId);
            switch (selectedPurchase.getText().toString()) {
                case "Brand new":
                    fPoint = -1;
                    break;
                case "Second-hand":
                    rPoint += 1;
                    fPoint = 2;
                    break;
                case "No purchase":
                    rPoint += 3;
                    fPoint = 3;
                    break;
            }
        }

        // Calculate points for Planting question
        int selectedPlantingId = rgPlanting.getCheckedRadioButtonId();
        if (selectedPlantingId != -1) {
            RadioButton selectedPlanting = getView().findViewById(selectedPlantingId);
            switch (selectedPlanting.getText().toString()) {
                case "Planting trees in local park":
                case "Indoor plant workshops":
                case "Create school garden":
                    rPoint += 1;
                    pPoint = 3;
                    break;
            }
        }

        // Show total points in a Toast
        Toast.makeText(getContext(), "Total Points: " + rPoint, Toast.LENGTH_LONG).show();
        double alpha = 0.30F;
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    double energyScore = snapshot.child("EnergyScore").getValue(double.class);
                    double plantScore = snapshot.child("PlantScore").getValue(double.class);
                    double coScore = snapshot.child("Co2Score").getValue(double.class);
                    double financeScore = snapshot.child("FinanceScore").getValue(double.class);
                    int RewardsScore = snapshot.child("RewardScore").getValue(Integer.class);
                    // Update the score using an exponential moving average formula
                    double newScoreEn = alpha * ePoint + (1-alpha)* energyScore;
                    double newScorePl = alpha * pPoint + (1-alpha)* plantScore;
                    double newScoreCo = alpha * cPoint + (1-alpha)* coScore;
                    double newScoreFi = alpha * fPoint + (1-alpha)* financeScore;
                    // Clamp the new score to ensure it stays between 0 and 3
                    newScoreEn = Math.max(0, Math.min(3, newScoreEn));
                    newScorePl = Math.max(0, Math.min(3, newScorePl));
                    newScoreCo = Math.max(0, Math.min(3, newScoreCo));
                    newScoreFi = Math.max(0, Math.min(3, newScoreFi));
                    int newScoreRe = RewardsScore + rPoint;
                    // Create a map to store the updated scores
                    Map<String, Object> scoreUpdates = new HashMap<>();
                    scoreUpdates.put("EnergyScore", newScoreEn);
                    scoreUpdates.put("PlantScore", newScorePl);
                    scoreUpdates.put("Co2Score", newScoreCo);
                    scoreUpdates.put("FinanceScore", newScoreFi);
                    scoreUpdates.put("RewardScore", newScoreRe);

                    // Update the scores in the database
                    database.updateChildren(scoreUpdates)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Data successfully written
                                    Toast.makeText(getActivity(), "Scores updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to write data
                                    Toast.makeText(getActivity(), "Failed to update scores: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
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
}

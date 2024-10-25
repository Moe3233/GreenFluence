package com.example.attempt2.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;

import com.example.attempt2.LoginActivity;
import com.example.attempt2.R;
import com.example.attempt2.UserSession;
import com.example.attempt2.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;


public class HomeFragment extends Fragment {

    TextView textViewID;
    TextView textViewName;

    EditText nameStudent;
    EditText nameMajor;
    EditText nameID;
    EditText editTextName;
    DatabaseReference dbRef;

    ImageView ener1;
    ImageView ener2;
    ImageView ener3;

    ImageView tree1;
    ImageView tree2;
    ImageView tree3;

    ImageView cO1;
    ImageView cO2;
    ImageView cO3;

    ImageView cash1;
    ImageView cash2;
    ImageView cash3;


    private FirebaseAuth mAuth;
    private DatabaseReference database;

    private FragmentHomeBinding binding;
    private NewsApiClient newsApiClient;

    private LinearLayout articleContainer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        textViewID = root.findViewById(R.id.textView_Title);
        nameStudent = root.findViewById(R.id.editTextName);
        nameID = root.findViewById(R.id.editTextStudent);
        nameMajor = root.findViewById(R.id.editTextMajor);

        ener1 = root.findViewById(R.id.ener1);
        ener2 = root.findViewById(R.id.ener2);
        ener3 = root.findViewById(R.id.ener3);
        tree1 = root.findViewById(R.id.tree1);
        tree2 = root.findViewById(R.id.tree2);
        tree3 = root.findViewById(R.id.tree3);
        cO1 = root.findViewById(R.id.co1);
        cO2 = root.findViewById(R.id.co2);
        cO3 = root.findViewById(R.id.co3);
        cash1 = root.findViewById(R.id.cash1);
        cash2 = root.findViewById(R.id.cash2);
        cash3 = root.findViewById(R.id.cash3);

        SpannableString spannableString = new SpannableString("Greenfluence");
        int greenColor = getResources().getColor(R.color.green);// Set "Green" to green color
        spannableString.setSpan(new ForegroundColorSpan(greenColor), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int blueColor = getResources().getColor(R.color.blue);  // Set "Fluence" to blue color
        spannableString.setSpan(new ForegroundColorSpan(blueColor), 5, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewID.setText(spannableString); // Setting text to "GreenFluence"


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users"); // Assuming your users are stored under "users"

        // Get current user
        FirebaseUser user = mAuth.getCurrentUser();

        // Check if user is logged in
        if (user == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            Toast.makeText(getActivity(), "Login Required", Toast.LENGTH_SHORT).show();
            return root;
        }

        articleContainer = root.findViewById(R.id.articleContainer);
        // Create a NewsApiClient instance with your API key
        newsApiClient = new NewsApiClient("c183b7de64a849709669614aaaa930ae");

        // Fetch top headlines
        fetchArticles("sustain");
        String uid = user.getUid(); // Get UID of the logged-in user
        database.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("Name").getValue(String.class);
                    nameStudent.setText(name);
                    String major = snapshot.child("Major").getValue(String.class);
                    nameMajor.setText(major);
                    String username = snapshot.child("Username").getValue(String.class);
                    nameID.setText(username);
                    int energyScore = snapshot.child("EnergyScore").getValue(Integer.class);
                    int plantScore = snapshot.child("PlantScore").getValue(Integer.class);
                    int coScore = snapshot.child("Co2Score").getValue(Integer.class);
                    int financeScore = snapshot.child("FinanceScore").getValue(Integer.class);

                    // Handle energyScore using switch case
                    switch (energyScore) {
                        case 1:
                            ener1.setImageResource((R.drawable.energyon));
                            break;
                        case 2:
                            ener1.setImageResource((R.drawable.energyon));
                            ener2.setImageResource((R.drawable.energyon));
                            break;
                        case 3:
                            ener1.setImageResource((R.drawable.energyon));
                            ener2.setImageResource((R.drawable.energyon));
                            ener3.setImageResource((R.drawable.energyon));
                            break;
                        default:
                            break;
                    }
                    switch (plantScore) {
                        case 1:
                            tree1.setImageResource((R.drawable.treeon));
                            break;
                        case 2:
                            tree1.setImageResource((R.drawable.treeon));
                            tree2.setImageResource((R.drawable.treeon));
                            break;
                        case 3:
                            tree1.setImageResource((R.drawable.treeon));
                            tree2.setImageResource((R.drawable.treeon));
                            tree3.setImageResource((R.drawable.treeon));
                            break;
                        default:
                            break;
                    }
                    switch (coScore) {
                        case 1:
                            cO1.setImageResource((R.drawable.cloudon));
                            break;
                        case 2:
                            cO1.setImageResource((R.drawable.cloudon));
                            cO2.setImageResource((R.drawable.cloudon));
                            break;
                        case 3:
                            cO1.setImageResource((R.drawable.cloudon));
                            cO2.setImageResource((R.drawable.cloudon));
                            cO3.setImageResource((R.drawable.cloudon));
                            break;
                        default:
                            break;
                    }

                    switch (financeScore) {
                        case 1:
                            cash1.setImageResource((R.drawable.moneyon));
                            break;
                        case 2:
                            cash1.setImageResource((R.drawable.moneyon));
                            cash2.setImageResource((R.drawable.moneyon));
                            break;
                        case 3:
                            cash1.setImageResource((R.drawable.moneyon));
                            cash2.setImageResource((R.drawable.moneyon));
                            cash3.setImageResource((R.drawable.moneyon));
                            break;
                        default:
                            break;
                    }
                } else {
                    Toast.makeText(getActivity(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        View imageView = root.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                // Get the BottomNavigationView from the activity and click the profile tab
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
                bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
            }
        });
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void fetchArticles(String query) {
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q(query)
                        .language("en") // Specify the language if needed
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        if (response.getArticles() != null && !response.getArticles().isEmpty()) {
                            articleContainer.removeAllViews(); // Clear previous articles

                            for (int i = 0; i < response.getArticles().size(); i++) {
                                String articleTitle = response.getArticles().get(i).getTitle();
                                String articleUrl = response.getArticles().get(i).getUrl(); // Get the URL for each article

                                // Creates a horizontal LinearLayout to hold the article and the button
                                LinearLayout articleLayout = new LinearLayout(getContext());
                                articleLayout.setOrientation(LinearLayout.HORIZONTAL);

                                // Creates a new TextView for the article title
                                TextView articleTextView = new TextView(getContext());
                                articleTextView.setText(articleTitle);
                                articleTextView.setTextSize(16);

                                // Sets weight and layout params for the article title (80%)
                                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                                        0, // Use 0 to enable weight
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        0.8f // 80% weight
                                );
                                articleTextView.setLayoutParams(textParams);
                                // Creates a border programmatically for the TextView
                                GradientDrawable border = new GradientDrawable();
                                border.setStroke(5, getResources().getColor(R.color.black)); // Border width and color
                                border.setCornerRadius(10); // Optional: rounded corners
                                articleTextView.setPadding(16, 16, 16, 16); // Add some padding inside the border
                                articleTextView.setBackground(border); // Set the border as background

                                // Adds click listener to open article URL
                                articleTextView.setOnClickListener(view -> {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl));
                                    startActivity(browserIntent);
                                });

                                // Creates a Button for bookmarking the article
                                Button bookmarkButton = new Button(getContext());
                                bookmarkButton.setBackgroundResource(R.drawable.bookmarklogo); // Set the bookmarkLogo drawable
                                bookmarkButton.setPadding(16, 16, 16, 16);

                                // Set weight and layout params for the bookmark button (20%)
                                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                                        0, // Use 0 to enable weight
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        0.2f // 20% weight
                                );
                                bookmarkButton.setLayoutParams(buttonParams);

                                // Adds a click listener to the bookmark button to save the article
                                int finalI = i;
                                bookmarkButton.setOnClickListener(view -> {
                                    // Calls SaveArticle function
                                    SaveArticle(response.getArticles().get(finalI).getTitle(), response.getArticles().get(finalI).getUrl(), response.getArticles().get(finalI).getUrlToImage(), bookmarkButton); // Pass the article to be saved
                                });

                                // Adds the TextView and Button to the LinearLayout
                                articleLayout.addView(articleTextView);
                                articleLayout.addView(bookmarkButton);

                                // Sets layout params for the LinearLayout
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                );
                                params.setMargins(0, 16, 0, 16); // Margins between each article
                                articleLayout.setLayoutParams(params);

                                // Adds the articleLayout
                                articleContainer.addView(articleLayout);
                            }
                        } else {
                            Toast.makeText(getActivity(), "No articles found for the query: " + query, Toast.LENGTH_SHORT).show();
                        }
                    }

                    private void SaveArticle(String title, String url, String imageUrl, Button bookmarkButton) {
                        // Change the bookmark button to reflect the saved state
                        bookmarkButton.setBackgroundResource(R.drawable.bookmarklogo2);

                        // Get the current user's ID
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        // Reference to the user's article folder in the database
                        DatabaseReference userArticlesRef = database.child(userId).child("Bookmarks");

                        userArticlesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // Create a map for the article details (URL and imageUrl)
                                Map<String, Object> articleDetails = new HashMap<>();
                                articleDetails.put("url", url);
                                articleDetails.put("imageUrl", imageUrl);

                                // Create a map to store the saved articles
                                Map<String, Object> savedArticles = new HashMap<>();

                                if (snapshot.exists()) {
                                    // If articles already exist, preserve them
                                    for (DataSnapshot articleSnapshot : snapshot.getChildren()) {
                                        String existingTitle = articleSnapshot.getKey();
                                        Map<String, Object> existingArticleDetails = (Map<String, Object>) articleSnapshot.getValue();
                                        savedArticles.put(existingTitle, existingArticleDetails); // Preserve existing articles
                                    }
                                }

                                // Add the new article with its title, URL, and image URL
                                savedArticles.put(title, articleDetails);

                                // Update the database with the new set of articles
                                userArticlesRef.updateChildren(savedArticles)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Successfully added the article
                                                Toast.makeText(getActivity(), "Article Bookmarked successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Failed to write data
                                                Toast.makeText(getActivity(), "Failed to update bookmarks: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle errors
                                Toast.makeText(getActivity(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("NewsApiError", "Error fetching articles: " + throwable.getMessage());
                        Toast.makeText(getActivity(), "Error fetching articles: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }


}
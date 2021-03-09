package com.sdp.movemeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class FirebaseUsersMainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.sdp.movemeet.MESSAGE";

    TextView fullName, email, phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_users_main);

        phone = findViewById(R.id.text_view_profile_phone);
        fullName = findViewById(R.id.text_view_profile_name);
        email = findViewById(R.id.text_view_profile_email);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        // Retrieving the data from the Firestore database
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                phone.setText(documentSnapshot.getString("phone"));
                fullName.setText(documentSnapshot.getString("fullName"));
                email.setText(documentSnapshot.getString("email"));
            }

        });

    }

    /* Called when the user taps the Go button */
    @SuppressWarnings("unused")
    public void sendMessage(View view) {
        Intent intent = new Intent(this, FirebaseUsersGreetingActivity.class);
        EditText editText = findViewById(R.id.edit_text_name);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut(); // this will do the logout of the user from Firebase
        Toast.makeText(FirebaseUsersMainActivity.this, "Logged out successfully.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), FirebaseUsersLogin.class)); // sending the user to the "Login" activity
        finish();
    }

}
package com.example.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseApp mFireBaseApp;
    private Button btnSpeechRecognition;
    private static final int SPEECH_REQUEST_CODE = 100;
    private static final String DB_URL = "https://se-lab2-85a48-default-rtdb.europe-west1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        mFireBaseApp = FirebaseApp.initializeApp(this);

        mDatabase = FirebaseDatabase.getInstance(DB_URL).getReference();

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance(mFireBaseApp);
        // Sign in the user anonymously
        signInAnonymously();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Add your item names to the list
        List<String> itemList = new ArrayList<>();
        itemList.add("Light");
        itemList.add("Door");
        itemList.add("Window");

        mAdapter = new MyAdapter(this, itemList, mDatabase);
        recyclerView.setAdapter(mAdapter);

        btnSpeechRecognition = findViewById(R.id.btn_speech_recognition);
        btnSpeechRecognition.setOnClickListener(v -> startSpeechRecognition());
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("signInAnonymously", "signInAnonymously:success");
                    } else {
                        Log.w("signInAnonymously", "signInAnonymously:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your command");
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "Speech recognition not supported on your device.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String recognizedText = results.get(0).toLowerCase();
            handleCommand(recognizedText);
        }
    }

    private void handleCommand(String command) {
        Log.i("handleCommand", "In handleCommand");
        command = command.toLowerCase().trim();
        if (command.contains("open")) {
            if (command.contains("door")) {
                mDatabase.child("door").setValue("open");
                Toast.makeText(this, "Opening door...", Toast.LENGTH_SHORT).show();
            } else if (command.contains("window")) {
                mDatabase.child("window").setValue("open");
                Toast.makeText(this, "Opening window...", Toast.LENGTH_SHORT).show();
            }
        } else if (command.contains("close")) {
            if (command.contains("door")) {
                mDatabase.child("door").setValue("closed");
                Toast.makeText(this, "Closing door...", Toast.LENGTH_SHORT).show();
            } else if (command.contains("window")) {
                mDatabase.child("window").setValue("closed");
                Toast.makeText(this, "Closing window...", Toast.LENGTH_SHORT).show();
            }
        } else if (command.contains("on")) {
            if (command.contains("light")) {
                mDatabase.child("light").setValue("on");
                Toast.makeText(this, "Turning light on...", Toast.LENGTH_SHORT).show();
            }
        } else if (command.contains("off")) {
            if (command.contains("light")) {
                mDatabase.child("light").setValue("off");
                Toast.makeText(this, "Turning light off...", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Command not recognized. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}

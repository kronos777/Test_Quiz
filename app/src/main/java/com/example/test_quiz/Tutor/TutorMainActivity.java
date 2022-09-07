package com.example.test_quiz.Tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.test_quiz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class TutorMainActivity extends AppCompatActivity {


    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference myRef;
    private boolean isTutor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTutors();
        setContentView(R.layout.activity_tutor_main);
    }



    public void checkForTutors() {

        auth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        myRef=database.getReference();

        myRef.child("tutors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Objects.requireNonNull(auth.getUid()))
                        .exists()&& Objects.requireNonNull(dataSnapshot.child(auth.getUid())
                        .child("authVerification").getValue()).toString().equals("yes")){
                    Toasty.info(getApplicationContext(),"Hello Tutor", Toasty.LENGTH_LONG).show();
                } else {
                    Toasty.info(getApplicationContext(),"Hello no Tutor", Toasty.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
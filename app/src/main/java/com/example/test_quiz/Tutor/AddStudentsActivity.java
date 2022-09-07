package com.example.test_quiz.Tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test_quiz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class AddStudentsActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference myRef;
    private EditText username, email, password, nameIdGroup;
    String authId;
    // private Button btnAddTutor = findViewById(R.id.btn_addTutor);
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_students);
    }

    public void addStudents(View view){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        username = findViewById(R.id.user_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        nameIdGroup = findViewById(R.id.nameIdGroup);
        auth = FirebaseAuth.getInstance();
        authId = auth.getUid();
        //   database = FirebaseDatabase.getInstance();
        //   myRef = database.getReference();
        avLoadingIndicatorView = findViewById(R.id.loader1);
        /*add tutor users*/
        String nameString = username.getText().toString().trim();
        String emailString = email.getText().toString().trim();
        String passwordString = password.getText().toString().trim();
        // String nameRecordString = nameRecord.getText().toString().trim();


        //  myRef.child("tutors").child("tutursOne").setValue(nameString);


        //nameRecordDb = nameRecordDb[0].toString().trim();;

        if (TextUtils.isEmpty(emailString)) {
            username.setError("Enter valid Email");
            return;
        }

        if (TextUtils.isEmpty(passwordString)) {
            password.setError("Password should be atleast length of 6!");
            return;
        }

        if (passwordString.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }





        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.smoothToShow();
        //create user
        auth.createUserWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(AddStudentsActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(AddStudentsActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        avLoadingIndicatorView.setVisibility(View.GONE);
                        avLoadingIndicatorView.smoothToHide();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toasty.error(AddStudentsActivity.this,"Authentication failed."+ task.getException(),
                                    Toasty.LENGTH_SHORT).show();
                        } else {

                            String currentUser = auth.getUid();

                            String nameString = username.getText().toString().trim();
                            String emailString = email.getText().toString().trim();
                            String passwordString = password.getText().toString().trim();
                            String nameIdGroupString = nameIdGroup.getText().toString().trim();
                            String authTutorString = authId.trim();
                            String usergroupidString = "perviy b";


                         //   mDatabase.child("students").child(authId).child(currentUser).child("usergroupid").setValue(nameIdGroupString);
                            mDatabase.child("students").child(currentUser).child("usergroupid").setValue(usergroupidString);
                            mDatabase.child("students").child(currentUser).child("name").setValue(nameString);
                            mDatabase.child("students").child(currentUser).child("email").setValue(emailString);
                            mDatabase.child("students").child(currentUser).child("password").setValue(passwordString);
                            mDatabase.child("students").child(currentUser).child("authId").setValue(authTutorString);



                            Objects.requireNonNull(auth.getCurrentUser()).sendEmailVerification();
                            Toasty.success(AddStudentsActivity.this,R.string.email_sent + currentUser,Toasty.LENGTH_SHORT).show();

                        }
                    }
                });
        /**/
        /*add tutor users*/


    }
}
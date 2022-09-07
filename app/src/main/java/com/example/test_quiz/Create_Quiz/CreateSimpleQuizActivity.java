package com.example.test_quiz.Create_Quiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.test_quiz.Misc_Section.ImageUtils;
import com.example.test_quiz.R;
import com.example.test_quiz.View.AddDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicMarkableReference;

import es.dmoral.toasty.Toasty;


public class CreateSimpleQuizActivity extends AppCompatActivity {


    private ImageView imageView;
    private Button update;
   // private Uri mImageUri;
    private EditText nameQustion;
    private EditText descriptionQustion;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_simple_quiz);


        nameQustion = findViewById(R.id.nameRecord);
        descriptionQustion = findViewById(R.id.email);
        imageView = findViewById(R.id.userImage);
        update = findViewById(R.id.btn_addTutor);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

    }

    public void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

        Toasty.info(getApplicationContext(),"Hello User" + intent, Toasty.LENGTH_LONG).show();

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),mImageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                String userUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                //uploading the image
                UploadTask uploadTask = FirebaseStorage.getInstance()
                        .getReference().child(userUid).putFile(mImageUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageUtils.displayRoundImageFromUrl(CreateSimpleQuizActivity.this, mImageUri.toString(), imageView);
            //Picasso.get().load(mImageUri).into(imageView);
        }
    }

    public String saveUploadFile(String path) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        return  path;
    }


}
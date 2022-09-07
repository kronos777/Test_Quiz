package com.example.test_quiz.Students;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test_quiz.Attempt_Quiz_Section.AttemptTest;
import com.example.test_quiz.Model.Question;
import com.example.test_quiz.Model.Test;
import com.example.test_quiz.NotificationActivity.NotificationService;
import com.example.test_quiz.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class StudentMainActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference myRef;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private ListView listView;
    private StudentMainActivity.TestAdapter testAdapter;
    private String authId;
    private String usergroupid;
    private int lastPos = -1;

    ArrayList<Test> tests=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
       /* Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));*/
       // setSupportActionBar(toolbar);
        avLoadingIndicatorView = findViewById(R.id.loader1);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.show();
//        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);
        database= FirebaseDatabase.getInstance();
        myRef=database.getReference();
        listView=findViewById(R.id.test_listview);
        testAdapter=new StudentMainActivity.TestAdapter(StudentMainActivity.this,tests);
        listView.setAdapter(testAdapter);
      //  getQues();

      /*  String TutorId = getCurrentReferenceTestAuthId();
        String GroupId = getCurrentReferenceTestGroupId();
        Toast.makeText(StudentMainActivity.this,
                TutorId,
                Toast.LENGTH_SHORT).show();*/
        getQuesStudents();

       /* Toast.makeText(StudentMainActivity.this,
                TutorId,
                Toast.LENGTH_SHORT).show();
        Toast.makeText(StudentMainActivity.this,
                GroupId,
                Toast.LENGTH_SHORT).show();*/
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        stopService(new Intent(StudentMainActivity.this, NotificationService.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getQues(){
        //addListenerForSingleValueEvent
        myRef.child("tests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tests.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Test t=new Test();
                    t.setName(snapshot.getKey());
                    t.setTime(Long.parseLong(snapshot.child("Time").getValue().toString()));
                    ArrayList<Question> ques=new ArrayList<>();
                    for (DataSnapshot qSnap:snapshot.child("Questions").getChildren()){
                        ques.add(qSnap.getValue(Question.class));
                    }
                    t.setQuestions(ques);
                    tests.add(t);

                }
                testAdapter.dataList=tests;
                testAdapter.notifyDataSetChanged();
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.hide();
                Log.e("The read success: " ,"su"+tests.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.hide();
                Log.e("The read failed: " ,databaseError.getMessage());
            }
        });
    }


    public void getQuesStudents() {

        final String[] tutorId = new String[1];
        final String[] groupId = new String[1];

        auth = FirebaseAuth.getInstance();


        myRef.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Objects.requireNonNull(auth.getUid()))
                        .exists()) {

                    groupId[0] = (String) dataSnapshot.child(Objects.requireNonNull(auth.getUid())).child("usergroupid").getValue().toString().trim();
                    //    authId = (String) dataSnapshot.child(Objects.requireNonNull(auth.getUid())).child("authId").getValue();
                    //      Toast.makeText(StudentMainActivity.this, String.valueOf(authId), Toast.LENGTH_SHORT).show();
                    //   Toast.makeText(StudentMainActivity.this, String.valueOf(usergroupid), Toast.LENGTH_SHORT).show();
                    //  Toasty.info(getApplicationContext(),"Hello Student", Toasty.LENGTH_LONG).show();
                } else {
                    hideAllMenu();
                    Toasty.info(getApplicationContext(),"Hello no Student", Toasty.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Objects.requireNonNull(auth.getUid()))
                        .exists()) {

                    // usergroupid = (String) dataSnapshot.child(Objects.requireNonNull(auth.getUid())).child("usergroupid").getValue();
                    tutorId[0] = (String) dataSnapshot.child(Objects.requireNonNull(auth.getUid())).child("authId").getValue().toString().trim();
                    //      Toast.makeText(StudentMainActivity.this, String.valueOf(authId), Toast.LENGTH_SHORT).show();
                    //   Toast.makeText(StudentMainActivity.this, String.valueOf(usergroupid), Toast.LENGTH_SHORT).show();
                    //  Toasty.info(getApplicationContext(),"Hello Student", Toasty.LENGTH_LONG).show();
                } else {
                    hideAllMenu();
                    //  Toasty.info(getApplicationContext(),"Hello no Student", Toasty.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    /**/


        myRef.child("tests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tests.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                    String dataGroup = snapshot.child("GroupId").getValue().toString().trim();
                    String dataTutor = snapshot.child("TutorId").getValue().toString().trim();

                    if (dataGroup.equals(groupId[0]) && dataTutor.equals(tutorId[0])) {
                        Test t=new Test();
                        t.setName(snapshot.getKey());
                        t.setTime(Long.parseLong(snapshot.child("Time").getValue().toString()));
                        ArrayList<Question> ques=new ArrayList<>();
                        for (DataSnapshot qSnap:snapshot.child("Questions").getChildren()){
                            ques.add(qSnap.getValue(Question.class));
                        }
                        t.setQuestions(ques);
                        tests.add(t);

                    }

             }
                testAdapter.dataList=tests;
                testAdapter.notifyDataSetChanged();
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.hide();
                Log.e("The read success: " ,"su"+tests.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.hide();
                Log.e("The read failed: " ,databaseError.getMessage());
            }
        });
    }

/*
    public String getCurrentReferenceTestGroupId () {
        //get tutor id
        //get group id
     //authId
        //usergroupid
        auth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        myRef=database.getReference();

        myRef.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Objects.requireNonNull(auth.getUid()))
                        .exists()) {

                    usergroupid = (String) dataSnapshot.child(Objects.requireNonNull(auth.getUid())).child("usergroupid").getValue();
                //    authId = (String) dataSnapshot.child(Objects.requireNonNull(auth.getUid())).child("authId").getValue();
             //      Toast.makeText(StudentMainActivity.this, String.valueOf(authId), Toast.LENGTH_SHORT).show();
                //   Toast.makeText(StudentMainActivity.this, String.valueOf(usergroupid), Toast.LENGTH_SHORT).show();
                  //  Toasty.info(getApplicationContext(),"Hello Student", Toasty.LENGTH_LONG).show();
                } else {
                    hideAllMenu();
                    Toasty.info(getApplicationContext(),"Hello no Student", Toasty.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return usergroupid;
    }


    public String getCurrentReferenceTestAuthId () {
        //get tutor id
        //get group id
        //authId
        //usergroupid
        auth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        myRef=database.getReference();

        myRef.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Objects.requireNonNull(auth.getUid()))
                        .exists()) {

                    // usergroupid = (String) dataSnapshot.child(Objects.requireNonNull(auth.getUid())).child("usergroupid").getValue();
                        authId = (String) dataSnapshot.child(Objects.requireNonNull(auth.getUid())).child("authId").getValue();
                    //      Toast.makeText(StudentMainActivity.this, String.valueOf(authId), Toast.LENGTH_SHORT).show();
                    //   Toast.makeText(StudentMainActivity.this, String.valueOf(usergroupid), Toast.LENGTH_SHORT).show();
                    //  Toasty.info(getApplicationContext(),"Hello Student", Toasty.LENGTH_LONG).show();
                } else {
                    hideAllMenu();
                  //  Toasty.info(getApplicationContext(),"Hello no Student", Toasty.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return authId;
    }
*/
    public void hideAllMenu () {
        NavigationView navigationView =  findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.tutors_tutorMenuGroup).setVisible(false);
        Toasty.info(getApplicationContext(),"Hello User", Toasty.LENGTH_LONG).show();
    }

    public class TestAdapter extends ArrayAdapter<Test> implements Filterable {
        public ArrayList<Test> dataList;
        private Context mContext;
        private int lastPos;

        public TestAdapter( Context context,ArrayList<Test> list) {
            super(context, 0 , list);
            mContext = context;
            dataList = list;
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;
            if(listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.test_item,parent,false);

            ((ImageView)listItem.findViewById(R.id.item_imageView)).
                    setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_appicon));

            ((TextView)listItem.findViewById(R.id.item_textView))
                    .setText(dataList.get(position).getName()+" : "+dataList.get(position).getTime()+"Min");

            ((Button)listItem.findViewById(R.id.item_button)).setText("Ответить");

            (listItem.findViewById(R.id.item_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, AttemptTest.class);
                    intent.putExtra("Questions",dataList.get(position));
                    intent.putExtra("TESTNAME",dataList.get(position).getName());
                    startActivity(intent);
                }
            });

            Animation animation = AnimationUtils.loadAnimation(getContext(),
                    (position > lastPos) ? R.anim.up_from_bottom : R.anim.down_from_top);
            (listItem).startAnimation(animation);
            lastPos = position;

            return listItem;
        }
    }


}
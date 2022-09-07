package com.example.test_quiz.Tutor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.test_quiz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class TutorStudentsGroupActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    public TextView groupStudentName;
    public TextView groupStudentQuery;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_students_group);
    }

    public static String transliterate(String message){
        char[] abcCyr =   {' ','а','б','в','г','д','е','ё', 'ж','з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х', 'ц','ч', 'ш','щ','ъ','ы','ь','э', 'ю','я','А','Б','В','Г','Д','Е','Ё', 'Ж','З','И','Й','К','Л','М','Н','О','П','Р','С','Т','У','Ф','Х', 'Ц', 'Ч','Ш', 'Щ','Ъ','Ы','Ь','Э','Ю','Я','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        String[] abcLat = {" ","a","b","v","g","d","e","e","zh","z","i","y","k","l","m","n","o","p","r","s","t","u","f","h","ts","ch","sh","sch", "","i", "","e","ju","ja","A","B","V","G","D","E","E","Zh","Z","I","Y","K","L","M","N","O","P","R","S","T","U","F","H","Ts","Ch","Sh","Sch", "","I", "","E","Ju","Ja","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++ ) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }

    /**/
    public void addUsersGoup(View view) {

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        groupStudentName = findViewById(R.id.nameUsers);
        groupStudentQuery = findViewById(R.id.user_qustions);

        /*add tutor users*/
        String groupStudentNameString = groupStudentName.getText().toString().trim();
        String  groupStudentQueryString =  groupStudentQuery.getText().toString().trim();

        String currentUser = auth.getUid();
        String currentGroupNameLat = transliterate(groupStudentNameString);
        // String groupUniqueNameLat = currentUser + currentGroupNameLat;
        //String currentGroupUserUid = auth.getUid() + transliterate(nameString);


        mDatabase.child("groupusers").child(currentUser).child(currentGroupNameLat).child("namestudents").setValue(groupStudentNameString);
        mDatabase.child("groupusers").child(currentUser).child(currentGroupNameLat).child("qustionsnames").setValue(groupStudentQueryString);
        mDatabase.child("groupusers").child(currentUser).child(currentGroupNameLat).child("groupid").setValue(currentUser + currentGroupNameLat);
        //mDatabase.child("groupQuery").child(currentUser).child("currentgroupuseruid").setValue(currentGroupUserUid);
        Toasty.success(TutorStudentsGroupActivity.this,"Ваша группа учеников добавлена!",Toasty.LENGTH_SHORT).show();

    }

}
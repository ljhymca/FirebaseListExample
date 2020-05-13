package org.techtown.firebaselistexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //전역변수
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView =findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);//리사이클러 뷰 성능강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); //user객체를 담을 array list

        database = FirebaseDatabase.getInstance();//파이어베이스 db기능 연동

        databaseReference = database.getReference("User");//DB 테이블 연결결,firebase콘솔 user
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스db 받아오는 곳
                arrayList.clear();//기존 배열 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {//반복문으로 datalist추출
                    User user = snapshot.getValue(User.class);//만들어 둔 user 객체에 데이트를 담는다.
                    arrayList.add(user);//다음 데이터들을 배열 리스트에 넣고 리사이클러 뷰로 보냄
                }
                adapter.notifyDataSetChanged();//list저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //db 전송중 에러 발생시
                Log.e("MainActivity", String.valueOf(databaseError.toException()));
            }
        });

        adapter = new CustomAdapter(arrayList, this);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결

    }
}

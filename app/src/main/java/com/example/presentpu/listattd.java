package com.example.presentpu;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class listattd extends AppCompatActivity {

    Context context;
    RecyclerView recyclerView;
    ArrayList<attendanceModel> attendanceModelArrayList;
    myAdapter myAdapter;
    FirebaseFirestore firebaseFirestore;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listattd);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        firebaseFirestore = FirebaseFirestore.getInstance();
        attendanceModelArrayList = new ArrayList<>();
        myAdapter = new myAdapter(listattd.this, attendanceModelArrayList);

        recyclerView.setAdapter(myAdapter);

        CatchResult();

    }



    private void CatchResult (){
        firebaseFirestore.collection("attendance").orderBy("Name", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("Name");
                                String id = document.getString("Student Id");
                                String dttm = document.getString("DateTime");
                                String desc = document.getString("Description");
                                String imageUrl = document.getString("imageUrl");

                                // Tambahkan item ke dalam list
                                attendanceModelArrayList.add(new attendanceModel(name, id, dttm, desc, imageUrl));
                            }

                            // Perbarui RecyclerView setelah mendapatkan data
                            myAdapter.notifyDataSetChanged();
                        } else {
                        }
                    }
                });
    }
}
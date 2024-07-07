package com.example.presentpu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    EditText fullName,studentID,email,password;
    Button lgBtn,RegBtn;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CheckBox isLecturerBox,isStudentBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.Fname);
        studentID = findViewById(R.id.stdID);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        lgBtn = findViewById(R.id.btLog);
        RegBtn = findViewById(R.id.btReg);

        isLecturerBox = findViewById(R.id.isLecBox);
        isStudentBox = findViewById(R.id.isStdBox);

        isStudentBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    isLecturerBox.setChecked(false);
                }
            }
        });

        isLecturerBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    isStudentBox.setChecked(false);
                }
            }
        });

        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(fullName);
                checkField(studentID);
                checkField(email);
                checkField(password);

                // checkBox validation
                if (!(isLecturerBox.isChecked() || isStudentBox.isChecked())){
                    Toast.makeText(register.this, "Select The Account Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (valid){
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(register.this, "Account Created", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fStore.collection("Users").document(user.getUid());
                            Map<String,Object> userInfo = new HashMap<>();
                            userInfo.put("FullName",fullName.getText().toString());
                            userInfo.put("Student ID", studentID.getText().toString());
                            userInfo.put("Email",email.getText().toString());
                            //speicfy if the user is admin
                            if (isLecturerBox.isChecked()){
                                userInfo.put("isLecturer", "1");
                            }
                            if (isStudentBox.isChecked()){
                                userInfo.put("isStudent", "1");
                            }

                            df.set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    if (isLecturerBox.isChecked()){
                                        startActivity(new Intent(getApplicationContext(),admin.class));
                                        finish();
                                    }
                                    if (isStudentBox.isChecked()){
                                        startActivity(new Intent(getApplicationContext(),dashboard.class));
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(register.this,"Failed to Create Account",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(register.this,"Failed to Create Account",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        lgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        if (FirebaseApp.getApps(this).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }

    public boolean checkField(EditText textField){
        if (textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }
        return valid;
    }
}
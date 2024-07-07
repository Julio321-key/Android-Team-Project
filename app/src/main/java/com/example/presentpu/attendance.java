package com.example.presentpu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class attendance extends AppCompatActivity {
    EditText FullName, StudentID, DateTime, Description;
    private ImageButton Camera;
    Button submit;
    private FirebaseFirestore firebaseFirestore;
    Bitmap bitmap;
    FrameLayout frameLayout;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    FirebaseAuth firebaseAuth;
    private Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        Camera = findViewById(R.id.imageSelfie);
        submit = findViewById(R.id.Submit);
        FullName = findViewById(R.id.inputNama);
        StudentID = findViewById(R.id.ID);
        DateTime = findViewById(R.id.inputTanggal);
        Description = findViewById(R.id.inputKeterangan);
        frameLayout = findViewById(R.id.FrameL);

        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference= storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        DateTime.setOnClickListener(v -> {
            Calendar DatePresent = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener Date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    DatePresent.set(Calendar.YEAR, year);
                    DatePresent.set(Calendar.MONTH, month);
                    DatePresent.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String strFromatDefault = "dd MMMM yyyy HH:mm";
                    SimpleDateFormat simpleDateFormat= new SimpleDateFormat(strFromatDefault, Locale.getDefault());
                    DateTime.setText(simpleDateFormat.format(DatePresent.getTime()));
                }
            };
            new DatePickerDialog(
                    attendance.this, Date,
                    DatePresent.get(Calendar.YEAR),
                    DatePresent.get(Calendar.MONTH),
                    DatePresent.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadDataPresent();
            }
        });
    }

    public void openCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Camera.setImageBitmap(imageBitmap);

            imageUri = getImageUri(this, imageBitmap);
        }
    }

    private Uri getImageUri(Context context, Bitmap bitmap){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    private void UploadDataPresent(){
        String name = FullName.getText().toString();
        String id = StudentID.getText().toString();
        String dttm = DateTime.getText().toString();
        String desc = Description.getText().toString();

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(id) && TextUtils.isEmpty(dttm) && TextUtils.isEmpty(desc) && imageUri == null){
            Toast.makeText(this, "Please Fill All fields", Toast.LENGTH_SHORT).show();
        }

        String imageName = "image_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageReference.child("images/" + imageName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {

                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = imageUri.toString();

                        Map<String, Object> infoData = new HashMap<>();
                        infoData.put("Name", name);
                        infoData.put("Student Id", id);
                        infoData.put("DateTime", dttm);
                        infoData.put("Description", desc);
                        infoData.put("imageUrl", imageUrl);

                        firebaseFirestore.collection("attendance")
                                .add(infoData)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(attendance.this, "Data submitted successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(attendance.this, "Failed to submit data", Toast.LENGTH_SHORT).show();
                                });
            });
        }).addOnFailureListener(e -> {
                    Toast.makeText(attendance.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }
}

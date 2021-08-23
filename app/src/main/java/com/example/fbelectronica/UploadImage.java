package com.example.fbelectronica;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.StorageMetadata;

import java.io.File;

public class UploadImage extends AppCompatActivity {

    private TextView lblText;
    //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    //DatabaseReference rootChild = databaseReference.child("txt");

    private StorageReference storageReference;
    private static final int GALLERY_INTENT = 1;
    private Button btnAtras;
    private ImageView imgFoto;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        storageReference = FirebaseStorage.getInstance().getReference();

        lblText = (TextView) findViewById(R.id.lblText);
        imgFoto = (ImageView) findViewById(R.id.imgFoto);
        progressDialog = new ProgressDialog(this);
        btnAtras = (Button) findViewById(R.id.btnAtras);

        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            progressDialog.setTitle("Subiendo...");
            progressDialog.setMessage("Subiendo Foto");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Uri uri = data.getData();

            StorageReference filePath = storageReference.child("img").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Uri des = taskSnapshot.getUploadSessionUri();
                    //Glide.with(MainActivity.this).load(des).into(imgFoto);
                    Toast.makeText(UploadImage.this,"Subida",Toast.LENGTH_SHORT).show();
                    //Log.i("URL", String.valueOf(des));
                    //Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();

                    //here
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();

                    final String sdownload_url = String.valueOf(downloadUrl);
                    Log.i("URL", sdownload_url);
                }
            });
        }
    }
}
package com.example.fbelectronica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class Login extends AppCompatActivity {

    private EditText txtEmaiil;
    private EditText txtPass;
    private Button btnLogin;

    private String email;
    private String pass;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        txtEmaiil = (EditText) findViewById(R.id.txtUser);
        txtPass = (EditText) findViewById(R.id.txtPass);
        btnLogin = (Button) findViewById(R.id.btnInicio);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = txtEmaiil.getText().toString();
                pass = txtPass.getText().toString();

                if (!email.isEmpty() && !pass.isEmpty()){
                    loginUser();
                }
                else {
                    Toast.makeText(Login.this,"Llenar Campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser(){
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(Login.this, ListaActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(Login.this, "No se pudo iniciar sesion, comprobar datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
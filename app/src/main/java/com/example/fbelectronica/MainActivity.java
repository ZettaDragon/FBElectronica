package com.example.fbelectronica;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fbelectronica.objetos.Productos;
import com.example.fbelectronica.objetos.ReferenciasFirebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnGuardar;
    private Button btnListar;
    private Button btnBorrar;
    private Button btnSeleccionar;
    private TextView txtMarca;
    private TextView txtDescripcion;
    private TextView txtPrecio;
    private ImageView imgFoto;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    Productos savedProductos;
    private String id;
    private static final int GALLERY_INTENT = 1;
    private ProgressDialog progressDialog;
    private StorageReference storageReference;
    private Uri imgUri;
    Productos nP;
    String sdownload_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        setEvents();
    }

    private void initComponents(){
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = this.firebaseDatabase.getReferenceFromUrl(ReferenciasFirebase.URL_DATABASE + ReferenciasFirebase.DATABASE_NAME + "/" + ReferenciasFirebase.TABLE_NAME);
        this.txtMarca = findViewById(R.id.txtMarca);
        this.txtDescripcion = findViewById(R.id.txtDescripcion);
        this.txtPrecio = findViewById(R.id.txtPrecio);
        this.btnSeleccionar = findViewById(R.id.btnSelectFoto);
        this.btnListar = findViewById(R.id.btnRegresar);
        this.btnGuardar = findViewById(R.id.btnGuardar);
        this.btnBorrar = findViewById(R.id.btnBorrar);
        this.imgFoto = findViewById(R.id.imgFoto);
        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        savedProductos = null;
    }

    private void setEvents(){
        this.btnSeleccionar.setOnClickListener(this);
        this.btnListar.setOnClickListener(this);
        this.btnGuardar.setOnClickListener(this);
        this.btnBorrar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if (isNetworkAvailable()){
            switch (v.getId()){
                case R.id.btnGuardar:
                    boolean completo = true;
                    /*if (txtMarca.getText().toString().equals("")){
                        txtMarca.setError("Rellenarar Campo");
                        completo = false;
                    }
                    if (txtDescripcion.getText().toString().equals("")){
                        txtDescripcion.setError("Rellenarar Campo");
                        completo = false;
                    }
                    if (txtPrecio.getText().toString().equals("")){
                        txtPrecio.setError("Rellenarar Campo");
                        completo = false;
                    }*/

                    if (completo){
                        nP = new Productos();

                        nP.setMarca(txtMarca.getText().toString());
                        nP.setDescripcion(txtDescripcion.getText().toString());
                        nP.setPrecio(Double.parseDouble(txtPrecio.getText().toString()));
                        nP.setFoto(sdownload_url);

                        /*UploadImage uploadImage = new UploadImage();
                        String i = uploadImage.cambiarUrl(id,nP);

                        System.out.println(nP.getFoto());*/
                        //Log.e("i", i);


                       // nP.setFoto(i);

                        /*UploadImage uploadImage = new UploadImage();
                        String ui = uploadImage.sdownload_url;*/
                        /*nP.setFoto(ui);
                        Log.i("SU",uploadImage.sdownload_url);*/

                        if (savedProductos != null){
                            actualizar(id,nP);
                            Toast.makeText(getApplicationContext(),"Contacto actualizado con exito", Toast.LENGTH_SHORT).show();
                            txtMarca.requestFocus();
                        }
                    }
                    break;
                case R.id.btnRegresar:
                    Intent i= new Intent(MainActivity.this,ListaActivity.class);
                    limpiar();
                    startActivityForResult(i,0);
                    break;
                case R.id.btnSelectFoto:
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("image/*");
                    startActivityForResult(intent,GALLERY_INTENT);
                    /*Intent in = new Intent(MainActivity.this, UploadImage.class);
                    startActivityForResult(in,0);*/
                    break;
                case R.id.btnBorrar:
                    borrarProducto(savedProductos.get_ID());
                    Toast.makeText(getApplicationContext(), "Producto Eliminado", Toast.LENGTH_SHORT).show();
                    limpiar();
                    break;
            }
        }else {
            Toast.makeText(getApplicationContext(), "Se necesita tener conexion a internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void borrarProducto(String childIndex){
        databaseReference.child(String.valueOf(childIndex)).removeValue();
    }

    /*public void agregar(Productos c) {
        DatabaseReference newContactoReference = databaseReference.push();
        //obtener el id del registro y setearlo
        String id = newContactoReference.getKey();
        c.set_ID(id);
        newContactoReference.setValue(c);
    }*/

    public void actualizar(String id, Productos p) {
        //actualizar un objeto al nodo referencia
        p.set_ID(id);
        databaseReference.child(String.valueOf(id)).setValue(p);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    public void limpiar(){
        savedProductos = null;
        txtMarca.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtMarca.requestFocus();
        imgFoto.setImageResource(R.drawable.com);
        id = "";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);

        if (intent != null){
            Bundle bundle  = intent.getExtras();
            if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
                progressDialog.setTitle("Subiendo...");
                progressDialog.setMessage("Subiendo Foto");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Uri uri = intent.getData();

                StorageReference filePath = storageReference.child("img").child(uri.getLastPathSegment());
                filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Uri des = taskSnapshot.getUploadSessionUri();
                        //Glide.with(MainActivity.this).load(des).into(imgFoto);
                        Toast.makeText(MainActivity.this,"Subida",Toast.LENGTH_SHORT).show();
                        //Log.i("URL", String.valueOf(des));
                        //Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();

                        //here
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                        Uri downloadUrl = urlTask.getResult();

                        sdownload_url = String.valueOf(downloadUrl);
                        Log.i("URL", sdownload_url);
                    }
                });
            }
            else if (Activity.RESULT_OK == resultCode){
                Productos productos = (Productos) bundle.getSerializable("producto");
                savedProductos = productos;
                id = productos.get_ID();
                txtMarca.setText(productos.getMarca());
                txtDescripcion.setText(productos.getDescripcion());
                txtPrecio.setText(String.valueOf(productos.getPrecio()));
                Glide.with(MainActivity.this).load(productos.getFoto()).into(imgFoto);
            }
        }
    }
}

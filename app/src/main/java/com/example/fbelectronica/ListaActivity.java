package com.example.fbelectronica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fbelectronica.objetos.Productos;
import com.example.fbelectronica.objetos.ReferenciasFirebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListaActivity extends ListActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    final Context context = this;
    //private Button btnNuevo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl(ReferenciasFirebase.URL_DATABASE + ReferenciasFirebase.DATABASE_NAME + "/" + ReferenciasFirebase.TABLE_NAME);

        /*btnNuevo = (Button)findViewById(R.id.btnNuevo);
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });*/

        obtener();
    }

    public void obtener(){
        final ArrayList<Productos> productos = new ArrayList<Productos>();
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {

                Productos producto = snapshot.getValue(Productos.class);
                productos.add(producto);

                final MyArrayAdapter arrayAdapter = new MyArrayAdapter(context,R.layout.productos_items,productos);
                setListAdapter(arrayAdapter);

            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        };
        databaseReference.addChildEventListener(listener);
    }

    class MyArrayAdapter extends ArrayAdapter<Productos>{

        Context context;
        int textView;
        ArrayList<Productos> productosArrayList;

        public MyArrayAdapter(Context context, int textView, ArrayList<Productos> productosArrayList){
            super(context,textView,productosArrayList);
            this.context = context;
            this.textView = textView;
            this.productosArrayList = productosArrayList;
        }

        public View getView(final int position, View convertView, ViewGroup viewGroup){
            LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(this.textView,null);
            ImageView imgFoto = (ImageView) view.findViewById(R.id.imgFoto);
            TextView lblMarca = (TextView) view.findViewById(R.id.lblMarca);
            TextView lblDescripcion = (TextView) view.findViewById(R.id.lblDescripcion);
            TextView lblPrecio = (TextView) view.findViewById(R.id.lblPrecio);
            Button btnVer = (Button) view.findViewById(R.id.btnVer);

            lblMarca.setText(productosArrayList.get(position).getMarca());
            lblDescripcion.setText(productosArrayList.get(position).getDescripcion());
            String pre = String.valueOf(productosArrayList.get(position).getPrecio());
            lblPrecio.setText(pre);
            Glide.with(context).load(productosArrayList.get(position).getFoto()).into(imgFoto);

            btnVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("producto", productosArrayList.get(position));
                    Intent i = new Intent();
                    i.putExtras(bundle);
                    setResult(Activity.RESULT_OK,i);
                    finish();
                }
            });

            return view;
        }
    }


}
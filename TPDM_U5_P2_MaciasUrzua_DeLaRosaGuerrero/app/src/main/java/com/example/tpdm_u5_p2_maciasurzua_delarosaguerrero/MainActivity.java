package com.example.tpdm_u5_p2_maciasurzua_delarosaguerrero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;

    EditText name,cel;
    Button logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        name = findViewById(R.id.name);
        cel = findViewById(R.id.cel);
        logIn = findViewById(R.id.logIn);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> a = new HashMap<>();
                a.put("name",name.getText().toString());
                db.collection("players").document(cel.getText().toString())
                .set(a)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent a = new Intent(MainActivity.this,Main2Activity.class);
                        a.putExtra("name", name.getText().toString());
                        a.putExtra("cel", cel.getText().toString());
                        startActivity(a);
                    }
                });

            }
        });
    }
}

package com.example.tpdm_u5_p2_maciasurzua_delarosaguerrero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    FirebaseFirestore db;

    Button create,join;
    String name, cel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        db = FirebaseFirestore.getInstance();

        create = findViewById(R.id.create);
        join = findViewById(R.id.join);

        name = getIntent().getStringExtra("name");
        cel = getIntent().getStringExtra("cel");

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> a = new HashMap<>();
                a.put("p1",name);
                a.put("p2","0");
                a.put("tp1","0");
                a.put("tp2","0");
                db.collection("match").document(cel)
                .set(a)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent a = new Intent(Main2Activity.this, Main4Activity.class);
                        a.putExtra("cel",cel);
                        a.putExtra("host",true);
                        startActivity(a);
                    }
                });
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Main2Activity.this, Main3Activity.class);
                a.putExtra("name",name);
                startActivity(a);
            }
        });
    }
}

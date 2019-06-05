package com.example.tpdm_u5_p2_maciasurzua_delarosaguerrero;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main3Activity extends AppCompatActivity {

    FirebaseFirestore db;

    String name;

    ListView list;
    List<Map> matches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        db = FirebaseFirestore.getInstance();

        list = findViewById(R.id.list);
        matches = new ArrayList();
        name = getIntent().getStringExtra("name");

        fillList();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final DocumentReference a = db.collection("match").document(matches.get(position).get("cel").toString());
                a.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot d = task.getResult();
                            Match m = d.toObject(Match.class);
                            Map<String, Object> a = new HashMap<>();
                            a.put("cel", d.getId().toString());
                            a.put("p1", m.getP1());
                            a.put("p2", name);
                            a.put("tp1",m.getTp1());
                            a.put("tp2",m.getTp2());

                            db.collection("match").document(a.get("cel").toString())
                            .set(a)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent a = new Intent(Main3Activity.this, Main4Activity.class);
                                    a.putExtra("cel",matches.get(position).get("cel").toString());
                                    a.putExtra("host",false);
                                    startActivity(a);
                                }
                            });
                        }
                    }
                });

            }
        });

    }

    public void fillList() {
        db.collection("match").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() == 0) {
                    Toast.makeText(Main3Activity.this, "No hay Partidas",Toast.LENGTH_LONG);
                    return;
                }
                for (QueryDocumentSnapshot temp: queryDocumentSnapshots) {
                    Match m = temp.toObject(Match.class);
                    Map<String, Object> a = new HashMap<>();
                    a.put("cel", temp.getId().toString());
                    a.put("p1", m.getP1());
                    a.put("p2", m.getP2());
                    a.put("tp1",m.getTp1());
                    a.put("tp2",m.getTp2());

                    matches.add(a);
                }
                loadMatches();
            }
        });
    }

    private void loadMatches () {
        String[] lista = new String[matches.size()];
        for (int i = 0; i < lista.length; i++) {
            lista[i] = matches.get(i).get("cel").toString() + " - " + matches.get(i).get("p1").toString();
        }
        ArrayAdapter<String> a = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
        list.setAdapter(a);
    }
}

package com.example.tpdm_u5_p2_maciasurzua_delarosaguerrero;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Main4Activity extends AppCompatActivity {

    FirebaseFirestore db;

    String cel;
    boolean host;

    LinearLayout fondo;
    TextView youN, otherN, win;
    ImageView youA, otherA;

    SensorManager mSensorManager;
    Sensor mSensorAcc;
    SensorEventListener listener;

    boolean can;
    int option;
    int tp1, tp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (mSensorAcc == null) {
            Toast.makeText(this, "No hay acelerómetro", Toast.LENGTH_LONG).show();
        } else {
            listener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                        int minimum = 25;
                        if (Math.abs(event.values[0] + event.values[1] + event.values[2]) > minimum && can) {
                            can = false;
                            option = (int) Math.floor(Math.random() * (3 - 1 + 1) + 1);

                            final DocumentReference a = db.collection("match").document(cel);
                            a.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()) {
                                        DocumentSnapshot d = task.getResult();
                                        Match m = d.toObject(Match.class);
                                        Map<String, Object> a = new HashMap<>();
                                        a.put("cel", d.getId().toString());
                                        a.put("p1", m.getP1());
                                        a.put("p2", m.getP2());
                                        if (host) {
                                            a.put("tp1", option+"");
                                            a.put("tp2", m.getTp2());
                                        } else {
                                            a.put("tp1", m.getTp1());
                                            a.put("tp2", option+"");
                                        }
                                        if (host) {
                                            switch (option) {
                                                case 0:
                                                    youA.setImageResource(R.drawable.carita);
                                                    break;
                                                case 1:
                                                    youA.setImageResource(R.drawable.piedra);
                                                    break;
                                                case 2:
                                                    youA.setImageResource(R.drawable.papel);
                                                    break;
                                                case 3:
                                                    youA.setImageResource(R.drawable.tijera);
                                            }
                                        } else {
                                            switch (option) {
                                                case 0:
                                                    youA.setImageResource(R.drawable.carita);
                                                    break;
                                                case 1:
                                                    youA.setImageResource(R.drawable.piedra);
                                                    break;
                                                case 2:
                                                    youA.setImageResource(R.drawable.papel);
                                                    break;
                                                case 3:
                                                    youA.setImageResource(R.drawable.tijera);
                                            }
                                        }
                                        db.collection("match").document(a.get("cel").toString())
                                                .set(a);
                                    }
                                }
                            });
                        }
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
        }
        db = FirebaseFirestore.getInstance();

        cel = getIntent().getStringExtra("cel");
        host = getIntent().getBooleanExtra("host",false);

        youN = findViewById(R.id.youN);
        youA = findViewById(R.id.youA);
        otherN = findViewById(R.id.otherN);
        otherA = findViewById(R.id.otherA);
        win = findViewById(R.id.win);
        fondo = findViewById(R.id.fondo);

        setValues();

        option = 0;
        can = false;

        Thread check = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            win.setText("Alto");
                            fondo.setBackgroundColor(Color.WHITE);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.e("Error", e.getMessage());
                    }
                    final DocumentReference a = db.collection("match").document(cel);
                    a.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot d = task.getResult();
                                Match m = d.toObject(Match.class);
                                Map<String, Object> a = new HashMap<>();
                                a.put("tp1", m.getTp1());
                                a.put("tp2", m.getTp2());
                                a.put("p2", m.getP2());

                                tp1 = Integer.parseInt(a.get("tp1").toString());
                                tp2 = Integer.parseInt(a.get("tp2").toString());

                                if (host) {
                                    otherN.setText(a.get("p2").toString());
                                    switch (a.get("tp1").toString()) {
                                        case "0":
                                            youA.setImageResource(R.drawable.carita);
                                            break;
                                        case "1":
                                            youA.setImageResource(R.drawable.piedra);
                                            break;
                                        case "2":
                                            youA.setImageResource(R.drawable.papel);
                                            break;
                                        case "3":
                                            youA.setImageResource(R.drawable.tijera);
                                    }
                                    switch (a.get("tp2").toString()) {
                                        case "0":
                                            youA.setImageResource(R.drawable.carita);
                                            break;
                                        case "1":
                                            otherA.setImageResource(R.drawable.piedra);
                                            break;
                                        case "2":
                                            otherA.setImageResource(R.drawable.papel);
                                            break;
                                        case "3":
                                            otherA.setImageResource(R.drawable.tijera);
                                    }
                                } else {
                                    switch (a.get("tp2").toString()) {
                                        case "1":
                                            youA.setImageResource(R.drawable.piedra);
                                            break;
                                        case "2":
                                            youA.setImageResource(R.drawable.papel);
                                            break;
                                        case "3":
                                            youA.setImageResource(R.drawable.tijera);
                                    }
                                    switch (a.get("tp1").toString()) {
                                        case "1":
                                            otherA.setImageResource(R.drawable.piedra);
                                            break;
                                        case "2":
                                            otherA.setImageResource(R.drawable.papel);
                                            break;
                                        case "3":
                                            otherA.setImageResource(R.drawable.tijera);
                                    }
                                }
                            }
                        }
                    });

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Log.e("Error", e.getMessage());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            winner();
                        }
                    });
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        Log.e("Error", e.getMessage());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setFace();
                            win.setText("Agita");
                            fondo.setBackgroundColor(Color.WHITE);
                        }
                    });
                    can = true;
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Log.e("Error", e.getMessage());
                    }
                }
            }
        });
        check.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (host) {
            db.collection("match").document(cel)
            .delete();
        }
    }

    protected void onResume(){
        super.onResume();
        if(mSensorAcc != null){
            mSensorManager.registerListener(listener,mSensorAcc,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    protected void onPause(){
        super.onPause();
        if (mSensorAcc != null){
            mSensorManager.unregisterListener(listener);
        }
    }

    public void winner () {
        if(tp1 == 0 && tp2 == 1) {
            if (host){
                win.setText("PERDÍSTE");
                fondo.setBackgroundColor(Color.RED);
            } else {
                win.setText("GANASTE");
                fondo.setBackgroundColor(Color.GREEN);
            }
        }
        if(tp1 == 0 && tp2 == 2) {
            if (host){
                win.setText("PERDÍSTE");
                fondo.setBackgroundColor(Color.RED);
            } else {
                win.setText("GANASTE");
                fondo.setBackgroundColor(Color.GREEN);
            }
        }
        if(tp1 == 0 && tp2 == 3) {
            if (host){
                win.setText("PERDÍSTE");
                fondo.setBackgroundColor(Color.RED);
            } else {
                win.setText("GANASTE");
                fondo.setBackgroundColor(Color.GREEN);
            }
        }
        if (tp1 == 1 && tp2 == 0) {
            if (host){
                win.setText("GANASTE");
                fondo.setBackgroundColor(Color.RED);
            } else {
                win.setText("GANASTE");
                fondo.setBackgroundColor(Color.GREEN);
            }
        }
        if (tp1 == 1 && tp2 == 2) {
            if (host){
                win.setText("PERDÍSTE");
                fondo.setBackgroundColor(Color.RED);
            } else {
                win.setText("GANASTE");
                fondo.setBackgroundColor(Color.GREEN);
            }
        }
        if (tp1 == 1 && tp2 == 3) {
            if (host){
                win.setText("GANASTE");
                fondo.setBackgroundColor(Color.GREEN);
            } else {
                win.setText("PERDÍSTE");
                fondo.setBackgroundColor(Color.RED);
            }
        }
        if (tp1 == 2 && tp2 == 0) {
            if (host){
                win.setText("GANASTE");
                fondo.setBackgroundColor(Color.GREEN);
            } else {
                win.setText("PERDÍSTE");
                fondo.setBackgroundColor(Color.RED);
            }
        }
        if (tp1 == 2 && tp2 == 1) {
            if (host){
                win.setText("GANASTE");
                fondo.setBackgroundColor(Color.GREEN);
            } else {
                win.setText("PERDÍSTE");
                fondo.setBackgroundColor(Color.RED);
            }
        }
        if (tp1 == 2 && tp2 == 3) {
            if (host){
                win.setText("PERDÍSTE");
                fondo.setBackgroundColor(Color.RED);
            } else {
                win.setText("GANASTE");
                fondo.setBackgroundColor(Color.GREEN);
            }
        }
        if (tp1 == 3 && tp2 == 0) {
            if (host){
                win.setText("GANASTE");
                fondo.setBackgroundColor(Color.RED);
            } else {
                win.setText("PERDÍSTE");
                fondo.setBackgroundColor(Color.GREEN);
            }
        }
        if (tp1 == 3 && tp2 == 1) {
            if (host){
                win.setText("PERDÍSTE");
                fondo.setBackgroundColor(Color.RED);
            } else {
                win.setText("GANASTE");
                fondo.setBackgroundColor(Color.GREEN);
            }
        }
        if (tp1 == 3 && tp2 == 2) {
            if (host){
                win.setText("GANASTE");
                fondo.setBackgroundColor(Color.GREEN);
            } else {
                win.setText("PERDÍSTE");
                fondo.setBackgroundColor(Color.RED);
            }
        }
        if (tp1 == tp2) {
            if (host){
                win.setText("EMPATE");
                fondo.setBackgroundColor(Color.GRAY);
            } else {
                win.setText("EMPATE");
                fondo.setBackgroundColor(Color.GRAY);
            }
        }
    }

    public void setFace() {
        youA.setImageResource(R.drawable.carita);
        otherA.setImageResource(R.drawable.carita);
    }

    public void setValues () {
        final DocumentReference a = db.collection("match").document(cel);
        a.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot d = task.getResult();
                    Match m = d.toObject(Match.class);
                    Map<String, Object> a = new HashMap<>();
                    a.put("p1", m.getP1());
                    a.put("p2", m.getP2());

                    if(host) {
                        youN.setText(a.get("p1").toString());
                        otherN.setText(a.get("p2").toString());
                    } else {
                        youN.setText(a.get("p2").toString());
                        otherN.setText(a.get("p1").toString());
                    }
                    youA.setImageResource(R.drawable.carita);
                    otherA.setImageResource(R.drawable.carita);
                }
            }
        });

    }

}

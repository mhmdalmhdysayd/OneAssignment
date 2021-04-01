package com.example.mobilecloudcomputing_j01;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText editTextname;
    EditText editTextphone;
    EditText editTextaddress;
    TextView textViewResultData;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextname = findViewById(R.id.name);
        editTextphone = findViewById(R.id.phone);
        editTextaddress = findViewById(R.id.address);

        textViewResultData = findViewById(R.id.resultData);

        readData();

    }

    public void saveToFirebase(View v){
        String name=editTextname.getText().toString();
        String phone=editTextphone.getText().toString();
        String address=editTextaddress.getText().toString();

        Map<String,Object> product = new HashMap<>();
        product.put("Name",name);
        product.put("Phone",phone);
        product.put("Address",address);

        db.collection("contacts")
                .add(product)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG","Data Added Successful lisener");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG","Data Added Failure lisener");
                    }
                });


    }

    public void readData(){

        db.collection("contacts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        StringBuffer result = new StringBuffer();

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                result.append("Name : ").append(document.getData().get("Name")).append("\n\n")
                                        .append("Phone : ").append(document.getData().get("Phone")).append("\n\n")
                                        .append("Address : ").append(document.getData().get("Address")).append("\n--------------------------------------------------\n");
                            }
                            textViewResultData.setText(result);
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
package com.example.diegotest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.Map;

public class ShowDatabase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_database);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // fillScrollView();
        fillScrollViewFB();
    }

    void fillScrollViewFB() {
        LinearLayout layout = findViewById(R.id.showDBLinearLayout);
        layout.removeAllViews();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference docref = database.collection("users").document("Xxlucia69xX");
        Source src = Source.SERVER;
        //docref.delete();

        docref.get(src).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot result = task.getResult();
                Map<String, Object> info = result.getData();
                Toast.makeText(ShowDatabase.this, "TODO OK", Toast.LENGTH_LONG).show();
                TextView infoView = new TextView(ShowDatabase.this);
                infoView.setText(info.get("password").toString());
                layout.addView(infoView);
            }
        });

    }


    void fillScrollView() {
        LinearLayout layout = findViewById(R.id.showDBLinearLayout);
        DataBaseAux dbAux = new DataBaseAux(this);
        SQLiteDatabase database = dbAux.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM personas", null);
        layout.removeAllViews();

        if(cursor.moveToFirst()) { // si lo puedo mover al ppo, existe info
            do {
                int idIndex = cursor.getColumnIndex("id");
                int nameIndex = cursor.getColumnIndex("name");

                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);

                TextView infoView = new TextView(this);
                infoView.setText("id: " + id + " Name: " + name);

                Button deleteButton = new Button(this);
                deleteButton.setText("Delete register " + id);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int rowsAffected = database.delete("personas", "id=" + id, null);
                        if(rowsAffected > 0)
                            fillScrollView();
                    }
                });

                layout.addView(infoView);
                layout.addView(deleteButton);
            } while(cursor.moveToNext());
        }
    }

    public void changeToHome(View view) {
        startActivity(new Intent(ShowDatabase.this, MainActivity.class));
    }
}
package com.example.diegotest;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    DataBaseAux dbAux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbAux = new DataBaseAux(this);

        Button changeToImageActivity = findViewById(R.id.b_homeToImageActivity);
        changeToImageActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "CLICKADO", Toast.LENGTH_LONG).show();
                Log.d("Diego", "TODO OK");
                startActivity(new Intent(MainActivity.this, imageActivity.class));
            }
        });
    }

    public void addToSQL_DB(View view) {
        TextView nameTextView = findViewById(R.id.nameTextView);
        String nameString = nameTextView.getText().toString();

        // VALIDACION

        // DATABASE
        SQLiteDatabase database = dbAux.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", nameString);

        long result = database.insert("personas", null, values);

        if(result >= 0)
            Toast.makeText(this, "VALOR INSERTADO!!", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "ERROR AL INSERTAR", Toast.LENGTH_LONG).show();
    }

    public void readFromSQL_DB(View view) {
        SQLiteDatabase db = dbAux.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM personas WHERE id = 1", null);
        if(cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("name");
            String rowName = cursor.getString(columnIndex);
            Toast.makeText(this, "El nombre de la fila 1 es: " + rowName, Toast.LENGTH_LONG).show();
        }
    }
}
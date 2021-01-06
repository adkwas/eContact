package com.example.econtact;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RetrievePDF extends AppCompatActivity {

    ListView listView;
    List<putPDF>uploadedPDF;
    DatabaseReference databaseReference;
    String val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_p_d_f);
        listView = findViewById(R.id.listview_retrievePDF);
        uploadedPDF = new ArrayList<>();

        val = getIntent().getStringExtra("val");

        retrievedPDFFile();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                putPDF putPDF = uploadedPDF.get(position);

                if(putPDF.typeData.equals("PDF")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(putPDF.getUrl()), "application/pdf");
                    startActivity(intent);
                }

                if(putPDF.typeData.equals("JPG")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(putPDF.getUrl()), "image/jpeg");
                    startActivity(intent);
                }

                if(putPDF.typeData.equals("DOC")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(putPDF.getUrl()), "application/msword");
                    startActivity(intent);
                }

            }
        });
    }

    private void retrievedPDFFile() {

        databaseReference = FirebaseDatabase.getInstance().getReference(val);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Wyszukiwanie w bazie danych

                for(DataSnapshot ds: snapshot.getChildren()){
                        putPDF putPDF = ds.getValue(com.example.econtact.putPDF.class);
                        uploadedPDF.add(putPDF);
                }

                //Ustawienie rozmiaru tablicy
                String[] uploadsName = new String[uploadedPDF.size()];

                //Ustawienie plików do tablicy
                for(int i = 0; i<uploadsName.length; i++){
                    uploadsName[i] = uploadedPDF.get(i).getName();
                }

                //Ustawienie View pdf w aplikacji do wyswietlenia
                ArrayAdapter<String>arrayAdapter = new
                        ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploadsName){
                            @NonNull
                            @Override
                            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                TextView textView = view
                                        .findViewById(android.R.id.text1);

                                textView.setTextColor(Color.WHITE);
                                textView.setTextSize(20);
                                return view;
                            }
                        };

                listView.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
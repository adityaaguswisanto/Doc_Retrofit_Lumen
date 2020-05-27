package com.adityaagusw.examplelumen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.adityaagusw.examplelumen.Activity.InputMhsActivity;
import com.adityaagusw.examplelumen.Activity.InputNoteActivity;
import com.adityaagusw.examplelumen.Activity.LihatMhsActivity;
import com.adityaagusw.examplelumen.Activity.LihatNoteActivity;
import com.adityaagusw.examplelumen.Activity.PaginationActivity;
import com.adityaagusw.examplelumen.Activity.SearchNoteActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnInputNote, btnInputMahasiswa, btnLihatNote, btnLihatMhs, btnSearchNote, btnPagiNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInputNote = findViewById(R.id.btnInputNote);
        btnInputMahasiswa = findViewById(R.id.btnInputMahasiswa);
        btnLihatNote = findViewById(R.id.btnLihatNote);
        btnLihatMhs = findViewById(R.id.btnLihatMahasiswa);
        btnSearchNote = findViewById(R.id.btnSearchNote);
        btnPagiNote = findViewById(R.id.btnPagiNote);

        btnInputNote.setOnClickListener(this);
        btnInputMahasiswa.setOnClickListener(this);
        btnLihatNote.setOnClickListener(this);
        btnLihatMhs.setOnClickListener(this);
        btnSearchNote.setOnClickListener(this);
        btnPagiNote.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnInputNote:
                startActivity(new Intent(getApplicationContext(), InputNoteActivity.class));
                break;
            case R.id.btnInputMahasiswa:
                Intent intent = new Intent(getApplicationContext(), InputMhsActivity.class);
                startActivity(intent);
                break;
            case R.id.btnLihatNote:
                startActivity(new Intent(getApplicationContext(), LihatNoteActivity.class));
                break;
            case R.id.btnLihatMahasiswa:
                startActivity(new Intent(getApplicationContext(), LihatMhsActivity.class));
                break;
            case R.id.btnSearchNote:
                startActivity(new Intent(getApplicationContext(), SearchNoteActivity.class));
                break;
            case R.id.btnPagiNote:
                startActivity(new Intent(getApplicationContext(), PaginationActivity.class));
                break;

        }
    }
}

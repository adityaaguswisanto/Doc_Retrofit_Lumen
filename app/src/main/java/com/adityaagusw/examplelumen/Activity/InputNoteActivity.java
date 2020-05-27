package com.adityaagusw.examplelumen.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adityaagusw.examplelumen.Api.ApiClient;
import com.adityaagusw.examplelumen.Api.ApiInterface;
import com.adityaagusw.examplelumen.Model.Note;
import com.adityaagusw.examplelumen.Model.NoteResponse;
import com.adityaagusw.examplelumen.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtTitle, edtContent;
    private Button btnSubmit, btnUpdate, btnDelete;
    private ProgressBar progressBar;

    private int id;
    private String title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_note);

        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        progressBar = findViewById(R.id.progressBar);
        btnSubmit = findViewById(R.id.btnSubmitNote);
        btnDelete = findViewById(R.id.btnDeleteNote);
        btnUpdate = findViewById(R.id.btnUpdateNote);
        btnUpdate.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);

        btnSubmit.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        //here catch data for update
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");

        catchData();

    }

    private void catchData() {
        if (id != 0) {
            edtTitle.setText(title);
            edtContent.setText(content);
            btnUpdate.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

        title = edtTitle.getText().toString().trim();
        content = edtContent.getText().toString().trim();

        switch (v.getId()) {
            case R.id.btnSubmitNote:
                sendNote(title, content);
                break;
            case R.id.btnUpdateNote:
                updateNote(id, title, content);
                break;
            case R.id.btnDeleteNote:
                deleteNote(id);
                break;
        }
    }

    private void deleteNote(int id) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<NoteResponse<Note>> call = apiInterface.deleteNote(id);

        call.enqueue(new Callback<NoteResponse<Note>>() {
            @Override
            public void onResponse(@NonNull Call<NoteResponse<Note>> call,@NonNull Response<NoteResponse<Note>> response) {

                if (response.isSuccessful()){

                    assert response.body() != null;
                    boolean value = response.body().isValue();

                    if (!value){
                        Toast.makeText(InputNoteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(InputNoteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(InputNoteActivity.this, "Terjadi Kesalahan Saat Menghapus", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<NoteResponse<Note>> call,@NonNull Throwable t) {
                Toast.makeText(InputNoteActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateNote(int id, String title, String content) {

        if (title.length() < 3) {
            edtTitle.setError("Tidak boleh kurang dari 3");
        }
        if (content.length() < 3) {
            edtContent.setError("Tidak boleh kurang dari 3");
        }
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {
            setLoadingUpdate(true);
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<NoteResponse<Note>> call = apiInterface.updateNote(id, title, content);

            call.enqueue(new Callback<NoteResponse<Note>>() {
                @Override
                public void onResponse(@NonNull Call<NoteResponse<Note>> call,@NonNull Response<NoteResponse<Note>> response) {

                    if (response.isSuccessful()){

                        assert response.body() != null;
                        boolean value = response.body().isValue();

                        if (!value){
                            Toast.makeText(InputNoteActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(InputNoteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(InputNoteActivity.this, "Terjadi kesalahan update", Toast.LENGTH_SHORT).show();
                    }
                    setLoadingUpdate(false);
                }

                @Override
                public void onFailure(@NonNull Call<NoteResponse<Note>> call,@NonNull Throwable t) {
                    Toast.makeText(InputNoteActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    setLoadingUpdate(false);
                }
            });

        }

    }

    private void sendNote(String title, String content) {

        if (title.length() < 3) {
            edtTitle.setError("Tidak boleh kurang dari 3");
        }
        if (content.length() < 3) {
            edtContent.setError("Tidak boleh kurang dari 3");
        }
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {
            setLoading(true);
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<NoteResponse<Note>> call = apiInterface.addNote(title, content);

            call.enqueue(new Callback<NoteResponse<Note>>() {
                @Override
                public void onResponse(@NonNull Call<NoteResponse<Note>> call, @NonNull Response<NoteResponse<Note>> response) {

                    if (response.isSuccessful()) {

                        assert response.body() != null;
                        boolean value = response.body().isValue();
                        String message = response.body().getMessage();

                        if (!value) {
                            Toast.makeText(InputNoteActivity.this, message, Toast.LENGTH_SHORT).show();
                            edtTitle.setText("");
                            edtContent.setText("");
                        } else {
                            Toast.makeText(InputNoteActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(InputNoteActivity.this, "Kesalahan Membuat Note", Toast.LENGTH_SHORT).show();
                    }

                    setLoading(false);

                }

                @Override
                public void onFailure(@NonNull Call<NoteResponse<Note>> call, @NonNull Throwable t) {
                    Toast.makeText(InputNoteActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    setLoading(false);
                }
            });
        }

    }

    private void setLoadingUpdate(Boolean state){
        if (state){
            progressBar.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
        }
    }

    private void setLoading(boolean state) {

        if (state) {
            progressBar.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.VISIBLE);
        }

    }

}

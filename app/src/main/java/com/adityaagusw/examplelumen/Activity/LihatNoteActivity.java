package com.adityaagusw.examplelumen.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.adityaagusw.examplelumen.Adapter.AdapterNote;
import com.adityaagusw.examplelumen.Api.ApiClient;
import com.adityaagusw.examplelumen.Api.ApiInterface;
import com.adityaagusw.examplelumen.Model.Note;
import com.adityaagusw.examplelumen.Model.NoteGet;
import com.adityaagusw.examplelumen.Model.NoteResponse;
import com.adityaagusw.examplelumen.R;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LihatNoteActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private AdapterNote adapterNote;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_note);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshNote);
        recyclerView = findViewById(R.id.recyclerViewNote);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        getDataNote();

        swipeRefreshLayout.setOnRefreshListener(this::getDataNote);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataNote();
    }

    private void getDataNote() {

        loadingSwipe(true);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<NoteGet<Note>> call = apiInterface.getNote();

        call.enqueue(new Callback<NoteGet<Note>>() {
            @Override
            public void onResponse(@NonNull Call<NoteGet<Note>> call, @NonNull Response<NoteGet<Note>> response) {

                if (response.isSuccessful()) {

                    assert response.body() != null;
                    boolean value = response.body().isValue();

                    if (!value) {

                        ArrayList<Note> list = response.body().getData();

                        if (list.isEmpty()) {
                            Toast.makeText(LihatNoteActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                        } else {
                            adapterNote = new AdapterNote(list);
                            adapterNote.setNoteArrayList(list);
                            recyclerView.setAdapter(adapterNote);
                        }

                    } else {
                        Toast.makeText(LihatNoteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        loadingSwipe(false);
                    }

                    loadingSwipe(false);

                } else {
                    Toast.makeText(LihatNoteActivity.this, "Kesalahan Melihat Note", Toast.LENGTH_SHORT).show();
                    loadingSwipe(false);
                }

            }

            @Override
            public void onFailure(@NonNull Call<NoteGet<Note>> call, @NonNull Throwable t) {
                Toast.makeText(LihatNoteActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingSwipe(false);
            }
        });

    }

    private void loadingSwipe(boolean state) {

        if (state) {
            swipeRefreshLayout.setRefreshing(true);
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }

    }

}

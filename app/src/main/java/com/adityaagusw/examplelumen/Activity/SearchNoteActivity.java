package com.adityaagusw.examplelumen.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchNoteActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    private AdapterNote adapterNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_note);

        recyclerView = findViewById(R.id.recyclerViewSearchNote);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        recyclerView.setVisibility(View.GONE);

        if (newText != null) {

            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<NoteGet<Note>> call = apiInterface.searchNote(newText);

            call.enqueue(new Callback<NoteGet<Note>>() {
                @Override
                public void onResponse(@NonNull Call<NoteGet<Note>> call, @NonNull Response<NoteGet<Note>> response) {

                    if (response.isSuccessful()) {

                        assert response.body() != null;
                        boolean value = response.body().isValue();

                        if (!value) {

                            ArrayList<Note> list = response.body().getData();

                            if (list.isEmpty()) {
                                Toast.makeText(SearchNoteActivity.this, "Data tidak ada", Toast.LENGTH_SHORT).show();
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                adapterNote = new AdapterNote(list);
                                adapterNote.setNoteArrayList(list);
                                recyclerView.setAdapter(adapterNote);
                                recyclerView.setVisibility(View.VISIBLE);
                            }

                        } else {
                            Toast.makeText(SearchNoteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            recyclerView.setVisibility(View.GONE);
                        }

                    } else {
                        Toast.makeText(SearchNoteActivity.this, "Kesalahan Melihat Note", Toast.LENGTH_SHORT).show();
                        recyclerView.setVisibility(View.GONE);
                    }


                }

                @Override
                public void onFailure(@NonNull Call<NoteGet<Note>> call, @NonNull Throwable t) {
                    Toast.makeText(SearchNoteActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.GONE);
                }
            });

        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("Cari Title....");
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);
        return true;
    }
}

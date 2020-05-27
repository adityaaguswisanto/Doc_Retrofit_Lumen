package com.adityaagusw.examplelumen.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adityaagusw.examplelumen.Adapter.AdapterNote;
import com.adityaagusw.examplelumen.Api.ApiClient;
import com.adityaagusw.examplelumen.Api.ApiInterface;
import com.adityaagusw.examplelumen.Model.Note;
import com.adityaagusw.examplelumen.Model.NotePagination;
import com.adityaagusw.examplelumen.Pagination.Pagination;
import com.adityaagusw.examplelumen.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaginationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterNote adapterNote;
    private ProgressBar progressBar;

    //for pagination
    private static final int START_PAGE = 1;
    private int TOTAL_PAGES = 10;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int CURRENT_PAGE = START_PAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagination);

        recyclerView = findViewById(R.id.recyclerViewMain);
        progressBar = findViewById(R.id.progressBar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        getData();

        recyclerView.addOnScrollListener(new Pagination(linearLayoutManager) {
            @Override
            protected void loadMoreItem() {
                isLoading = true;
                CURRENT_PAGE += 1;
                loadNextMore();
            }

            @Override
            protected int getTotalPages() {
                return TOTAL_PAGES;
            }

            @Override
            protected boolean isLastPage() {
                return isLastPage;
            }

            @Override
            protected boolean isLoading() {
                return isLoading;
            }
        });

    }

    private void loadNextMore() {
        progressBar.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<NotePagination<Note>> call = apiInterface.paginationNote(CURRENT_PAGE);

        call.enqueue(new Callback<NotePagination<Note>>() {
            @Override
            public void onResponse(@NonNull Call<NotePagination<Note>> call, @NonNull Response<NotePagination<Note>> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    ArrayList<Note> list = response.body().getData();

                    if (list.isEmpty()) {
                        Toast.makeText(PaginationActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        adapterNote.addAll(list);
                        isLoading = false;
                        progressBar.setVisibility(View.GONE);
                    }
                    isLoading = false;
                } else {
                    Toast.makeText(PaginationActivity.this, "Kesalahan melihat data", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(@NonNull Call<NotePagination<Note>> call, @NonNull Throwable t) {
                Toast.makeText(PaginationActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getData() {
        progressBar.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<NotePagination<Note>> call = apiInterface.paginationNote(CURRENT_PAGE);

        call.enqueue(new Callback<NotePagination<Note>>() {
            @Override
            public void onResponse(@NonNull Call<NotePagination<Note>> call, @NonNull Response<NotePagination<Note>> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    ArrayList<Note> list = response.body().getData();

                    if (list.isEmpty()) {
                        Toast.makeText(PaginationActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        adapterNote = new AdapterNote(list);
                        recyclerView.setAdapter(adapterNote);
                        Toast.makeText(PaginationActivity.this, "Loading page ", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(PaginationActivity.this, "Kesalahan Melihat Data", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(@NonNull Call<NotePagination<Note>> call, @NonNull Throwable t) {
                Toast.makeText(PaginationActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}

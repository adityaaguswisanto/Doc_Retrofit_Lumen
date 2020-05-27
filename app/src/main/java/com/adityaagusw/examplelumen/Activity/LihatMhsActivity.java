package com.adityaagusw.examplelumen.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.adityaagusw.examplelumen.Adapter.AdapterMhs;
import com.adityaagusw.examplelumen.Adapter.AdapterNote;
import com.adityaagusw.examplelumen.Api.ApiClient;
import com.adityaagusw.examplelumen.Api.ApiInterface;
import com.adityaagusw.examplelumen.Model.Mahasiswa;
import com.adityaagusw.examplelumen.Model.MahasiswaGet;
import com.adityaagusw.examplelumen.Model.MahasiswaResponse;
import com.adityaagusw.examplelumen.Model.Note;
import com.adityaagusw.examplelumen.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LihatMhsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AdapterMhs adapterMhs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_mhs);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshMhs);
        recyclerView = findViewById(R.id.recyclerViewMhs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getMahasiswa();

        swipeRefreshLayout.setOnRefreshListener(this::getMahasiswa);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getMahasiswa();
    }

    private void getMahasiswa() {

        loadMahasiswa(true);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<MahasiswaGet<Mahasiswa>> call = apiInterface.getMahasiswa();

        call.enqueue(new Callback<MahasiswaGet<Mahasiswa>>() {
            @Override
            public void onResponse(@NonNull Call<MahasiswaGet<Mahasiswa>> call, @NonNull Response<MahasiswaGet<Mahasiswa>> response) {

                if (response.isSuccessful()) {

                    assert response.body() != null;
                    boolean value = response.body().isValue();

                    if (!value) {

                        ArrayList<Mahasiswa> list = response.body().getData();

                        if (list.isEmpty()) {
                            Toast.makeText(LihatMhsActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                        } else {
                            adapterMhs = new AdapterMhs(list);
                            adapterMhs.setMahasiswaArrayList(list);
                            recyclerView.setAdapter(adapterMhs);
                        }

                    } else {
                        Toast.makeText(LihatMhsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        loadMahasiswa(false);
                    }

                    loadMahasiswa(false);

                } else {
                    Toast.makeText(LihatMhsActivity.this, "Kesalahan melihat mahasiswa", Toast.LENGTH_SHORT).show();
                    loadMahasiswa(false);
                }

            }

            @Override
            public void onFailure(@NonNull Call<MahasiswaGet<Mahasiswa>> call, @NonNull Throwable t) {
                Toast.makeText(LihatMhsActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                loadMahasiswa(false);
            }
        });

    }

    private void loadMahasiswa(boolean state) {

        if (state){
            swipeRefreshLayout.setRefreshing(true);
        }else{
            swipeRefreshLayout.setRefreshing(false);
        }

    }
}

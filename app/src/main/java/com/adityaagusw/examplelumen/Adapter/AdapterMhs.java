package com.adityaagusw.examplelumen.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adityaagusw.examplelumen.Activity.InputMhsActivity;
import com.adityaagusw.examplelumen.Model.Mahasiswa;
import com.adityaagusw.examplelumen.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterMhs extends RecyclerView.Adapter<AdapterMhs.MyViewHolder> {

    private ArrayList<Mahasiswa> mahasiswaArrayList;

    public AdapterMhs(ArrayList<Mahasiswa> mahasiswaArrayList) {
        this.mahasiswaArrayList = mahasiswaArrayList;
    }

    public void setMahasiswaArrayList(ArrayList<Mahasiswa> mahasiswaArrayList) {
        this.mahasiswaArrayList = mahasiswaArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mhs, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Mahasiswa mahasiswa = mahasiswaArrayList.get(position);
        String url = "https://examplerestapi.000webhostapp.com/foto/" + mahasiswa.getFoto();

        holder.txtNim.setText(mahasiswa.getNim());
        holder.txtNama.setText(mahasiswa.getNama());
        Glide.with(holder.itemView)
                .load(url)
                .into(holder.ivFoto);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), InputMhsActivity.class);
            intent.putExtra("id", mahasiswa.getId());
            intent.putExtra("nim", mahasiswa.getNim());
            intent.putExtra("nama", mahasiswa.getNama());
            intent.putExtra("foto", url);

            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return mahasiswaArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivFoto;
        private TextView txtNim, txtNama;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivFoto = itemView.findViewById(R.id.ivFotoItem);
            txtNim = itemView.findViewById(R.id.txtNimItem);
            txtNama = itemView.findViewById(R.id.txtNamaItem);

        }
    }
}

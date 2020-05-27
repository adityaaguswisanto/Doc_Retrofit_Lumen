package com.adityaagusw.examplelumen.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.adityaagusw.examplelumen.Api.ApiClient;
import com.adityaagusw.examplelumen.Api.ApiInterface;
import com.adityaagusw.examplelumen.Model.Mahasiswa;
import com.adityaagusw.examplelumen.Model.MahasiswaResponse;
import com.adityaagusw.examplelumen.R;
import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputMhsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivFoto;
    private EditText edtNim, edtNama;
    private Button btnSubmit, btnUpdate, btnDelete;

    private File imgFile;

    private int id;
    private String nim, nama, foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_mhs);

        ivFoto = findViewById(R.id.ivFoto);
        edtNim = findViewById(R.id.edtNim);
        edtNama = findViewById(R.id.edtNama);
        btnSubmit = findViewById(R.id.btnSubmitMhs);
        btnUpdate = findViewById(R.id.btnUpdateMhs);
        btnDelete = findViewById(R.id.btnDeleteMhs);

        btnUpdate.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);

        Permission();

        //here catch data for update
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        nim = intent.getStringExtra("nim");
        nama = intent.getStringExtra("nama");
        foto = intent.getStringExtra("foto");

        catchData();

        btnSubmit.setOnClickListener(this);
        ivFoto.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

    }

    private void catchData() {

        if (id != 0) {
            edtNim.setText(nim);
            edtNama.setText(nama);

            Glide.with(this)
                    .load(foto)
                    .into(ivFoto);

            btnUpdate.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);

        }

    }

    @Override
    public void onClick(View v) {

        String nim1 = edtNim.getText().toString().trim();
        String nama1 = edtNim.getText().toString().trim();

        switch (v.getId()) {
            case R.id.btnSubmitMhs:

                if (nim1.isEmpty() || nama1.isEmpty() || imgFile == null) {
                    Toast.makeText(this, "jangan dikosongkan", Toast.LENGTH_SHORT).show();
                } else {

                    ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    //for file activityResult
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/from-data"), imgFile);

                    MultipartBody.Part foto = MultipartBody.Part.createFormData("foto", imgFile.getName(), requestBody);

                    RequestBody nim = RequestBody.create(MediaType.parse("text/plain"), edtNim.getText().toString().trim());
                    RequestBody nama = RequestBody.create(MediaType.parse("text/plain"), edtNama.getText().toString().trim());

                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<MahasiswaResponse<Mahasiswa>> call = apiInterface.addMahasiswa(nim, nama, foto);

                    call.enqueue(new Callback<MahasiswaResponse<Mahasiswa>>() {
                        @Override
                        public void onResponse(@NonNull Call<MahasiswaResponse<Mahasiswa>> call, @NonNull Response<MahasiswaResponse<Mahasiswa>> response) {

                            if (response.isSuccessful()) {

                                assert response.body() != null;
                                boolean value = response.body().isValue();

                                if (!value) {
                                    Toast.makeText(InputMhsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(InputMhsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                progressDialog.dismiss();

                            } else {
                                Toast.makeText(InputMhsActivity.this, "Terjadi kesalahan saat mengupload", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }

                        @Override
                        public void onFailure(@NonNull Call<MahasiswaResponse<Mahasiswa>> call, @NonNull Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(InputMhsActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                break;
            case R.id.btnDeleteMhs:
                //here code

                ApiInterface api = ApiClient.getApiClient().create(ApiInterface.class);
                Call<MahasiswaResponse<Mahasiswa>> callApi = api.deleteMahasiswa(id);

                callApi.enqueue(new Callback<MahasiswaResponse<Mahasiswa>>() {
                    @Override
                    public void onResponse(@NonNull Call<MahasiswaResponse<Mahasiswa>> call, @NonNull Response<MahasiswaResponse<Mahasiswa>> response) {

                        if (response.isSuccessful()) {

                            assert response.body() != null;
                            boolean value = response.body().isValue();

                            if (!value) {
                                Toast.makeText(InputMhsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(InputMhsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(InputMhsActivity.this, "Kesalahan dalam menghapus", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<MahasiswaResponse<Mahasiswa>> call, @NonNull Throwable t) {
                        Toast.makeText(InputMhsActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case R.id.ivFoto:
                EasyImage.openChooserWithGallery(InputMhsActivity.this, "Pilih", 3);
                break;
            case R.id.btnUpdateMhs:
                //here code

                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Loading Update...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                MultipartBody.Part fotoPart = null;

                if (imgFile != null) {
                    File file = new File(String.valueOf(imgFile));
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/from-data"), file);
                    fotoPart = MultipartBody.Part.createFormData("foto", file.getName(), requestBody);
                }

                RequestBody nim = RequestBody.create(MediaType.parse("text/plain"), edtNim.getText().toString().trim());
                RequestBody nama = RequestBody.create(MediaType.parse("text/plain"), edtNama.getText().toString().trim());

                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<MahasiswaResponse<Mahasiswa>> call = apiInterface.updateMahasiswa(id, nim, nama, fotoPart);

                call.enqueue(new Callback<MahasiswaResponse<Mahasiswa>>() {
                    @Override
                    public void onResponse(@NonNull Call<MahasiswaResponse<Mahasiswa>> call, @NonNull Response<MahasiswaResponse<Mahasiswa>> response) {

                        if (response.isSuccessful()) {

                            assert response.body() != null;
                            boolean value = response.body().isValue();

                            if (!value) {
                                Toast.makeText(InputMhsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(InputMhsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            progressDialog.dismiss();

                        } else {
                            Toast.makeText(InputMhsActivity.this, "Terjadi kesalahan saat update", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<MahasiswaResponse<Mahasiswa>> call, @NonNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(InputMhsActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                break;
        }

    }

    //here for image--------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //here library easyimage
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                CropImage.activity(Uri.fromFile(imageFile))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setFixAspectRatio(true)
                        .start(InputMhsActivity.this);
            }

            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                super.onImagePickerError(e, source, type);
                Toast.makeText(InputMhsActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                super.onCanceled(source, type);
                Toast.makeText(InputMhsActivity.this, "Tidak jadi", Toast.LENGTH_SHORT).show();
            }
        });

        //here result
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                assert result != null;
                Uri uri = result.getUri();

                Glide.with(getApplicationContext())
                        .load(new File(Objects.requireNonNull(uri.getPath())))
                        .into(ivFoto);

                imgFile = new File(uri.getPath());

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                assert result != null;
                Exception exception = result.getError();
                Toast.makeText(this, "Error : " + exception.toString(), Toast.LENGTH_SHORT).show();
            }

        }


    }

    //here for Permission---------------------------------------------------------------------------
    private void Permission() {
        if (ContextCompat.checkSelfPermission(InputMhsActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(InputMhsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Kasih Hak Aksess dulu cok", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(InputMhsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

}

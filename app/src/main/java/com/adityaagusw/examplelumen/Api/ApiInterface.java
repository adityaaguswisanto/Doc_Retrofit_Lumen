package com.adityaagusw.examplelumen.Api;

import com.adityaagusw.examplelumen.Model.Mahasiswa;
import com.adityaagusw.examplelumen.Model.MahasiswaGet;
import com.adityaagusw.examplelumen.Model.MahasiswaResponse;
import com.adityaagusw.examplelumen.Model.Note;
import com.adityaagusw.examplelumen.Model.NoteGet;
import com.adityaagusw.examplelumen.Model.NotePagination;
import com.adityaagusw.examplelumen.Model.NoteResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("note/create")
    Call<NoteResponse<Note>> addNote(@Field("title") String title,
                                     @Field("content") String content);

    @FormUrlEncoded
    @POST("note/update")
    Call<NoteResponse<Note>> updateNote(@Field("id") int id,
                                        @Field("title") String title,
                                        @Field("content") String content);

    @FormUrlEncoded
    @POST("note/delete")
    Call<NoteResponse<Note>> deleteNote(@Field("id") int id);

    @GET("note")
    Call<NoteGet<Note>> getNote();

    //pagination
    @GET("note/paginate")
    Call<NotePagination<Note>> paginationNote(@Query("page") int page);

    @GET("note/search")
    Call<NoteGet<Note>> searchNote(@Query("data") String data);

    @Multipart
    @POST("mahasiswa/create")
    Call<MahasiswaResponse<Mahasiswa>> addMahasiswa(@Part("nim") RequestBody nim,
                                                    @Part("nama") RequestBody nama,
                                                    @Part MultipartBody.Part foto);

    @Multipart
    @POST("mahasiswa/update")
    Call<MahasiswaResponse<Mahasiswa>> updateMahasiswa(@Part("id") int id,
                                                       @Part("nim") RequestBody nim,
                                                       @Part("nama") RequestBody nama,
                                                       @Part MultipartBody.Part foto);

    @FormUrlEncoded
    @POST("mahasiswa/delete")
    Call<MahasiswaResponse<Mahasiswa>> deleteMahasiswa(@Field("id") int id);

    @GET("mahasiswa")
    Call<MahasiswaGet<Mahasiswa>> getMahasiswa();

}

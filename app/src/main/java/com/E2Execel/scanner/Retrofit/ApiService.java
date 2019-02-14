package com.E2Execel.scanner.Retrofit;

import com.E2Execel.scanner.Pojo.add_pv_module.AddPvModule;
import com.E2Execel.scanner.Pojo.login_details.Login;
import com.E2Execel.scanner.Pojo.result_details.Result;
import com.E2Execel.scanner.Pojo.search_details.Search;
import com.E2Execel.scanner.Pojo.update_details.UpdateDetails;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // public static String token = null;

    //for sign In
    @Headers("APIKEY:931d2c3421e9499098676ac0122aaca1")
    @POST("panel/api/v1/user/login/")
    @FormUrlEncoded
    Call<Login> getLoginJason(@Field("username_or_email") String username_or_email, @Field("password") String password, @Field("Source") String Source);


    //for SEARCH

    @POST("panel/api/v1/search/")
    @FormUrlEncoded
    Call<Search> getSearchJason(@Header("APIKEY") String APIKEY, @Header("Authorization") String Authorization,
                                @Field("searchkey") String searchkey, @Field("Source") String Source);


    @GET("panel/api/v1/info/{path}")
    Call<Result> getResultsJson(@Header("APIKEY") String APIKEY, @Header("Authorization") String Authorization, @Path("path") String path);



    //add pv module with Id
    @Multipart
    @POST("panel/api/v1/pvmodule")
    Call<AddPvModule> addPvModuleWithId(@Header("APIKEY") String APIKEY, @Header("Authorization") String Authorization,
                                  @Part MultipartBody.Part file1, @Part("srno") RequestBody srno,
                                  @Part("panel") RequestBody panel, @Part("id") RequestBody id,
                                  @Part("Source") RequestBody Source);

    //add pv module without Id
    @Multipart
    @POST("panel/api/v1/pvmodule")
    Call<AddPvModule> addPvModuleWithOutId(@Header("APIKEY") String APIKEY, @Header("Authorization") String Authorization,
                                        @Part MultipartBody.Part file1, @Part("srno") RequestBody srno,
                                        @Part("panel") RequestBody panel,
                                        @Part("Source") RequestBody Source);


    //for Pump info
    @Multipart
    @POST("panel/api/v1/update/{path}")
    Call<UpdateDetails> uploadPumpInfo(@Header("APIKEY") String APIKEY, @Header("Authorization") String Authorization,
                                       @Part MultipartBody.Part file1, @Part("pumpsrno") RequestBody pumpsrno,
                                       @Part("Source") RequestBody Source, @Path("path") String path);


    //for Controler Info
    @Multipart
    @POST("panel/api/v1/update/{path}")
    Call<UpdateDetails> uploadControllerInfo(@Header("APIKEY") String APIKEY, @Header("Authorization") String Authorization,
                                             @Part MultipartBody.Part file1, @Part("controllersrno") RequestBody controllersrno,
                                             @Part("Source") RequestBody Source, @Path("path") String path);

    //for Hp motors info
    @Multipart
    @POST("panel/api/v1/update/{path}")
    Call<UpdateDetails> uploadHpmotorInfo(@Header("APIKEY") String APIKEY, @Header("Authorization") String Authorization,
                                          @Part MultipartBody.Part file1, @Part("hpmotorsrno") RequestBody hpmotorsrno,
                                          @Part("Source") RequestBody Source, @Path("path") String path);


    //for Installation info
    @Multipart
    @POST("panel/api/v1/update/{path}")
    Call<UpdateDetails> uploadInstallationInfo(@Header("APIKEY") String APIKEY, @Header("Authorization") String Authorization,
                                               @Part MultipartBody.Part file1, @Part("installationstatus") RequestBody installationstatus,
                                               @Part("Source") RequestBody Source, @Path("path") String path);

    //for update user photo
    @Multipart
    @POST("panel/api/v1/update/{path}")
    Call<UpdateDetails> uploadUserPhoto(@Header("APIKEY") String APIKEY, @Header("Authorization") String Authorization,
                                               @Part MultipartBody.Part file1,
                                               @Part("Source") RequestBody Source, @Path("path") String path);

}

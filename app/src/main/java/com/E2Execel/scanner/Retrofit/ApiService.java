package com.E2Execel.scanner.Retrofit;

import com.E2Execel.scanner.Pojo.login_details.Login;
import com.E2Execel.scanner.Pojo.result_details.Results;
import com.E2Execel.scanner.Pojo.search_details.Search;
import com.E2Execel.scanner.Pojo.update_details.UpdateDetails;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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

public interface ApiService {

    // public static String token = null;

    //for sign In
    @Headers("APIKEY:931d2c3421e9499098676ac0122aaca1")
    @POST("panel/api/v1/user/login/")
    @FormUrlEncoded
    Call<Login> getLoginJason(@Field("username_or_email") String username_or_email, @Field("password") String password, @Field("source") String source);


    //for SEARCH

    @POST("panel/api/v1/device/search/")
    @FormUrlEncoded
    Call<Search> getSearchJason(@Header("APIKEY") String APIKEY, @Header("Authorization") String Authorization,
                                @Field("searchkey") String searchkey, @Field("source") String source);


    @GET("panel/api/v1/device/info/{path}")
    Call<Results> getResultsJson(@Header("APIKEY") String APIKEY, @Header("Authorization") String Authorization, @Path("path") String path);

    @Multipart
    @POST("panel/api/v1/device/update/{path}")
    Call<UpdateDetails> upload(@Header("APIKEY") String APIKEY, @Header("Authorization") String Authorization,
                               @Part MultipartBody.Part file1, @Part("pvmodulesrno") RequestBody pvmodulesrno,
                               @Path("path") String path);

}

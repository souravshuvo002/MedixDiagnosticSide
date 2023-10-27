package com.example.medixdiagnostic.Api;

import com.example.medixdiagnostic.Model.Result;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {


    // register call
    @FormUrlEncoded
    @POST("registerDiagnostic")
    Call<Result> registerDiagnostic(
            @Field("name") String name,
            @Field("address") String address,
            @Field("phone") String phone,
            @Field("email") String email,
            @Field("password") String password,
            @Field("tagline") String tagline,
            @Field("discount") String discount);


    // login call
    @FormUrlEncoded
    @POST("loginDiagnostic")
    Call<Result> loginDiagnostic(
            @Field("phone") String phone,
            @Field("password") String password);



    //getting all test categories
    @GET("getAllCategories")
    Call<Result> getAllCategories();

    //get all test based on diagnostic_center_id
    @GET("getTestByCenterIDAdmin/{diagnostic_center_id}")
    Call<Result> getTestByCenterID(@Path("diagnostic_center_id") String diagnostic_center_id);

    // add test item
    @FormUrlEncoded
    @POST("addTestItem")
    Call<Result> addTestItem(
            @Field("center_id") String center_id,
            @Field("category_id") String category_id,
            @Field("name") String name,
            @Field("short_name") String short_name,
            @Field("long_desc") String long_desc,
            @Field("short_desc") String short_desc);

    // Get Last inserted  test id by of center_id
    @FormUrlEncoded
    @POST("getLastInsertedTestID")
    Call<Result> getLastInsertedTestID(
            @Field("center_id") String center_id);

    // add test item price
    @FormUrlEncoded
    @POST("addTestPrice")
    Call<Result> addTestPrice(
            @Field("center_id") String center_id,
            @Field("test_id") String test_id,
            @Field("price") String price);

    // Update test item
    @FormUrlEncoded
    @POST("updateTestItem/{test_id}")
    Call<Result> updateTestItem(
            @Path("test_id") String test_id,
            @Field("category_id") String category_id,
            @Field("name") String name,
            @Field("short_name") String short_name,
            @Field("long_desc") String long_desc,
            @Field("short_desc") String short_desc,
            @Field("status") String status);

    // Update test item Price
    @FormUrlEncoded
    @POST("updateTestItemPrice/{test_id}")
    Call<Result> updateTestItemPrice(
            @Path("test_id") String test_id,
            @Field("price") String price,
            @Field("discount_price") String discount_price,
            @Field("dis_start_date") String dis_start_date,
            @Field("dis_end_date") String dis_end_date);

    // get all book history
    @FormUrlEncoded
    @POST("getAllBookingForDiagnostic")
    Call<Result> getAllBookingForDiagnostic(
            @Field("center_id") String center_id);

    // get Book Details for Diagnostic Center
    @FormUrlEncoded
    @POST("getBookDetailsDiagnosticCenter")
    Call<Result> getBookDetailsDiagnosticCenter(
            @Field("test_book_id") String test_book_id,
            @Field("center_id") String center_id);

    // get Book Details for test for Diagnostic Center
    @FormUrlEncoded
    @POST("getTestBookDetailsDiagnosticCenter")
    Call<Result> getTestBookDetailsDiagnosticCenter(
            @Field("test_book_id") String test_book_id,
            @Field("center_id") String center_id);

    // updating a book status by Diagnostic Center
    @FormUrlEncoded
    @POST("updateBookStatus/{test_book_id}")
    Call<Result> updateBookStatus(
            @Path("test_book_id") String test_book_id,
            @Field("center_id") String center_id,
            @Field("test_id") String test_id,
            @Field("customer_id") String customer_id,
            @Field("book_status_id") String book_status_id);

    //updating token for diagnostic
    @FormUrlEncoded
    @POST("updateDiagnosticToken")
    Call<Result> updateDiagnosticToken(
            @Field("diagnostic_center_id") String diagnostic_center_id,
            @Field("token") String token);

    //get customer token
    @GET("getCustomerToken/{customer_id}")
    Call<Result> getCustomerToken(@Path("customer_id") String customer_id);
}

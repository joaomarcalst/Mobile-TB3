package com.automacorp.service

import com.example.automacorp.model.RoomDto
import retrofit2.Call
import retrofit2.http.*

interface RoomsApiService {

    @GET("rooms")
    fun findAll(): Call<List<RoomDto>>

    @GET("rooms/{id}")
    fun findById(@Path("id") id: Long): Call<RoomDto>

    @POST("rooms")
    fun createRoom(@Body room: RoomDto): Call<RoomDto>

    @PUT("rooms/{id}")
    fun updateRoom(@Path("id") id: Long, @Body room: RoomDto): Call<RoomDto>

    @DELETE("rooms/{id}")
    fun deleteRoom(@Path("id") id: Long): Call<Void>
}
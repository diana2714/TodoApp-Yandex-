package com.example.todoappsecond.network

import com.example.todoappsecond.data.model.TodoItemRequest
import com.example.todoappsecond.data.model.TodoItemResponse
import retrofit2.Response
import retrofit2.http.*

interface TodoApiService {

    @GET("list")
    suspend fun getTodoList(@Header("X-Last-Known-Revision") revision: Int): Response<TodoItemResponse>

    @POST("list")
    suspend fun addTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body todoItem: TodoItemRequest
    ): Response<TodoItemResponse>

    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String,
        @Body todoItem: TodoItemRequest
    ): Response<TodoItemResponse>

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String
    ): Response<TodoItemResponse>
}

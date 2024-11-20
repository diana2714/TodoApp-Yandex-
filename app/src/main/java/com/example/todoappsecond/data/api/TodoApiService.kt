package com.example.todoappsecond.data.api


import com.example.todoappsecond.data.dto.TodoItemRequestDto
import com.example.todoappsecond.data.dto.TodoItemResponseDto
import retrofit2.Response
import retrofit2.http.*

interface TodoApiService {

    @GET("list")
    suspend fun getTodoList(
        @Header("X-Last-Known-Revision") revision: Int
    ): Response<TodoItemResponseDto>

    @POST("list")
    suspend fun addTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body todoItem: TodoItemRequestDto
    ): Response<TodoItemResponseDto>

    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String,
        @Body todoItem: TodoItemRequestDto
    ): Response<TodoItemResponseDto>

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String
    ): Response<TodoItemResponseDto>
}

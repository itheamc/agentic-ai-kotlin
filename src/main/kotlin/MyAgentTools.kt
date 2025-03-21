package com.itheamc

import dev.langchain4j.agent.tool.P
import dev.langchain4j.agent.tool.Tool
import okhttp3.OkHttpClient
import okhttp3.Request


class MyAgentTools {

    @Tool("Return the real-time weather updates for a given city")
    fun getWeather(
        @P("The city for which the real-time weather updates should be returned") city: String?
    ): String {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("https://api.weatherapi.com/v1/current.json?key=${System.getenv("WEATHER_API_KEY")}&q=$city")
            .build()

        client.newCall(request).execute().use { response ->
            return response.body?.string() ?: ""
        }
    }

    @Tool("Return the todo for a given id")
    fun todoById(
        @P("The id of the todo which data should be returned") id: Int?
    ): String {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("https://jsonplaceholder.typicode.com/todos/${id}")
            .build()

        client.newCall(request).execute().use { response ->
            return response.body?.string() ?: ""
        }
    }

    @Tool("Return the todos")
    fun todos(): String {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("https://jsonplaceholder.typicode.com/todos")
            .build()

        client.newCall(request).execute().use { response ->
            return response.body?.string() ?: ""
        }
    }
}
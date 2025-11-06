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

    @Tool("Return the todo with a given id")
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

    @Tool("Return the user with a given id")
    fun userById(
        @P("The id of the user which data should be returned") id: Int?
    ): String {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("https://jsonplaceholder.typicode.com/users/${id}")
            .build()

        client.newCall(request).execute().use { response ->
            return response.body?.string() ?: ""
        }
    }

    @Tool("Return the users")
    fun users(): String {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("https://jsonplaceholder.typicode.com/users")
            .build()

        client.newCall(request).execute().use { response ->
            return response.body?.string() ?: ""
        }
    }

    @Tool("Return the products")
    fun products(): String {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("https://fakestoreapi.com/products/")
            .build()

        client.newCall(request).execute().use { response ->
            return response.body?.string() ?: ""
        }
    }

    @Tool("Send the email")
    fun sendEmail(
        @P("The email address") email: String,
        @P("The subject of the email") subject: String,
        @P("The body of the email") body: String,
    ): String {
        return """
            Sending email to $email
            Subject: $subject,
            Body: $body
        """.trimIndent()
    }

    @Tool("Return the maximum number from the given numbers")
    fun max(
        @P("The list of numbers") numbers: List<String> = emptyList(),
    ): String {
        return numbers.mapNotNull {it.toDoubleOrNull() ?: it.toIntOrNull()?.toDouble()}.maxOrNull()?.toString() ?: "N/A"
    }

    @Tool("Return the minimum number from the given numbers")
    fun min(
        @P("The list of numbers") numbers: List<String> = emptyList(),
    ): String {
        return numbers.mapNotNull {it.toDoubleOrNull() ?: it.toIntOrNull()?.toDouble()}.minOrNull()?.toString() ?: "N/A"
    }

    @Tool("Return the average number from the given numbers")
    fun average(
        @P("The list of numbers") numbers: List<String> = emptyList(),
    ): String {
        val lst = numbers.mapNotNull {it.toDoubleOrNull() ?: it.toIntOrNull()?.toDouble()}
        return if (lst.isNotEmpty()) (lst.reduce { acc, n -> acc + n } / numbers.size).toString() else "N/A"
    }
}
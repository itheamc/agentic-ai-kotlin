package com.itheamc

fun main() {
    val myAgent = MyAgent()

    while (true) {
        print("You: ")  // Prompt for user input
        val userInput = readlnOrNull() ?: break  // Read user input and break if null (EOF)

        if (userInput.equals("exit", ignoreCase = true)) {
            println("Chat ended.")
            break
        }

        myAgent.chat(userInput)  // Call the chat function
    }
}

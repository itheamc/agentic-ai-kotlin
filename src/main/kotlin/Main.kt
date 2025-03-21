package com.itheamc

fun main() {
    val agent = MyAgent(
        modelProvider = AgentModelProvider.GOOGLE_AI
    )

    while (true) {
        // Prompt for user input
        print("You: ")

        // Read user input and break if null
        val prompt = readlnOrNull() ?: break

        // Check if user wants to exit
        if (prompt.equals("exit", ignoreCase = true)) {
            println("Chat ended.")
            break
        }

        // Call the chat function
        agent.chat(prompt)
    }
}

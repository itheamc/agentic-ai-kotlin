package com.itheamc

import dev.langchain4j.agent.tool.ToolExecutionRequest
import dev.langchain4j.agent.tool.ToolSpecifications
import dev.langchain4j.data.message.ChatMessage
import dev.langchain4j.data.message.SystemMessage.systemMessage
import dev.langchain4j.data.message.ToolExecutionResultMessage
import dev.langchain4j.data.message.UserMessage.userMessage
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.chat.request.ChatRequest
import dev.langchain4j.model.chat.request.ChatRequestParameters
import dev.langchain4j.model.chat.response.ChatResponse
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel
import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.service.tool.DefaultToolExecutor
import java.util.*

enum class AgentModelProvider {
    OPENAI,
    GOOGLE_AI
}

/**
 * The main class for the LangChain Agent.
 * It provides a chat interface with the user and handles the conversation flow.
 */
class MyAgent(
    private val modelProvider: AgentModelProvider = AgentModelProvider.OPENAI
) {

    /**
     * Lazily initializes the OpenAI chat model with the specified API key and model name.
     */
    private val model: ChatLanguageModel by lazy {
        if (modelProvider == AgentModelProvider.OPENAI) {
            OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-4o-mini")
                .build()
        } else {
            GoogleAiGeminiChatModel.builder()
                .apiKey(System.getenv("GEMINI_API_KEY"))
                .modelName("gemini-2.0-flash")
                .build()
        }
    }


    /**
     * A [MyAgentTools] instance providing the all the available tools for my agent.
     * It contains functions like [MyAgentTools.getWeather] that can be used
     * as tools by the AI model.
     */
    private val tools = MyAgentTools()


    /**
     * Tool specifications for the AI model.
     * This specifies the tools available for use by the model in processing requests.
     * It converts the tools instance into a tool specifications object that can be
     * used by the AI model to execute specific actions or retrieve information.
     */
    private val toolSpecifications = ToolSpecifications.toolSpecificationsFrom(tools)

    /**
     * A mutable list to maintain the chat history with the AI model.
     * It initializes with a system message indicating the role of the AI.
     * This list is used to track the conversation flow and includes user messages,
     * system instructions, and tool execution results.
     */
    private val chatMessages = mutableListOf<ChatMessage>().apply {
        add(systemMessage("You are a helpful assistant."))
    }


    /**
     * Handles a chat session with the user.
     *
     * @param prompt The user's input prompt to start or continue the chat.
     * This function adds the user's message to the chat history, sends a chat request to the AI model,
     * and processes the AI's response, which may include executing specified tools.
     */
    fun chat(prompt: String) {
        try {
            // Step 1: Add User Query
            chatMessages.add(userMessage(prompt))

            // Step 2: Get AI Response
            val chatResponse = sendChatRequest(chatMessages)
            val aiMessage = chatResponse.aiMessage()
            chatMessages.add(aiMessage)

            if (aiMessage.toolExecutionRequests().isNullOrEmpty()) {
                println(aiMessage.text())
                return
            }

            // Step 3: Handle Tool Execution Requests
            processToolRequests(aiMessage.toolExecutionRequests())

            // Step 4: Get Final AI Response
            val finalResponse = sendChatRequest(chatMessages)
            println(finalResponse.aiMessage().text())
        } catch (e: Exception) {
            println("Error during chat processing: ${e.localizedMessage}")
        }
    }


    /**
     * Send a chat request to the model and return the response.
     *
     * @param messages The list of messages to send.
     * @return The response from the model.
     */
    private fun sendChatRequest(messages: List<ChatMessage>): ChatResponse {
        return model.chat(
            ChatRequest.builder()
                .messages(messages)
                .parameters(ChatRequestParameters.builder().toolSpecifications(toolSpecifications).build())
                .build()
        )
    }


    /**
     * Process the tool execution requests from the AI's response.
     * This processes each tool request by executing the tool and adding the result to the chat history.
     *
     * @param toolRequests The list of tool execution requests from the AI's response.
     */
    private fun processToolRequests(toolRequests: List<ToolExecutionRequest>?) {
        toolRequests?.takeIf { it.isNotEmpty() }?.forEach { request ->
            println(
                "Executing tool: ${request.name()}${
                    request.arguments()
                        .replace("{", "(")
                        .replace("}", ")")
                }"
            )

            val result = DefaultToolExecutor(tools, request)
                .execute(request, UUID.randomUUID().toString())

            chatMessages.add(ToolExecutionResultMessage.from(request, result))
        }
    }
}

package com.bookshop_llm_poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatController {

    private static final Logger LOG = LoggerFactory.getLogger(ChatController.class);

    private final ChatClient chatClient;

    public ChatController(
        final ChatClient.Builder chatClientBuilder,
        final ChatMemory chatMemory,
        final List<ToolCallback> tools
    ) {
        this.chatClient = chatClientBuilder
            .defaultSystem("""
                You are an AI assistant that works for a bookstore and can answer questions about authors and their books from the bookstore only.
            """)
            .defaultToolCallbacks(tools)
            .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
            .build();
    }

    /**
     * Endpoint to chat with the AI assistant.
     * The conversation ID is used to maintain the context of the chat.
     * If no query is provided, a default message is returned.
     *
     * @param userId    the user's unique identifier, used as the conversation ID. Any unique string can be used here.
     * @param query the user's query. For instance "What books do you have in your store?"
     * @return the AI's response
     */
    @GetMapping("/user/{userId}/chat")
    public String chat(
        @PathVariable(name = "userId") String userId,
        @RequestParam(name = "query", defaultValue = "Tell the user to please use the request parameter ?query=Question to use this endpoint.") String query
    ) {
        LOG.info("Received request to chat for user {}.", userId);

        return chatClient.prompt()
            .user(query)
            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId))
            .call()
            .content();
    }
}
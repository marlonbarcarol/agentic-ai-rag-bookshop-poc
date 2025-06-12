package com.bookshop_llm_poc;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class BookshopLLMPoCApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookshopLLMPoCApplication.class, args);
	}

	@Bean
	public List<ToolCallback> toolCallbacks(AuthorService authorService) {
		return List.of(ToolCallbacks.from(authorService));
	}

	@Bean
	public ChatMemoryRepository chatMemoryRepository() {
		return new InMemoryChatMemoryRepository();
	}
}

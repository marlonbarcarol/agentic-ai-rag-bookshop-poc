package com.example.bookshop_llm_poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * AuthorService provides methods to manage authors and their books.
 * It allows retrieving, adding, and removing authors from a predefined list.
 * It uses the {@code @Tool} annotation to expose methods as tools that can be called by AI chat clients. </br>
 * For more details check <a href="https://docs.spring.io/spring-ai/reference/api/tools.html#_overview">Spring AI Tools Docs</a>.
 */
@Service
public class AuthorService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorService.class);

    private final List<Author> authors = new ArrayList<>(List.of(
        new Author("George Orwell", "1984"),
        new Author("George Orwell", "Animal Farm"),
        new Author("Sigmund Freud", "The Interpretation of Dreams"),
        new Author("Sigmund Freud", "Civilization and Its Discontents"),
        new Author("Friedrich Nietzsche", "Thus Spoke Zarathustra")
    ));

    @Tool(
        name = "getAuthor",
        description = "Retrieves author information by name. Returns a list of authors with the specified name."
    )
    public String getAuthor(
        @ToolParam(description = "the name of the author") String name
    ) {
        LOG.info("Retrieving author '{}' information", name);

        return authors.stream()
            .map(Author::name)
            .filter(author -> author.equalsIgnoreCase(name))
            .findAny()
            .orElse(null);
    }

    @Tool(
        name = "getAuthorList",
        description = "Retrieves a list of all authors and their books."
    )
    public List<Author> getAuthorList() {
        LOG.info("Retrieving list of authors");

        return List.copyOf(authors);
    }

    @Tool(
        name = "addAuthor",
        description = "Adds a new author to the list."
    )
    public void addAuthor(
        @ToolParam(description = "the name of the author") String name,
        @ToolParam(description = "the book that the author has published") String bookTitle
    ) {
        LOG.info("Adding a new author '{}' and the book '{}'.", name, bookTitle);

        authors.add(new Author(name, bookTitle));
    }

    @Tool(
        name = "removeAuthor",
        description = "Removes an author from the list."
    )
    public void removeAuthor(
        @ToolParam(description = "the name of the author") String name
    ) {
        LOG.info("Removing author '{}'.", name);

        authors.removeIf(author -> author.name().equalsIgnoreCase(name));
    }
}
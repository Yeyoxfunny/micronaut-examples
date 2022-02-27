package com.smorales.graphql;

import com.smorales.domain.Author;
import com.smorales.domain.Book;
import com.smorales.repository.DbRepository;
import com.smorales.utils.Utils;
import graphql.schema.DataFetcher;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.utils.SecurityService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;

@Slf4j
@Singleton
public class GraphQLDataFetchers {

    private final DbRepository dbRepository;
    private final SecurityService securityService;

    public GraphQLDataFetchers(DbRepository dbRepository, SecurityService securityService) {
        this.dbRepository = dbRepository;
        this.securityService = securityService;
    }

    public DataFetcher<Book> getBookByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            log.info("User is authenticated: {}", securityService.isAuthenticated());

            /*if (!securityService.isAuthenticated()) {
                throw new IllegalArgumentException("User must be authenticated");
            }*/

            securityService.username().ifPresent(log::info);
            securityService.getAuthentication()
                    .ifPresent(authentication -> {
                        log.info("Authenticated user: {}", authentication);
                        log.info("Attributes: {}", Utils.stringifyAsJson(authentication.getAttributes()));
                    });

            String bookId = dataFetchingEnvironment.getArgument("id");

            return dbRepository.findAllBooks()
                    .stream()
                    .filter(book -> book.getId().equals(bookId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher<Author> getAuthorDataFetcher() {
        return dataFetchingEnvironment -> {
            Book book = dataFetchingEnvironment.getSource();
            Author authorBook = book.getAuthor();
            return dbRepository.findAllAuthors()
                    .stream()
                    .filter(author -> author.getId().equals(authorBook.getId()))
                    .findFirst()
                    .orElse(null);
        };
    }

}
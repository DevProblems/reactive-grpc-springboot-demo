package com.devproblems.grpc.reactivegrpcdemo;

import com.devProblems.grpc.reactivegrpcdemo.Author;
import com.devProblems.grpc.reactivegrpcdemo.Book;
import com.devProblems.grpc.reactivegrpcdemo.ReactorBookAuthorServiceGrpc;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;


/**
 * @author Dev Problems(A Sarang Kumar Tak)
 * @YoutubeChannel https://www.youtube.com/@devproblems
 */
@GrpcService
public class BookAuthorReactiveService extends ReactorBookAuthorServiceGrpc.BookAuthorServiceImplBase {

    @Override
    public Mono<Author> getAuthor(Mono<Author> request) {
        return request
                .map(Author::getAuthorId)
                .map(authorId -> TempDB.getAuthorsFromTempDb()
                        .stream()
                        .filter(author -> author.getAuthorId() == authorId)
                        .findFirst()
                        .orElseThrow(() -> new StatusRuntimeException(Status.NOT_FOUND.withDescription("authorId not found in DB"))));
    }

    @Override
    public Flux<Book> getBooksByAuthor(Mono<Author> request) {
        return request
                .map(Author::getAuthorId)
                .map(authorId -> TempDB.getBooksFromTempDb()
                        .stream()
                        .filter(author -> author.getAuthorId() == authorId)
                        .collect(Collectors.toList()))
                .flatMapMany(Flux::fromIterable);
    }
}

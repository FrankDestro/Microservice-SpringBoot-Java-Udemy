package com.dev.book_service.controller;

import com.dev.book_service.models.entities.Book;
import com.dev.book_service.proxy.CambioProxy;
import com.dev.book_service.repositories.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book endpoint")
@RequiredArgsConstructor
@RestController
@RequestMapping("book-service")
public class BookController {

    private final Environment environment;

    private final BookRepository bookRepository;

    private final CambioProxy proxy;

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);


    @GetMapping("/book-test")
    public String teste() {
        LOGGER.info("Received request");
        return "Hello, world!";
    }

    @Operation(summary = "Find a specific book by your ID", method = "GET")
    @GetMapping(value = "{id}/{currency}")
    public Book findBook(
            @PathVariable("id") Long id,
            @PathVariable("currency") String currency
    ) {
        LOGGER.info("call microservice cambio");
        var book = bookRepository.getReferenceById(id);
        if(book == null) throw new RuntimeException("Book not Found");

        var cambio = proxy.getCambio(book.getPrice(), "USD", currency);

        var port = environment.getProperty("local.server.port");

        book.setEnvironment("Book port: " + port + " Cambio Port " + cambio.getEnvironment());

        book.setPrice(cambio.getConvertedValue().doubleValue());

        return book;
    }

//    @GetMapping(value = "{id}/{currency}")
//    public Book findBook(
//            @PathVariable("id") Long id,
//            @PathVariable("currency") String currency
//    ) {
//        var book = bookRepository.getReferenceById(id);
//        if(book == null) throw new RuntimeException("Book not Found");
//
//        HashMap<String, String> params = new HashMap<>();
//        params.put("amount", book.getPrice().toString());
//        params.put("from", "USD");
//        params.put("to", currency);
//
//
//
//        var response = new RestTemplate()
//                .getForEntity("http://localhost:8000/cambio-service/{amount}/{from}/{to}",
//                        Cambio.class,
//                        params);
//        var cambio = response.getBody();
//        var port = environment.getProperty("local.server.port");
//        book.setEnvironment(port);
//        book.setPrice(cambio.getConvertedValue().doubleValue());
//        return book;
//    }
}

package org.example.demo.config;

import com.github.javafaker.Faker;
import org.example.demo.entity.Book;
import org.example.demo.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedDatabase(BookRepository bookRepository) {
        return args -> {

            if (bookRepository.count() > 0) {
                return;
            }

            Faker faker = new Faker();

            for (int i = 0; i < 1000; i++) {
                Book book = new Book();
                book.setIsbn(faker.code().isbn13());
                book.setName(faker.book().title());
                book.setAuthor(faker.book().author());
                book.setPrice((double) faker.number().numberBetween(20, 100));
                book.setStock(faker.number().numberBetween(1, 50));

                bookRepository.save(book);
            }
        };
    }
}
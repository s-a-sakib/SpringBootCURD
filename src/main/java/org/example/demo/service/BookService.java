package org.example.demo.service;

import org.example.demo.entity.Book;
import org.example.demo.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public Page<Book> getAllBooksPaginated(int page, int size, String keyword){
        Pageable pageable = PageRequest.of(page, size);

        if (keyword != null && !keyword.trim().isEmpty()) {
            return bookRepository
                    .findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrIsbnContainingIgnoreCase(
                            keyword, keyword, keyword, pageable
                    );
        }

        return bookRepository.findAll(pageable);
    }

    public Book saveBook(Book book){
        return bookRepository.save(book);
    }

    public Book getBookById(Long id){
        return bookRepository.findById(id).orElse(null);
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }

    public boolean isIsbnExists(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }
}
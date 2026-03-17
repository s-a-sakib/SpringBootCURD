package org.example.demo.controller;

import jakarta.validation.Valid;
import org.example.demo.entity.Book;
import org.example.demo.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String listBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "") String keyword,
            Model model
    ) {
        Page<Book> bookPage = bookService.getAllBooksPaginated(page, size, keyword);

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);

        return "books";
    }

    @GetMapping("/new")
    public String createBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    @PostMapping
    public String saveBook(
            @Valid @ModelAttribute("book") Book book,
            BindingResult result,
            Model model
    ) {
        if (bookService.isIsbnExists(book.getIsbn())) {
            result.addError(new FieldError("book", "isbn", "This ISBN already exists"));
        }

        if (result.hasErrors()) {
            return "add-book";
        }

        bookService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);

        if (book == null) {
            return "redirect:/books";
        }

        model.addAttribute("book", book);
        return "edit-book";
    }

    @PostMapping("/update/{id}")
    public String updateBook(
            @PathVariable Long id,
            @Valid @ModelAttribute("book") Book book,
            BindingResult result,
            Model model
    ) {
        Book existingBook = bookService.getBookById(id);

        if (existingBook == null) {
            return "redirect:/books";
        }

        if (!existingBook.getIsbn().equals(book.getIsbn()) && bookService.isIsbnExists(book.getIsbn())) {
            result.addError(new FieldError("book", "isbn", "This ISBN already exists"));
        }

        if (result.hasErrors()) {
            book.setId(id);
            return "edit-book";
        }

        existingBook.setIsbn(book.getIsbn());
        existingBook.setName(book.getName());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPrice(book.getPrice());
        existingBook.setStock(book.getStock());

        bookService.saveBook(existingBook);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}
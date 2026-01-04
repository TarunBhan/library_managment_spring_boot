package com.library.library.controller;

import com.library.library.dto.BookCopyResponse;
import com.library.library.dto.CreateBookCopyRequest;
import com.library.library.entity.Book;
import com.library.library.entity.BookCopy;
import com.library.library.entity.BookIssueHistory;
import com.library.library.mapper.BookCopyMapper;
import com.library.library.repository.BookCopyRepository;
import com.library.library.repository.BookIssueHistoryRepository;
import com.library.library.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/book-copies")
public class BookCopyController {

    private final BookCopyRepository bookCopyRepository;
    private final BookRepository bookRepository;
    private final BookIssueHistoryRepository bookIssueHistoryRepository;


    public BookCopyController(BookCopyRepository bookCopyRepository,
                              BookRepository bookRepository,BookIssueHistoryRepository bookIssueHistoryRepository) {
        this.bookCopyRepository = bookCopyRepository;
        this.bookRepository = bookRepository;
        this.bookIssueHistoryRepository=bookIssueHistoryRepository;
    }

    // ✅ Create a copy for a book
    @PostMapping("/{bookId}")
    public BookCopyResponse addCopy(
            @PathVariable Long bookId,
            @RequestBody CreateBookCopyRequest request) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        BookCopy copy = new BookCopy();
        copy.setCopyCode(request.getCopyCode());
        copy.setBarcode(request.getBarcode());
        copy.setRackLocation(request.getRackLocation());
        copy.setBook(book);
        copy.setAvailable(true);
        copy.setStatus("AVAILABLE");

        BookCopy savedCopy = bookCopyRepository.save(copy);

        return BookCopyMapper.toResponse(savedCopy);

    }


    // ✅ Get all copies of a book dto.setBookId(copy.getBook().getId());
    @GetMapping("/book/{bookId}")
    public List<BookCopy> getCopiesByBook(@PathVariable Long bookId) {
        return bookCopyRepository.findByBookId(bookId);
    }



    @PostMapping("/{copyId}/issue")
    public BookCopy issueBook(@PathVariable Long copyId) {

        BookCopy copy = bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Book copy not found"
                ));

        if (!copy.isAvailable()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Book copy is already issued"
            );
        }

        copy.setAvailable(false);
        copy.setStatus("ISSUED");

        // Save issue history
        BookIssueHistory history = new BookIssueHistory();
        history.setBook(copy.getBook());
        history.setBookCopy(copy);

        bookIssueHistoryRepository.save(history);

        return bookCopyRepository.save(copy);
    }

    @PostMapping("/{copyId}/return")
    public ResponseEntity<String> returnBook(@PathVariable Long copyId) {



        BookCopy copy = bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new  ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Book copy not found"
        ));

        if (copy.isAvailable()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Book is already returned"
            );
        }

        copy.setAvailable(true);
        copy.setStatus("AVAILABLE");

        bookCopyRepository.save(copy);

        BookIssueHistory history = bookIssueHistoryRepository
                .findByBookCopyIdAndStatus(copyId, "ISSUED")
                .orElseThrow(() -> new RuntimeException("Active issue record not found"));

        history.setReturnedAt(LocalDateTime.now());
        history.setStatus("RETURNED");
        bookIssueHistoryRepository.save(history);



        return ResponseEntity.ok("Book returned successfully");
    }
}


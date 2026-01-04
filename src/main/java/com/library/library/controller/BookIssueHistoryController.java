package com.library.library.controller;

import com.library.library.entity.BookIssueHistory;
import com.library.library.repository.BookIssueHistoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issue-history")
public class BookIssueHistoryController {

    private final BookIssueHistoryRepository historyRepository;

    public BookIssueHistoryController(BookIssueHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    // ✅ 1. Get complete issue history
    @GetMapping
    public List<BookIssueHistory> getAllHistory() {
        return historyRepository.findAll();
    }

    // ✅ 2. Get issue history by BOOK ID
    @GetMapping("/book/{bookId}")
    public List<BookIssueHistory> getHistoryByBook(@PathVariable Long bookId) {
        return historyRepository.findByBookId(bookId);
    }

    // ✅ 3. Get issue history by BOOK COPY ID
    @GetMapping("/copy/{copyId}")
    public List<BookIssueHistory> getHistoryByCopy(@PathVariable Long copyId) {
        return historyRepository.findByBookCopyId(copyId);
    }
}

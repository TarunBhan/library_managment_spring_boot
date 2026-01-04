package com.library.library.repository;

import com.library.library.entity.BookIssueHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookIssueHistoryRepository extends JpaRepository<BookIssueHistory, Long> {

    Optional<BookIssueHistory> findByBookCopyIdAndStatus(Long copyId, String status);

    List<BookIssueHistory> findByBookId(Long BookId);
    List<BookIssueHistory> findByBookCopyId(Long BookCopyId);
}

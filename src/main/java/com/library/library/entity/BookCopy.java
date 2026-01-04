package com.library.library.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "book_copies")
public class BookCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "copy_code", nullable = false, unique = true)
    private String copyCode;


    @Column(unique = true)
    private String barcode;

    @Column(nullable = false)
    private String status; // AVAILABLE, ISSUED, LOST

    @Column(name = "rack_location")
    private String rackLocation;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "book_id", insertable = false, updatable = false)
    private Long bookId;


    private boolean available = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = "AVAILABLE";
        this.available = true;
    }

    public Book getBook() {
        return book;
    }

    public Long getId(){
        return id;
    }

    public String getCopyCode() {
        return copyCode;
    }

    public void setCopyCode(String copyCode) {
        this.copyCode = copyCode;
    }

    public boolean isAvailable() {
        return available;
    }
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getRackLocation() {
        return rackLocation;
    }

    public  Long getBookId(){
        return bookId;
    }

    public void setRackLocation(String rackLocation) {
        this.rackLocation = rackLocation;
    }


    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setBook(Book book) {
        this.book = book;
    }


    // getters & setters
}

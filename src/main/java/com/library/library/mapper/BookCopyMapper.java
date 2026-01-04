package com.library.library.mapper;

import com.library.library.dto.BookCopyResponse;
import com.library.library.entity.BookCopy;

public class BookCopyMapper {

    public static BookCopyResponse toResponse(BookCopy copy) {
        BookCopyResponse dto = new BookCopyResponse();

        dto.setId(copy.getId());
        dto.setCopyCode(copy.getCopyCode());
        dto.setBarcode(copy.getBarcode());
        dto.setStatus(copy.getStatus());
        dto.setAvailable(copy.isAvailable());
        dto.setRackLocation(copy.getRackLocation());
        dto.setBookId(copy.getBookId());


        return dto;
    }
}

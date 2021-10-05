package com.frexdel.api.service;

import com.frexdel.api.entity.Book;
import com.frexdel.api.entity.Library;
import com.frexdel.api.repository.IBookRepository;
import com.frexdel.api.repository.ILibraryRepository;
import com.frexdel.api.dto.LibraryDTO;
import com.frexdel.api.dto.mapper.BookMapper;
import com.frexdel.api.dto.mapper.LibraryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LibraryService {

    private final ILibraryRepository libraryRepository;
    private final IBookRepository bookRepository;
    private final LibraryMapper libraryMapper;
    private final BookMapper bookMapper;

    public LibraryService(final ILibraryRepository libraryRepository, final IBookRepository bookRepository,
                          final LibraryMapper libraryMapper, final BookMapper bookMapper) {
        this.libraryRepository = libraryRepository;
        this.bookRepository = bookRepository;
        this.libraryMapper = libraryMapper;
        this.bookMapper = bookMapper;
    }

    public LibraryDTO save(final LibraryDTO libraryDTO) {
        final Library library = libraryMapper.toEntity(libraryDTO);

        final Library createdLibrary = libraryRepository.save(library);
        final List<Book> books = new ArrayList<>();

        libraryDTO.getBookDTOs().forEach(bookDTO -> {

            final Book book = bookMapper.toEntity(bookDTO);
            book.setLibrary(createdLibrary);
            books.add(book);

        });
        createdLibrary.setBooks(books);
        bookRepository.saveAll(books);

        return libraryMapper.toDto(createdLibrary);
    }
}


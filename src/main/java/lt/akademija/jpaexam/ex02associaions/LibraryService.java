package lt.akademija.jpaexam.ex02associaions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibraryService {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private LibraryReaderRepository libraryReaderRepository;

    @Autowired
    private BookRepository bookRepository;

    public Library getLibrary(Long id) {
        return libraryRepository.find(id);
    }

    public Library createLibrary(Library l) {
        return libraryRepository.saveOrUpdate(l);
    }

    @Transactional
    public LibraryReader joinNewReader(Long libraryId, LibraryReader r) {
        LibraryReader savedReader = libraryReaderRepository.saveOrUpdate(r);
        Library library = libraryRepository.find(libraryId);
        library.getReaders().add(savedReader);

        return savedReader;
    }

    @Transactional
    public Book enterNewBook(Long libraryId, Book b) {
        Book savedBook = bookRepository.saveOrUpdate(b);
        Library library = libraryRepository.find(libraryId);
        library.getBooks().add(savedBook);
        return savedBook;
    }

    @Transactional
    public LibraryReader borrowBook(Long bookId, Long readerId) {
        LibraryReader reader = libraryReaderRepository.find(readerId);
        Book book = bookRepository.find(bookId);
        reader.addBorrowedBook(book);
        return reader;
    }
}

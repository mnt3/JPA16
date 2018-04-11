package lt.akademija.jpaexam.ex02associaions;

import java.util.ArrayList;
import java.util.List;

import lt.akademija.jpaexam.grader.Grader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionTemplate;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class Ex02Test {

    @Autowired
    TransactionTemplate template;

    @Autowired
    LibraryService service;

    @Autowired
    LibraryRepository libraryRepository;
    @Autowired
    LibraryReaderRepository libraryReaderRepository;

    @Test
    public void libraryCreation() {
        Grader.graded(1, () -> {
            Library saved = libraryRepository.saveOrUpdate(createEmptyLibrary("Test"));

            assertThat(saved.getId(), is(1L));
            assertThat(saved.getName(), is("Test"));
            assertThat(saved.getBooks(), anyOf(empty(), nullValue()));
            assertThat(saved.getReaders(), anyOf(empty(), nullValue()));
        });
    }

    @Test
    public void libraryReaderCreation() {
        Grader.graded(1, () -> {
            LibraryReader r = new LibraryReader();
            r.setFirstName("John");
            r.setLastName("Johnny");
            LibraryReader saved = libraryReaderRepository.saveOrUpdate(r);

            assertThat(saved.getId(), is(1L));
            assertThat(saved.getFirstName(), is("John"));
            assertThat(saved.getLastName(), is("Johnny"));
        });
    }

    @Test
    public void libraryReaderWithAddressInsertsAndUpdates() {
        Grader.graded(3, () -> {
            LibraryReader r = createReader("John");

            LibraryReaderAddress addressVilnius = new LibraryReaderAddress();
            addressVilnius.setCity("Vilnius");
            addressVilnius.setStreet("Zirmunu 123");
            addressVilnius.setZipcode("123345");
            r.getAddresses().add(addressVilnius);

            LibraryReaderAddress addressKaunas = new LibraryReaderAddress();
            addressKaunas.setCity("Kaunas");
            addressKaunas.setStreet("Laisves al. 123");
            addressKaunas.setZipcode("123345");
            r.getAddresses().add(addressKaunas);

            LibraryReader saved = libraryReaderRepository.saveOrUpdate(r);

            assertThat(saved.getAddresses(), hasSize(2));
            assertThat(saved.getAddresses().get(0).getCity(), is("Vilnius"));
            assertThat(saved.getAddresses().get(1).getCity(), is("Kaunas"));

            saved.getAddresses().get(0).setCity("RenamedVilnius");

            libraryReaderRepository.saveOrUpdate(saved);

            template.execute(s -> {
                LibraryReader updated = libraryReaderRepository.find(saved.getId());
                assertThat(updated.getAddresses().get(0).getCity(), is("RenamedVilnius"));
                return null;
            });
        });
    }


    @Test
    public void shouldNotAllowUpdateNorSaveOfReadersWhenSavingLibrary() {
        Grader.graded(3, () -> {
            try {
                Library library = createEmptyLibrary("Test");
                LibraryReader reader = createReader("First reader");
                library.setReaders(new ArrayList<>());
                library.getReaders().add(reader);
                Library saved = libraryRepository.saveOrUpdate(library);
                template.execute(s -> {
                    List<LibraryReader> readers = libraryRepository.find(saved.getId()).getReaders();
                    assertThat(readers, empty());
                    return null;
                });
            } catch (InvalidDataAccessApiUsageException e) {
                e.printStackTrace(); //All ok.
            }
        });
    }

    @Test
    public void readersJoinParticularLibrary() {
        Grader.graded(3, () -> {
            Long lid = service.createLibrary(createEmptyLibrary("Test")).getId();
            service.joinNewReader(lid, createReader("First reader"));
            service.joinNewReader(lid, createReader("Second reader"));

            template.execute(s -> {
                Library l = service.getLibrary(lid);
                assertThat(l.getReaders(), hasSize(2));
                assertThat(l.getReaders().get(0).getFirstName(), is("First reader"));
                assertThat(l.getReaders().get(1).getFirstName(), is("Second reader"));
                return null;
            });
        });

    }

    @Test
    public void newBookEnteredForParticularLibrary() {
        Grader.graded(3, () -> {
            Long lid = service.createLibrary(createEmptyLibrary("Test")).getId();
            service.enterNewBook(lid, createBook("First book"));
            service.enterNewBook(lid, createBook("Second book"));

            template.execute(s -> {
                Library l = service.getLibrary(lid);
                assertThat(l.getBooks(), hasSize(2));
                assertThat(l.getBooks().get(0).getTitle(), is("First book"));
                assertThat(l.getBooks().get(1).getTitle(), is("Second book"));
                return null;
            });
        });
    }

    @Test
    public void borrowBookFromLibrary() {
        Grader.graded(5, () -> {
            Long lid = service.createLibrary(createEmptyLibrary("Test")).getId();
            service.enterNewBook(lid, createBook("First book"));
            Book book = service.enterNewBook(lid, createBook("Second book"));

            service.joinNewReader(lid, createReader("First reader"));
            LibraryReader reader = service.joinNewReader(lid, createReader("Second reader"));

            service.borrowBook(book.getId(), reader.getId());

            template.execute(s -> {
                List<Book> borrowedBooks = libraryReaderRepository.find(reader.getId()).getBorrowedBooks();
                assertThat(borrowedBooks, hasSize(1));
                assertThat(borrowedBooks.get(0).getTitle(), is(book.getTitle()));
                return null;
            });
        });
    }

    @Test
    public void bookReadersBidirectionalMappingIsSyncedInObjectModel() {
        Grader.graded(2, () -> {
            LibraryReader r = new LibraryReader();
            Book b = new Book();
            r.addBorrowedBook(b);

            assertThat(r.getBorrowedBooks(), hasSize(1));
            assertThat(b.getBookReaders(), hasSize(1));
            assertThat(r.getBorrowedBooks().get(0), sameInstance(b));
            assertThat(b.getBookReaders().get(0), sameInstance(r));
        });
    }

    private Book createBook(String title) {
        Book b = new Book();
        b.setTitle(title);
        b.setAuthor("The Author");
        return b;
    }

    private LibraryReader createReader(String firstName) {
        LibraryReader r = new LibraryReader();
        r.setFirstName(firstName);
        r.setLastName("Johnny");
        r.setAddresses(new ArrayList<>());
        return r;
    }

    private Library createEmptyLibrary(String name) {
        Library l = new Library();
        l.setName(name);
        return l;
    }
}
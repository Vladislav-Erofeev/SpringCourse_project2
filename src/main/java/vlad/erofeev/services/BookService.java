package vlad.erofeev.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vlad.erofeev.models.Book;
import vlad.erofeev.models.Person;
import vlad.erofeev.repositories.BookRepository;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final PersonService personService;

    private int booksPerPage = 3;
    private boolean sort  = false;

    public void setBooksPerPage(int booksPerPage) {
        this.booksPerPage = booksPerPage;
    }

    public int getBooksPerPage() {
        return booksPerPage;
    }

    @Autowired
    public BookService(BookRepository bookRepository, PersonService personService) {
        this.bookRepository = bookRepository;
        this.personService = personService;
    }

    public boolean isSort() {
        return sort;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }

    public Page<Book> getBooks(int page) {
        if(sort) {
            if (booksPerPage == 0)
                return new PageImpl<>(bookRepository.findAll(Sort.by("year")));
            return bookRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year")));
        } else {
            if (booksPerPage == 0)
                return new PageImpl<>(bookRepository.findAll());
            return bookRepository.findAll(PageRequest.of(page, booksPerPage));
        }
    }

    public Book getBook(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public void add(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book) {
        Book book1 = getBook(id);
        book.setOwner(book1.getOwner());
        book.setOrder_date(book1.getOrder_date());
        book.setId(id);
        Hibernate.initialize(book.getOwner());
        bookRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public Person getOwner(int id) {
        Book book = bookRepository.findById(id).orElse(null);
        if(book == null) {
            return null;
        }
        return book.getOwner();
    }

    @Transactional
    public void deleteOwner(int id) {
        bookRepository.findById(id).orElse(null).setOwner(null);
    }

    @Transactional
    public void setOwner(int id, int person_id) {
        Book book = bookRepository.findById(id).orElse(null);
        if(book != null) {
            Person person = personService.get(person_id);
            Date date = new Date(System.currentTimeMillis());
            date.setDate(date.getDay() + 14);
            book.setOrder_date(date);
            book.setOwner(person);
        }
    }

    public List<Book> findByName(String name) {
        return bookRepository.findByNameIgnoreCaseStartsWith(name);
    }

    public void insert100Books() {
        Random random = new Random();
        for(int i = 0; i < 100; i ++) {
            Book book = new Book("Book N" + random.nextInt(1000),
                    "Author " + random.nextInt(1000), random.nextInt(1000)+100);
            add(book);
        }
    }
}

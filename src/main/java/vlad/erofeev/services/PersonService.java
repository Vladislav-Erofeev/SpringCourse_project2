package vlad.erofeev.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vlad.erofeev.models.Book;
import vlad.erofeev.models.Person;
import vlad.erofeev.repositories.PersonRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository repository;

    @Autowired
    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public List<Person> getPeople() {
        return repository.findAll();
    }

    public Person get(int id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional(readOnly = false)
    public void add(Person person) {
        repository.save(person);
    }

    @Transactional(readOnly = false)
    public void update(int id, Person person) {
        person.setId(id);
        repository.save(person);
    }

    @Transactional(readOnly = false)
    public void delete(int id) {
        repository.deleteById(id);
    }

    public List<Book> getBooks(int id) {
        Person person = repository.findById(id).orElse(null);
        if(person == null) {
            return new ArrayList<>();
        }
        Hibernate.initialize(person.getBooks());
        List<Book> books = person.getBooks();
        books.stream().forEach(book -> {if(new Date(System.currentTimeMillis()).after(book.getOrder_date())) book.setOverdue(true);});
        return person.getBooks();
    }
}

package vlad.erofeev.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vlad.erofeev.models.Book;
import vlad.erofeev.services.BookService;
import vlad.erofeev.services.PersonService;

@Controller
@RequestMapping("/books")
public class BookController {
    private BookService bookService;
    private PersonService personService;

    @Autowired
    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping
    public String booksPage(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                            Model model) {
        model.addAttribute("books", bookService.getBooks(page));
        model.addAttribute("page", page);
        model.addAttribute("booksPerPage", bookService.getBooksPerPage());
        model.addAttribute("sort", bookService.isSort());
        return "book/books";
    }

    @PostMapping()
    public String setBooksCount(@RequestParam(value = "booksPerPage", required = false, defaultValue = "3") Integer booksPerPage,
                                @RequestParam(value = "sort", required = false, defaultValue = "false") Boolean sort) {
        bookService.setBooksPerPage(booksPerPage);
        bookService.setSort(sort);
        return "redirect: /books";
    }

    @GetMapping("/{id}")
    public String bookPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.getBook(id));
        model.addAttribute("person", bookService.getOwner(id));
//        bookService.insert100Books();
        model.addAttribute("people", personService.getPeople());
        return "book/show";
    }

    @DeleteMapping("/{id}")
    public String deleteOwner(@PathVariable("id") int id) {
        bookService.deleteOwner(id);
        return "redirect: /books/" + id;
    }

    @PatchMapping("/{id}")
    public String setOwner(@RequestParam("person_id") int person_id,
                           @PathVariable("id") int id) {
        bookService.setOwner(id, person_id);
        return "redirect: /books/" + id;
    }

    @GetMapping("/new")
    public String newPage(@ModelAttribute("book")Book book) {
        return "book/new";
    }

    @PostMapping("/new")
    public String newBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "book/new";
        }
        bookService.add(book);
        return "redirect: /books";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.getBook(id));
        return "book/edit";
    }

    @PatchMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book,
                       BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "book/edit";
        bookService.update(id, book);
        return "redirect: /books/" + id;
    }

    @GetMapping("/{id}/delete")
    public String deletePage(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.getBook(id));
        return "book/delete";
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect: /books";
    }

    @GetMapping("/search")
    public String searchPage(@RequestParam(name = "search", defaultValue = "", required = false) String name,
                             Model model) {
        model.addAttribute("search", name);
        model.addAttribute("books", bookService.findByName(name));
        return "book/search";
    }
}

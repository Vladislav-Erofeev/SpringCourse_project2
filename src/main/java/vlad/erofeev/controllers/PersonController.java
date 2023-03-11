package vlad.erofeev.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vlad.erofeev.models.Person;
import vlad.erofeev.services.PersonService;

@Controller
@RequestMapping("/people")
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public String indexPage(Model model) {
        model.addAttribute("people", personService.getPeople());
        return "person/people";
    }

    @GetMapping("/{id}")
    public String peoplePage(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personService.get(id));
        model.addAttribute("books", personService.getBooks(id));
        return "person/show";
    }

    @GetMapping("/new")
    public String addPersonPage(@ModelAttribute("person")Person person) {
        return "person/new";
    }

    @PostMapping("/new")
    public String addPerson(@ModelAttribute("person") @Valid Person person,
                            BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "person/new";
        personService.add(person);
        return "redirect: /people";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personService.get(id));
        return "person/edit";
    }

    @PatchMapping("/{id}/edit")
    public String edit(@ModelAttribute("person") @Valid Person person,
                       BindingResult bindingResult,
                       @PathVariable("id") int id) {
        if(bindingResult.hasErrors())
            return "person/edit";
        personService.update(id, person);
        return "redirect: /people/" + id;
    }

    @GetMapping("/{id}/delete")
    public String deletePage(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personService.get(id));
        return "person/delete";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personService.delete(id);
        return "redirect: /people";
    }
}

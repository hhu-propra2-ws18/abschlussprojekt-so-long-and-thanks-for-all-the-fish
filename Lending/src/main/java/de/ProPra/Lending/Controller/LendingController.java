package de.ProPra.Lending.Controller;

import de.ProPra.Lending.Dataaccess.ArticleRepository;
import de.ProPra.Lending.Dataaccess.LendingRepository;
import de.ProPra.Lending.Dataaccess.LendingRepresentation;
import de.ProPra.Lending.Dataaccess.PersonRepository;
import de.ProPra.Lending.Model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class LendingController {

    private LendingRepository lendings;
    private PersonRepository persons;
    private ArticleRepository articles;

    @Autowired
    public LendingController(LendingRepository lendings, PersonRepository persons, ArticleRepository articles){
        this.lendings = lendings;
        this.articles = articles;
        this.persons = persons;
    }

    @GetMapping("/lendings/{lendID}")
        public String LendingPage(Model model, @PathVariable final long lendID) {
            LendingRepresentation filledLendings = new LendingRepresentation(lendings,persons,articles);
            model.addAttribute("lendings", filledLendings.fillLendings(lendID));
            return "overviewLendings";
        }

    @GetMapping("/borrows/{borrowID}")
    public String BorrowPage(Model model, @PathVariable final long borrowID) {
        LendingRepresentation filledLendings = new LendingRepresentation(lendings,persons,articles);
        model.addAttribute("lendings", filledLendings.fillBorrows(borrowID));
        return "overviewBorrows";
    }

    @GetMapping("/{id}")
    public String Overview(Model model, @PathVariable final long id) {
        model.addAttribute("id", id);
        return "overview";
    }
}

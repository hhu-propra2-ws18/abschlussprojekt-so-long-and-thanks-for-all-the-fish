package de.hhu.rhinoshareapp.controller.user;

import com.sun.org.apache.xpath.internal.operations.Mod;
import de.hhu.rhinoshareapp.domain.model.Address;
import de.hhu.rhinoshareapp.domain.security.ActualUserChecker;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminPageController {


    @Autowired
    UserRepository users;

    @GetMapping("/admin")
    public String loadAdminPage(Model m, Principal p) {
        ActualUserChecker.checkActualUser(m, p, users);
        return "Admin/admin_overview";
    }

    @GetMapping("/admin/usermanagement")
    public String loadUserManagement(Principal p, Model m) {
        ActualUserChecker.checkActualUser(m, p, users);
        List<User> userlist = users.findAll();
        m.addAttribute("userlist", userlist);
        return "Admin/admin_usermanagement";
    }

    @GetMapping("/admin/usermanagement/edit/{id}")
    public String loadEditForm(Model m, Principal p, @PathVariable long id) {
        ActualUserChecker.checkActualUser(m, p, users);
        Optional<User> user = users.findUserByuserID(id);
        m.addAttribute("userDetails", user.get());
        return "Admin/admin_userEdit";
    }

    //Edit some fields in User-Database
    @PostMapping("/admin/usermanagement/changesurname/{id}")
    public String changeSurname(Model m, Principal p, String surname, @PathVariable long id) {
        ActualUserChecker.checkActualUser(m, p, users);
        Optional<User> user = users.findUserByuserID(id);
        User u = user.get();
        u.setSurname(surname);
        users.save(u);
        return "redirect:/admin/usermanagement/edit/" + id;
    }

    @PostMapping("/admin/usermanagement/changename/{id}")
    public String changeName(Model m, Principal p, String name, @PathVariable long id) {
        ActualUserChecker.checkActualUser(m, p, users);
        Optional<User> user = users.findUserByuserID(id);
        User u = user.get();
        u.setName(name);
        users.save(u);
        return "redirect:/admin/usermanagement/edit/" + id;
    }

    @PostMapping("/admin/usermanagement/changestreet/{id}")
    public String changeStreet(Model m, Principal p, String street, @PathVariable long id) {
        ActualUserChecker.checkActualUser(m, p, users);
        Optional<User> user = users.findUserByuserID(id);
        User u = user.get();
        Address a = u.getAddress();
        a.setStreet(street);
        u.setAddress(a);
        users.save(u);
        return "redirect:/admin/usermanagement/edit/" + id;
    }

    @PostMapping("/admin/usermanagement/changehousenumber/{id}")
    public String changeHouseNumber(Model m, Principal p, String housenumber, @PathVariable long id) {
        ActualUserChecker.checkActualUser(m, p, users);
        Optional<User> user = users.findUserByuserID(id);
        User u = user.get();
        Address a = u.getAddress();
        a.setHouseNumber(housenumber);
        u.setAddress(a);
        users.save(u);
        return "redirect:/admin/usermanagement/edit/" + id;
    }

    @PostMapping("/admin/usermanagement/changepostcode/{id}")
    public String changePostalCode(Model m, Principal p, String postcode, @PathVariable long id) {
        ActualUserChecker.checkActualUser(m, p, users);
        Optional<User> user = users.findUserByuserID(id);
        User u = user.get();
        Address a = u.getAddress();
        a.setPostCode(postcode);
        u.setAddress(a);
        users.save(u);
        return "redirect:/admin/usermanagement/edit/" + id;
    }

    @PostMapping("/admin/usermanagement/changecity/{id}")
    public String changeCity(Model m, Principal p, String city, @PathVariable long id) {
        ActualUserChecker.checkActualUser(m, p, users);
        Optional<User> user = users.findUserByuserID(id);
        User u = user.get();
        Address a = u.getAddress();
        a.setCity(city);
        u.setAddress(a);
        users.save(u);
        return "redirect:/admin/usermanagement/edit/" + id;
    }

    @PostMapping("/admin/usermanagement/changecountry/{id}")
    public String changeCountry(Model m, Principal p, String country, @PathVariable long id) {
        ActualUserChecker.checkActualUser(m, p, users);
        Optional<User> user = users.findUserByuserID(id);
        User u = user.get();
        Address a = u.getAddress();
        a.setCountry(country);
        u.setAddress(a);
        users.save(u);
        return "redirect:/admin/usermanagement/edit/" + id;
    }

    @PostMapping("/admin/usermanagement/changeusername/{id}")
    public String changeUserName(Model m, Principal p, String username, @PathVariable long id) {
        ActualUserChecker.checkActualUser(m, p, users);
        Optional<User> user = users.findUserByuserID(id);
        User u = user.get();
        u.setUsername(username);
        users.save(u);
        return "redirect:/admin/usermanagement/edit/" + id;
    }

    @PostMapping("/admin/usermanagement/changeemail/{id}")
    public String changeEmail(Model m, Principal p, String email, @PathVariable long id) {
        ActualUserChecker.checkActualUser(m, p, users);
        Optional<User> user = users.findUserByuserID(id);
        User u = user.get();
        u.setEmail(email);
        users.save(u);
        return "redirect:/admin/usermanagement/edit/" + id;
    }

    @PostMapping("/admin/usermanagement/changescore/{id}")
    public String changeScore(Model m, Principal p, String score, @PathVariable long id) {
        ActualUserChecker.checkActualUser(m, p, users);
        Optional<User> user = users.findUserByuserID(id);
        User u = user.get();
        u.setScore(Integer.parseInt(score));
        users.save(u);
        return "redirect:/admin/usermanagement/edit/" + id;
    }

    //Eintrag löschen

    @GetMapping("/admin/usermanagement/delete/{id}")
    public String deleteUser(Model m, Principal p, @PathVariable long id) {
        ActualUserChecker.checkActualUser(m, p, users);

        users.deleteById(id);
        return "redirect:/admin/usermanagement/";
    }

}

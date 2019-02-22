package de.hhu.rhinoshareapp.controller.user;

import de.hhu.rhinoshareapp.domain.security.ActualUserChecker;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

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

}

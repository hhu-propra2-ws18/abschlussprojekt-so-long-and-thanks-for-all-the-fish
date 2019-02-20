package de.hhu.rhinoshareapp.web;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin")
    public String loadAdminPage() {
        return "adminpage";
    }

}

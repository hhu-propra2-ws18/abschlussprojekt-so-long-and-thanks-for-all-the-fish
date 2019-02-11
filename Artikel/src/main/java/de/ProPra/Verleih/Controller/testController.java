package de.ProPra.Verleih.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class testController {

    @GetMapping("/")
        public String mainPage() {
            return "hallo";

        }
}

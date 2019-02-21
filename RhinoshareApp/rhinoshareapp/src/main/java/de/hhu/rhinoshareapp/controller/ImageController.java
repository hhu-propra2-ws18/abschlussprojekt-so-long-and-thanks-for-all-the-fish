package de.hhu.rhinoshareapp.controller;

import de.hhu.rhinoshareapp.domain.model.Image;
import de.hhu.rhinoshareapp.domain.service.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ImageController {

    @Autowired
    ImageRepository imageRepository;

    @RequestMapping("/image/{id}")
    @ResponseBody
    public byte[] returnCurrentImage(@PathVariable long id){
        Image image = imageRepository.findById(id);
        return image.getFilebytes();
    }

}

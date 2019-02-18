package de.ProPra.Articles.infrastructure.web;

import de.ProPra.Articles.domain.model.Image;
import de.ProPra.Articles.domain.service.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class ImageController {

    @Autowired
    ImageRepository imageRepository;

    @RequestMapping(value = "/imageController/{imageId}")
    @ResponseBody
    public byte[] helloWorld(@PathVariable long imageId)  {
        Image image = imageRepository.findById(imageId).get();
        return image.getFilebytes();
    }

}

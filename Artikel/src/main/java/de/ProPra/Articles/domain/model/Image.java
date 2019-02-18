package de.ProPra.Articles.domain.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.util.Base64;

@Data
@Entity
public class Image {

    @Id
    @GeneratedValue
    public long imageID;
    public String filename;
    @Lob
    public byte[] filebytes;

    public Image() {

    }
//Base64.getEncoder().encode
    public Image(MultipartFile file) throws IOException {
        this.filebytes = file.getBytes();
        this.filename =file.getOriginalFilename();
    }
}

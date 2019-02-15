package de.ProPra.Articles.domain.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;

@Data
@Entity
public class Image {

    public byte[] filebytes;

    @Id
    @GeneratedValue
    public long imageID;

    @ManyToOne (targetEntity = Article.class)
    @JoinColumn
    public long articleID;

    public Image(MultipartFile file) throws IOException {
        this.filebytes = file.getBytes();
    }
}

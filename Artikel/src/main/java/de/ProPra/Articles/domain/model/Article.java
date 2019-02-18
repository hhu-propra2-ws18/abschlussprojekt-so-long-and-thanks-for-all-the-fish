package de.ProPra.Articles.domain.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.sql.SQLException;

@Entity
@Data
public class Article {
    @Id
    @GeneratedValue
    public long articleID;

    String name;

    String comment;

    //@OneToOne
    long personID;

    int deposit;

    int rent;

    @Transient
    public MultipartFile file;

    @OneToOne (fetch = FetchType.EAGER, cascade={CascadeType.PERSIST})
    public Image image;

    boolean available;


    public Article(){
    }

    public Article(String name, String comment, int personID, int deposit, int rent, boolean available, MultipartFile file) throws IOException, SQLException {
        this.name = name;
        this.comment = comment;
        this.personID = personID;
        this.deposit = deposit;
        this.rent = rent;
        this.available = available;
        this.file = file;
    }

    public void saveImage() throws IOException {
        this.image = new Image(file);
    }
    public long getImageID(){
        return image.getImageID();
    }
}


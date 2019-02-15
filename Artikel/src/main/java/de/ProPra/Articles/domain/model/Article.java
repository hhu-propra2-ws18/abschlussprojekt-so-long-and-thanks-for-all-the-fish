package de.ProPra.Articles.domain.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    double deposit;

    double rent;

    @Transient
    public MultipartFile file;

    @OneToMany (cascade = CascadeType.ALL)
    public List<Image> images = new ArrayList<>();

    boolean available;


    public Article(){
    }

    public Article(String name, String comment, int personID, double deposit, double rent, boolean available, MultipartFile file) throws IOException, SQLException {
        this.name = name;
        this.comment = comment;
        this.personID = personID;
        this.deposit = deposit;
        this.rent = rent;
        this.available = available;
        this.file = file;
    }

    public void saveImage() throws IOException {
        Image image = new Image(file);
        images.add(image);
    }
}


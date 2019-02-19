package de.ProPra.Articles.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.sql.SQLException;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    public Article(String name, String comment, long personID, int deposit, int rent, boolean available, MultipartFile file) throws IOException, SQLException {
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


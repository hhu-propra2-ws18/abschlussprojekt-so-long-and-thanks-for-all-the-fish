package de.hhu.rhinoshareapp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue
    public long articleID;

    String name;

    @Lob
    String comment;

    @OneToOne
    User owner;

    int deposit;

    int rent;

    @Transient
    MultipartFile file;

    @OneToMany (fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    public List<Image> image = new ArrayList<>();

    boolean available;

    Calendar finalStartDate;

    Calendar finalEndDate;

    boolean isRequested;

    @Lob
    String requestComment;

    public Article(String name, String comment, int deposit, int rent, boolean available, MultipartFile file) throws IOException {
        this.name = name;
        this.comment = comment;
        this.deposit = deposit;
        this.rent = rent;
        this.available = available;
        this.file = file;
        this.saveImage();
    }

    public void saveImage() throws IOException {
        this.image.add(new Image(file));
    }
}
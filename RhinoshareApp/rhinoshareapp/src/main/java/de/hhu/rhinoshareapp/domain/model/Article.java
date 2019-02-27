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

    int sellingPrice;

    @Transient
    MultipartFile file;

    @OneToOne (cascade={CascadeType.ALL})
    public Image image;

    boolean available;

    boolean forSale;

    Calendar finalStartDate;

    Calendar finalEndDate;

    boolean isRequested;

    @Lob
    String requestComment;

    public Article(String name, String comment, int deposit, int rent, boolean available, MultipartFile file){
        this.name = name;
        this.comment = comment;
        this.deposit = deposit;
        this.rent = rent;
        this.available = available;
        this.file = file;
    }

    public void saveImage() throws IOException {
        this.image = new Image(file);
    }

    public long getImageID() {
    	return this.image.getImageID();
	}

	public boolean imageIsEmpty(){
        if(image == null)
            return true;
        return getFilebytes().length == 0;
    }

    public byte[] getFilebytes(){
        return image.getFilebytes();
    }

    public boolean isActive(){
        return this.owner != null;
    }
}
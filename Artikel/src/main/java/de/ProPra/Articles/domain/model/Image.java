package de.ProPra.Articles.domain.model;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

@Data
public class Image {

    public Blob fileblob;
    public String filename;

    public Image(MultipartFile file) throws IOException, SQLException {
        this.filename = file.getOriginalFilename();
        this.fileblob = new javax.sql.rowset.serial.SerialBlob(file.getBytes());
    }
}

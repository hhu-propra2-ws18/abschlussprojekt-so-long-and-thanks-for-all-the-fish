package de.hhu.rhinoshareapp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue
    public long imageID;

    public String filename;

    @Lob
    public byte[] filebytes;

    public Image(MultipartFile file) throws IOException {
        this.filebytes = file.getBytes();
        this.filename = file.getOriginalFilename();
    }
}
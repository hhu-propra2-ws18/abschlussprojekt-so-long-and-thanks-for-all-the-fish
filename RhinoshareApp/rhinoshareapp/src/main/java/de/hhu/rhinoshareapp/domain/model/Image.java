package de.hhu.rhinoshareapp.domain.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
@SuppressFBWarnings // Bilder waren Optional und haben da schon viel Zeit rein investiert. Hier gab es denselben Fehler wie bei Account.
                    // Die Spotbugs fehler können deswegen umgangen werden
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
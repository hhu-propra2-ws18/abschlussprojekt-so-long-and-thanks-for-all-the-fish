package de.hhu.rhinoshareapp.domain.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings // Images sind optional gewesen und wir konnten auf die schnelle keine andere lösung finden
public class Image {

    @Id
    @GeneratedValue
    public long imageID;

    public String filename;

    @Type(type="org.hibernate.type.BinaryType")
    public byte[] filebytes;

    public Image(MultipartFile file) throws IOException {
        this.filebytes = file.getBytes();
        this.filename = file.getOriginalFilename();
    }

}
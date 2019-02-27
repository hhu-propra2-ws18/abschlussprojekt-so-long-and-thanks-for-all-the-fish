package de.hhu.rhinoshareapp.chat.model;

import de.hhu.rhinoshareapp.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue
    long messageID;

  /*  @OneToOne
    private User from;


    @OneToOne
    private User to;
*/
    private String fromName;

    private String toName;

    private String context;

    public ChatMessage(String from, String to, String context) {
        this.context = context;
        this.fromName = from;
        this.toName = to;
    }


}

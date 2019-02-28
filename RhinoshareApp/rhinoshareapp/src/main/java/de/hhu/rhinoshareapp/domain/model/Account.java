package de.hhu.rhinoshareapp.domain.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings //Account wurde uns von Propay vorgegeben und zeigt wegen reservations immer ienen Spotbugs-Fehler an.


public class Account {
    @Id
    String account;

    double amount;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn
    Reservation[] reservations;

}

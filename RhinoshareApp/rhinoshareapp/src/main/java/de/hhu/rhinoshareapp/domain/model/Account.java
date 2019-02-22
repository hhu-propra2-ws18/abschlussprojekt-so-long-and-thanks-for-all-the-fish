package de.hhu.rhinoshareapp.domain.model;

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
public class Account {
    @Id
    String account;

    double amount;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn
    Reservation[] reservations;

}

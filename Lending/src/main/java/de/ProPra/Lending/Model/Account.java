package de.ProPra.Lending.Model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Builder
@Data
@Entity
public class Account {
    @Id
    String account;
    double amount;
    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn
    Reservation[] reservations;

}

package de.ProPra.Lending.Model;

import lombok.Data;

import javax.persistence.*;

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

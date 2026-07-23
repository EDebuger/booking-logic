package models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

import org.jspecify.annotations.NonNull;

@Entity
@Table(name = "partner_companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name",length = 50,unique = true)
    @NonNull
    private String name;

    @Column(name = "telefone",length = 20,unique = true)
    @NonNull
    private String telefone;

    @Column(name = "email",length = 75,unique = true)
    @NonNull
    private String email;

    //@Column(name = "")
}

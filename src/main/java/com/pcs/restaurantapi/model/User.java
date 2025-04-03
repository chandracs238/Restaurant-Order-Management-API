package com.pcs.restaurantapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Cart cart;
    private LocalDate dob;
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public <E> User(String testUser, String password, List<E> roleCustomer) {
        this.username = testUser;
        this.password = password;
        this.role = (Role) roleCustomer.getFirst();
    }
}

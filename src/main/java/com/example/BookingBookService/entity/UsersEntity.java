package com.example.BookingBookService.entity;

import com.example.BookingBookService.model.ERole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Email
    @Size(max = 255)
    @Column(nullable = false, unique = true)
    private String email;

    /* ---------- ROLE UŻYTKOWNIKA ---------- */
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<ERole> roles = new HashSet<>();

    /* ---------- KONSTRUKTORY ---------- */
    public UsersEntity() {}

    public UsersEntity(String username,
                       String email,
                       String password,
                       Set<ERole> roles) {
        this.username = username;
        this.email    = email;
        this.password = password;
        this.roles    = roles;
    }

    /* ---------- GETTERY / SETTERY ---------- */
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Set<ERole> getRoles() { return roles; }

    public void setRoles(Set<ERole> roles) { this.roles = roles; }

    /* ---------- METODY POMOCNICZE ---------- */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsersEntity)) return false;
        return id != null && id.equals(((UsersEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

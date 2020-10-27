package com.urfusoftware.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String name;

    @Transient
    private String selected;

    public Role(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Set<User> users;

    @Override
    public String getAuthority() {
        return name;
    }
}

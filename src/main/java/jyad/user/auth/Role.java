package jyad.user.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

@Entity
public class Role {

    @Id
    @Column(name = "role_id", unique = true, updatable = false, nullable = false)
    private Long id;

    @Column(name = "role_name", nullable = false, updatable = false, unique = true)
    private String name;



}

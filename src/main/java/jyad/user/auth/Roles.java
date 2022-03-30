package jyad.user.auth;

import jyad.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

public class Roles {
    private static final String ROLE_NAME_USER = "USER";
    private static final String ROLE_NAME_ADMIN = "ADMIN";

    public static final Role ROLE_USER = new Role(ROLE_NAME_USER);
    public static final Role ROLE_ADMIN = new Role(ROLE_NAME_ADMIN);

    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Getter
    @Setter
    @Table(name = "roles")
    public static class Role {
        @Id
        @Column(name = "id")
        private Long id;

        @Column(name = "role_name")
        private String roleName;

        @ManyToMany(mappedBy = "roles")
        private Set<User> users;

        public boolean equals(Role role) {
            return this.roleName.equals(role.roleName);
        }

        public Role(String roleName) {
            this.roleName = roleName;
        }


    }
}
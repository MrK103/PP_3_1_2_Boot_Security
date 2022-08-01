package ru.kata.spring.boot_security.demo.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long userId;

   public User(String name, String surname, byte age, String email, String username, String password, Set<Role> roles) {
      this.name = name;
      this.surname = surname;
      this.age = age;
      this.email = email;
      this.username = username;
      this.password = password;
      this.roles = roles;
   }

   @Pattern(regexp = "[A-Za-z]{2,15}", message = "Name should be between 2 and 15 latin characters")
   private String name;

   @Pattern(regexp = "[A-Za-z]{2,15}", message = "Name should be between 2 and 15 latin characters")
   private String surname;

   @Min(value = 0, message = "Age should be >= 0")
   @Max(value = 127, message = "Age should be < 128")
   private byte age;

   @Pattern(regexp = "([A-z0-9_.-]+)@([A-z0-9_.-]+).([A-z]{2,8})", message = "Enter correct email")
   private String email;

   @NotEmpty(message = "Username cannot be empty")
   @Size(min = 2, max = 15, message = "Name should be between 2 and 15 latin characters")
   @Column(unique = true)
   private String username;

   @NotEmpty(message = "Password cannot be empty")
   @Size(min = 4, message = "Password should be greater then 4 symbols")
   private String password;

   @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(name = "users_roles",
           joinColumns = @JoinColumn(name = "userId"),
           inverseJoinColumns = @JoinColumn(name = "roleId"))
   private Set<Role> roles;

   public User() {
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return getRoles();
   }

   @Override
   public String getPassword() {
      return password;
   }

   @Override
   public String getUsername() {
      return username;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }

    public void setPassword(String oldPassword) {
      this.password = oldPassword;
    }
}
package com.example.security_demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    // @Min(value = 3, message = "First Name should atleast be of three characters")
    // @Max(value = 100, message = "First Name cannot exceed 100 characters")
    @Column(length = 20)
    private String firstName;

    @NotBlank
    // @Min(value = 3, message = "Last Name should atleast be of three characters")
    // @Max(value = 100, message = "Last Name cannot exceed 100 characters")
    private String lastName;

    // @NotBlank
    // @Max(value = 12, message = "USN cannot be more than 12 characters")
    // private String USN;

    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    private UserAuthority userAuthority;
}

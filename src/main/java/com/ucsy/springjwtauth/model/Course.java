package com.ucsy.springjwtauth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name field is required")
    @Column(unique = true, nullable = false)
    private String name;
    @NotNull(message = "Duration field is required")
    private Integer duration;
    @NotNull(message = "Price field is required")
    private Integer price;
}

package org.redflag.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "sdk_client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SdkClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID login;

    @Column(nullable = false)
    private String password;
}
package org.redflag.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<OrganizationNode> organizationNodes;
}

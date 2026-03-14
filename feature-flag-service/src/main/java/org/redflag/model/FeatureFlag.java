package org.redflag.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "feature_flag")
public class FeatureFlag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "value", nullable = false)
    private Boolean value;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @ManyToOne
    @JoinColumn(name = "organization_node_id")
    private OrganizationNode organizationNode;
}

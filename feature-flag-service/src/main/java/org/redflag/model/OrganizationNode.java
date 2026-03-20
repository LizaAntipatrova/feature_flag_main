package org.redflag.model;

import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnTransformer;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "organization_node")
public class OrganizationNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @ColumnTransformer(
            read = "path::text",
            write = "?::ltree"
    )
    @Column(name = "path", columnDefinition = "ltree", insertable = false)
    private String path;

    @Column(name = "is_service", nullable = false)
    private Boolean isService;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @OneToMany(mappedBy = "organizationNode", cascade = CascadeType.ALL)
    private List<FeatureFlag> featureFlags;

    @DateCreated
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @DateUpdated
    @Column(name = "updated_at")
    private Instant updatedAt;
}

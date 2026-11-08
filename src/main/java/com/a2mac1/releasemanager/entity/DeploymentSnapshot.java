package com.a2mac1.releasemanager.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deployment_snapshots")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeploymentSnapshot extends BaseEntity {



    @Column(name = "system_version", nullable = false)
    private Integer systemVersion;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "service_version", nullable = false)
    private Integer serviceVersion;

    public DeploymentSnapshot(
            int systemVersion,
            String serviceName,
            int serviceVersion) {

        this.systemVersion = systemVersion;
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
    }
}


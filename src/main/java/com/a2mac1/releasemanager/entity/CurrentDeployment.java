package com.a2mac1.releasemanager.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "current_deployments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CurrentDeployment extends BaseEntity {

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "service_version")
    private Integer serviceVersion;

    public CurrentDeployment(String serviceName, int serviceVersion) {
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
    }

    public boolean hasVersion(int version) {
        return this.serviceVersion == version;
    }

    public void updateVersion(int newVersion) {
        this.serviceVersion = newVersion;
    }
}


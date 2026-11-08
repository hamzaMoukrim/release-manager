package com.a2mac1.releasemanager.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "system_state")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SystemState extends BaseEntity {

    @Column(name = "current_version", nullable = false)
    private Integer currentVersion;


    public int incrementVersion() {
        return ++currentVersion;
    }
}


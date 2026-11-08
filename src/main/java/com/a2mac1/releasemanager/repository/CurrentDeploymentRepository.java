package com.a2mac1.releasemanager.repository;

import com.a2mac1.releasemanager.entity.CurrentDeployment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CurrentDeploymentRepository extends JpaRepository<CurrentDeployment, UUID> {
    Optional<CurrentDeployment> findByServiceName(String serviceName);

}
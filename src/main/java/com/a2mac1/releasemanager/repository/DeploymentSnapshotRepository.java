package com.a2mac1.releasemanager.repository;

import com.a2mac1.releasemanager.entity.DeploymentSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeploymentSnapshotRepository extends JpaRepository<DeploymentSnapshot, UUID> {

    List<DeploymentSnapshot> findBySystemVersionOrderByServiceNameAsc(int systemVersion);

}

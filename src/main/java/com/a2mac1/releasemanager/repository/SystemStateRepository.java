package com.a2mac1.releasemanager.repository;


import com.a2mac1.releasemanager.entity.SystemState;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SystemStateRepository extends JpaRepository<SystemState, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<SystemState> findFirstByOrderByCurrentVersionDescCreatedAtDesc();

    Optional<SystemState> findTopByOrderByCurrentVersionDescCreatedAtDesc();
}

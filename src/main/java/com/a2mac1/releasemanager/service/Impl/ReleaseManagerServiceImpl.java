package com.a2mac1.releasemanager.service.Impl;

import com.a2mac1.releasemanager.dto.DeployRequest;
import com.a2mac1.releasemanager.dto.ServiceResponse;
import com.a2mac1.releasemanager.entity.CurrentDeployment;
import com.a2mac1.releasemanager.entity.DeploymentSnapshot;
import com.a2mac1.releasemanager.entity.SystemState;
import com.a2mac1.releasemanager.exception.SystemVersionNotFoundException;
import com.a2mac1.releasemanager.repository.CurrentDeploymentRepository;
import com.a2mac1.releasemanager.repository.DeploymentSnapshotRepository;
import com.a2mac1.releasemanager.repository.SystemStateRepository;
import com.a2mac1.releasemanager.service.ReleaseManagerService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReleaseManagerServiceImpl implements ReleaseManagerService {


    private final SystemStateRepository systemStateRepository;
    private final CurrentDeploymentRepository deploymentRepository;
    private final DeploymentSnapshotRepository snapshotRepository;


    @Override
    @Transactional
    public int deploy(DeployRequest request) {

        SystemState systemState = systemStateRepository.findFirstByOrderByCurrentVersionDescCreatedAtDesc()
                .orElseThrow(() -> new IllegalStateException("System state is not initialized"));

        CurrentDeployment deployment = deploymentRepository.findByServiceName(request.name())
                .orElse(null);

        // No new SystemVersion if the service version is unchanged
        if (deployment != null && deployment.hasVersion(request.version())) {
            return systemState.getCurrentVersion();
        }

        if (deployment == null) {
            deployment = new CurrentDeployment(request.name(), request.version());
        } else {
            deployment.updateVersion(request.version());
        }

        deploymentRepository.saveAndFlush(deployment);

        int newSystemVersion = systemState.incrementVersion();

        createSnapshot(newSystemVersion);

        return newSystemVersion;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponse> getServices(int systemVersion) {

        SystemState systemState = systemStateRepository.findTopByOrderByCurrentVersionDescCreatedAtDesc()
                .orElseThrow(() -> new IllegalStateException("System state is not initialized"));

        if (systemVersion < 1 || systemVersion > systemState.getCurrentVersion()) {
            throw new SystemVersionNotFoundException(systemVersion);
        }

        return snapshotRepository.findBySystemVersionOrderByServiceNameAsc(systemVersion)
                .stream()
                .map(snapshot -> new ServiceResponse(
                        snapshot.getServiceName(),
                        snapshot.getServiceVersion()
                )).toList();
    }

    private void createSnapshot(int systemVersion) {
        List<DeploymentSnapshot> snapshots = deploymentRepository.findAll()
                        .stream()
                        .map(deployment -> new DeploymentSnapshot(
                                systemVersion,
                                deployment.getServiceName(),
                                deployment.getServiceVersion()
                        )).toList();
        snapshotRepository.saveAll(snapshots);
    }
}

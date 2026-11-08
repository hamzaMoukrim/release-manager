
package com.a2mac1.releasemanager.service;

import com.a2mac1.releasemanager.dto.DeployRequest;
import com.a2mac1.releasemanager.entity.*;
import com.a2mac1.releasemanager.repository.*;
import com.a2mac1.releasemanager.service.Impl.ReleaseManagerServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReleaseManagerServiceImplTest {

    @Mock
    private SystemStateRepository systemStateRepository;

    @Mock
    private CurrentDeploymentRepository deploymentRepository;

    @Mock
    private DeploymentSnapshotRepository snapshotRepository;

    @Mock
    private SystemState systemState;

    @InjectMocks
    private ReleaseManagerServiceImpl service;

    @Test
    void shouldKeepSystemVersionWhenServiceHasNotChanged() {
        var request = new DeployRequest("Service A", 1);
        var deployment = new CurrentDeployment("Service A", 1);

        when(systemStateRepository.findFirstByOrderByCurrentVersionDescCreatedAtDesc())
                .thenReturn(Optional.of(systemState));
        when(deploymentRepository.findByServiceName("Service A"))
                .thenReturn(Optional.of(deployment));
        when(systemState.getCurrentVersion()).thenReturn(3);

        int version = service.deploy(request);

        assertThat(version).isEqualTo(3);
        verify(systemState, never()).incrementVersion();
        verifyNoInteractions(snapshotRepository);
    }

    @Test
    void shouldIncrementSystemVersionWhenServiceChanges() {
        var serviceA = new CurrentDeployment("Service A", 1);
        var serviceB = new CurrentDeployment("Service B", 1);

        when(systemStateRepository.findFirstByOrderByCurrentVersionDescCreatedAtDesc())
                .thenReturn(Optional.of(systemState));
        when(deploymentRepository.findByServiceName("Service A"))
                .thenReturn(Optional.of(serviceA));
        when(systemState.incrementVersion()).thenReturn(3);
        when(deploymentRepository.findAll())
                .thenReturn(List.of(serviceA, serviceB));

        int version = service.deploy(new DeployRequest("Service A", 2));

        assertThat(version).isEqualTo(3);
        assertThat(serviceA.getServiceVersion()).isEqualTo(2);

        verify(deploymentRepository).saveAndFlush(serviceA);
        verify(snapshotRepository).saveAll(anyList());
    }

    @Test
    void shouldFailWhenSystemStateIsMissing() {
        when(systemStateRepository.findFirstByOrderByCurrentVersionDescCreatedAtDesc())
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.deploy(new DeployRequest("Service A", 1))
        ).isInstanceOf(IllegalStateException.class);
    }
}

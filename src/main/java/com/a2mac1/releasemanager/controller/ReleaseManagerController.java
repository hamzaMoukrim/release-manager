
package com.a2mac1.releasemanager.controller;

import com.a2mac1.releasemanager.dto.DeployRequest;
import com.a2mac1.releasemanager.dto.ServiceResponse;
import com.a2mac1.releasemanager.service.ReleaseManagerService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReleaseManagerController {

    private final ReleaseManagerService releaseManagerService;

    @PostMapping("/deploy")
    public int deploy(@Valid @RequestBody DeployRequest request) {
        return releaseManagerService.deploy(request);
    }

    @GetMapping("/services")
    public List<ServiceResponse> getServices(@RequestParam @Positive int systemVersion) {
        return releaseManagerService.getServices(systemVersion);
    }
}

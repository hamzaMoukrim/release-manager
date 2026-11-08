package com.a2mac1.releasemanager.service;

import com.a2mac1.releasemanager.dto.DeployRequest;
import com.a2mac1.releasemanager.dto.ServiceResponse;

import java.util.List;

public interface ReleaseManagerService {

    int deploy(DeployRequest request);

    List<ServiceResponse> getServices(int systemVersion);
}

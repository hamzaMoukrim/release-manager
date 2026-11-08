package com.a2mac1.releasemanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "release-manager")
public record ReleaseManagerProperties(

) {
}

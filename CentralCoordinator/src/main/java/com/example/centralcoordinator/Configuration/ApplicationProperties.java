package com.example.centralcoordinator.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * ApplicationProperties
 * This class is used to read the application properties from the application.properties file.
 */
@ConfigurationProperties(prefix = "appconfig")
public class ApplicationProperties {
    private List<Integer> nodePorts = new ArrayList<>();
    private List<String> nodeHostnames = new ArrayList<>();

    public List<Integer> getNodePorts() {
        return nodePorts;
    }

    public void setNodePorts(List<Integer> nodePorts) {
        this.nodePorts = nodePorts;
    }

    public List<String> getNodeHostnames() {
        return nodeHostnames;
    }

    public void setNodeHostnames(List<String> nodeHostnames) {
        this.nodeHostnames = nodeHostnames;
    }
}

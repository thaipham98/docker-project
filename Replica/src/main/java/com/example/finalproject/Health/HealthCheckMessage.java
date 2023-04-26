package com.example.finalproject.Health;

public class HealthCheckMessage {
    private String replicaId;
    private double cpuUsage;
    private double memoryUsage;
    private double responseTime;

    public HealthCheckMessage() {
        // Default constructor required by Kafka
    }

    public HealthCheckMessage(String replicaId, double cpuUsage, double memoryUsage, double responseTime) {
        this.replicaId = replicaId;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.responseTime = responseTime;
    }

    public String getReplicaId() {
        return replicaId;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public double getMemoryUsage() {
        return memoryUsage;
    }

    public double getResponseTime() {
        return responseTime;
    }

    public void setReplicaId(String replicaId) {
        this.replicaId = replicaId;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public void setMemoryUsage(double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public String toString() {
        return "Replica " + replicaId + " health status: " +
                "CPU usage = " + cpuUsage +
                ", memory usage = " + memoryUsage +
                ", response time = " + responseTime;
    }
}

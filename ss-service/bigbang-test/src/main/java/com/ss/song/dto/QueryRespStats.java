package com.ss.song.dto;

/**
 * author shangsong 2023/4/23
 */
public class QueryRespStats {
    private String state;

    private String queued;

    private String scheduled;

    private Object nodes;

    private Object totalSplits;

    private Object queuedSplits;

    private Object runningSplits;

    private Object completedSplits;

    private Object cpuTimeMillis;

    private Object wallTimeMillis;

    private Object queuedTimeMillis;

    private Object elapsedTimeMillis;

    private Object processedRows;

    private Object processedBytes0;

    private Object physicalInputBytes;

    private Object peakMemoryBytes;

    private Object spilledBytes;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getQueued() {
        return queued;
    }

    public void setQueued(String queued) {
        this.queued = queued;
    }

    public String getScheduled() {
        return scheduled;
    }

    public void setScheduled(String scheduled) {
        this.scheduled = scheduled;
    }

    public Object getNodes() {
        return nodes;
    }

    public void setNodes(Object nodes) {
        this.nodes = nodes;
    }

    public Object getTotalSplits() {
        return totalSplits;
    }

    public void setTotalSplits(Object totalSplits) {
        this.totalSplits = totalSplits;
    }

    public Object getQueuedSplits() {
        return queuedSplits;
    }

    public void setQueuedSplits(Object queuedSplits) {
        this.queuedSplits = queuedSplits;
    }

    public Object getRunningSplits() {
        return runningSplits;
    }

    public void setRunningSplits(Object runningSplits) {
        this.runningSplits = runningSplits;
    }

    public Object getCompletedSplits() {
        return completedSplits;
    }

    public void setCompletedSplits(Object completedSplits) {
        this.completedSplits = completedSplits;
    }

    public Object getCpuTimeMillis() {
        return cpuTimeMillis;
    }

    public void setCpuTimeMillis(Object cpuTimeMillis) {
        this.cpuTimeMillis = cpuTimeMillis;
    }

    public Object getWallTimeMillis() {
        return wallTimeMillis;
    }

    public void setWallTimeMillis(Object wallTimeMillis) {
        this.wallTimeMillis = wallTimeMillis;
    }

    public Object getQueuedTimeMillis() {
        return queuedTimeMillis;
    }

    public void setQueuedTimeMillis(Object queuedTimeMillis) {
        this.queuedTimeMillis = queuedTimeMillis;
    }

    public Object getElapsedTimeMillis() {
        return elapsedTimeMillis;
    }

    public void setElapsedTimeMillis(Object elapsedTimeMillis) {
        this.elapsedTimeMillis = elapsedTimeMillis;
    }

    public Object getProcessedRows() {
        return processedRows;
    }

    public void setProcessedRows(Object processedRows) {
        this.processedRows = processedRows;
    }

    public Object getProcessedBytes0() {
        return processedBytes0;
    }

    public void setProcessedBytes0(Object processedBytes0) {
        this.processedBytes0 = processedBytes0;
    }

    public Object getPhysicalInputBytes() {
        return physicalInputBytes;
    }

    public void setPhysicalInputBytes(Object physicalInputBytes) {
        this.physicalInputBytes = physicalInputBytes;
    }

    public Object getPeakMemoryBytes() {
        return peakMemoryBytes;
    }

    public void setPeakMemoryBytes(Object peakMemoryBytes) {
        this.peakMemoryBytes = peakMemoryBytes;
    }

    public Object getSpilledBytes() {
        return spilledBytes;
    }

    public void setSpilledBytes(Object spilledBytes) {
        this.spilledBytes = spilledBytes;
    }
}

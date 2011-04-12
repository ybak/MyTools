package com.buy360.isaac.protocprofile;


/**
 * time unit: nanosecond <br>
 * size unit: byte
 * 
 * @author isaac
 * 
 */
abstract public class AbstractProfiler {
    // test data
    public static final String NUMBER_MOBILE = "14551065703";
    public static final String NUMBER_HOME = "555-4321";
    public static final String EMAIL = "jdoe@example.com";
    public static final String NAME = "John Doe";
    public static final int ID = 1234;

    private String name;
    protected boolean equalTarget = false;

    // statistic fields
    private int countRun = 0;
    private long totalTimeBuildingInstance;
    private long totalTimeSerializingInstance;
    private long totalTimeDeserializingInstance;
    private long totalSerialiedSize;

    public AbstractProfiler(boolean equalTarget, String name) {
        this.equalTarget = equalTarget;
        this.name = name;
    }

    public void runAndReport(int count) throws Exception {
        for (int i = 0; i < count; i++) {
            run(i);
            countRun++;
        }

        report();
        destory();
    }

    private void run(int id) throws Exception {
        long timeBeforeBuildingInstance = System.nanoTime();
        Object instance = buildInstance(id);
        long timeBuildingInstance = System.nanoTime() - timeBeforeBuildingInstance;
        totalTimeBuildingInstance += timeBuildingInstance;

        long timeBeforeSerializingInstance = System.nanoTime();
        byte[] byteArray = serializeObject(instance);
        long timeSerializingInstance = System.nanoTime() - timeBeforeSerializingInstance;
        totalTimeSerializingInstance += timeSerializingInstance;

        long timeBeforeDeserializingInstance = System.nanoTime();
        deserializeObject(byteArray);
        long timeDeserializingInstance = System.nanoTime() - timeBeforeDeserializingInstance;
        totalTimeDeserializingInstance += timeDeserializingInstance;

        totalSerialiedSize += byteArray.length;
    }

    private void report() {
        System.out.println("profiler " + name + " run " + countRun + " times.");
        System.out.println("is profiler run on different target: " + equalTarget);
        System.out.println();
        System.out.println("time building " + name + " instance: " + totalTimeBuildingInstance / countRun);
        System.out.println("time serializing " + name + " instance: " + totalTimeSerializingInstance / countRun);
        System.out.println("time deserializing " + name + " instance: " + totalTimeDeserializingInstance / countRun);
        System.out.println("serialied " + name + " instance size: " + totalSerialiedSize / countRun);
        System.out.println();
        System.out.println();
    }

    abstract protected Object buildInstance(int id);

    abstract protected byte[] serializeObject(Object instance) throws Exception;

    abstract protected void deserializeObject(byte[] byteArray) throws Exception;

    protected void destory() {
    }

}
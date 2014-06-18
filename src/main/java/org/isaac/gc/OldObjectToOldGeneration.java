package org.isaac.gc;

/**
 * vm args: -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1 -XX:+PrintTenuringDistribution
 */
public class OldObjectToOldGeneration {
    private static final int _1MB = 1024 * 1024;

    public static void testAllocation() {
        byte[] allocation1,allocation2,allocation3;
        allocation1 = new byte[_1MB / 4];
        
        allocation2 = new byte[_1MB * 4];

        allocation3 = new byte[_1MB * 4];
        allocation3 = null;
        allocation3 = new byte[_1MB * 4];
    }

    public static void main(String[] args) {
        testAllocation();
    }
}

package com.buy360.isaac.protocprofile;

public class ProfilerRunner {
    private static final int COUNT_BIG_PROFILE = 1000;

    public static void main(String[] args) throws Exception {
        // Protocol Buffers
        // warming up the profiler
        ProtocbufferProfiler pbWarmupProfiler = new ProtocbufferProfiler(true);
        pbWarmupProfiler.runAndReport(1);

        ProtocbufferProfiler pbEqualTargetProfiler = new ProtocbufferProfiler(true);
        pbEqualTargetProfiler.runAndReport(COUNT_BIG_PROFILE);

        ProtocbufferProfiler pbDifferentTargetProfiler = new ProtocbufferProfiler(false);
        pbDifferentTargetProfiler.runAndReport(COUNT_BIG_PROFILE);

        // thrift
        ThriftProfiler thriftWarmupProfiler = new ThriftProfiler(true);
        thriftWarmupProfiler.runAndReport(1);

        ThriftProfiler thriftEqualTargetProfiler = new ThriftProfiler(true);
        thriftEqualTargetProfiler.runAndReport(COUNT_BIG_PROFILE);

        ThriftProfiler thriftDifferentTargetProfiler = new ThriftProfiler(false);
        thriftDifferentTargetProfiler.runAndReport(COUNT_BIG_PROFILE);

        // ice
        IceProfiler iceWarmupProfiler = new IceProfiler(true);
        iceWarmupProfiler.runAndReport(1);

        IceProfiler iceEqualTargetProfiler = new IceProfiler(true);
        iceEqualTargetProfiler.runAndReport(COUNT_BIG_PROFILE);
        
        IceProfiler iceDifferentTargetProfiler = new IceProfiler(false);
        iceDifferentTargetProfiler.runAndReport(COUNT_BIG_PROFILE);
    }

}

package com.example.device.api;

public class DeviceInfo
{
    public static final int MAX_THREADS_PER_BLOCK = 1024;
    public static final int MAX_THREADS_PER_WARP = 32;
    // TODO: add more constants
    
    private final int maxWarpsPerSM;
    private final int maxThreadsPerSM;
    private final int maxBlocksPerSM;
    private final int maxRegistersPerSM;
    private final int maxRegistersPerBlock;
    private final int maxRegistersPerThread;
    private final int sharedMemPerSM;
    private final int sharedMemPerBlock;
    
    public DeviceInfo(int maxWarpsPerSM, int maxThreadsPerSM, int maxBlocksPerSM, int maxRegistersPerSM,
                      int maxRegistersPerBlock, int maxRegistersPerThread, int sharedMemPerSM, int sharedMemPerBlock)
    {
        this.maxWarpsPerSM = maxWarpsPerSM;
        this.maxThreadsPerSM = maxThreadsPerSM;
        this.maxBlocksPerSM = maxBlocksPerSM;
        this.maxRegistersPerSM = maxRegistersPerSM;
        this.maxRegistersPerBlock = maxRegistersPerBlock;
        this.maxRegistersPerThread = maxRegistersPerThread;
        this.sharedMemPerSM = sharedMemPerSM;
        this.sharedMemPerBlock = sharedMemPerBlock;
    }

    public int getMaxWarpsPerSM()
    {
        return maxWarpsPerSM;
    }

    public int getMaxThreadsPerSM()
    {
        return maxThreadsPerSM;
    }

    public int getMaxBlocksPerSM()
    {
        return maxBlocksPerSM;
    }

    public int getMaxRegistersPerSM()
    {
        return maxRegistersPerSM;
    }

    public int getMaxRegistersPerBlock()
    {
        return maxRegistersPerBlock;
    }

    public int getMaxRegistersPerThread()
    {
        return maxRegistersPerThread;
    }

    public int getSharedMemPerSM()
    {
        return sharedMemPerSM;
    }

    public int getSharedMemPerBlock()
    {
        return sharedMemPerBlock;
    }
}

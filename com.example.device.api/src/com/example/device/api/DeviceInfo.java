package com.example.device.api;

public class DeviceInfo
{
    public static final int MAX_THREADS_PER_BLOCK = 1024;
    public static final int MAX_THREADS_PER_WARP = 32;
    public static final int MAX_DIM_OF_GRID_OF_THREAD_BLOCKS = 3;
    public static final int MAX_X_DIM_OF_GRID_OF_THREAD_BLOCKS = 2^31-1;
    public static final int MAX_Y_OR_Z_DIM_OF_GRID_OF_THREAD_BLOCKS = 65535;
    public static final int MAX_DIM_OF_THREAD_BLOCK = 3;
    public static final int MAX_X_OR_Y_DIM_OF_BLOCK = 1024;
    public static final int MAX_Z_DIM_OF_BLOCK = 64;
    public static final int LOCAL_MEM_PER_THREAD = 512*1024;
    public static final int MAX_WARPS_PER_SM = 64;
    public static final int MAX_THREADS_PER_SM = 2048;
    
    private int maxBlocksPerSM;
    private int maxRegistersPerSM;
    private int maxRegistersPerBlock;
    private int maxRegistersPerThread;
    private int sharedMemPerSM;
    private int sharedMemPerBlock;
    private int maxGridsPerDevice;
    
    public DeviceInfo(int maxBlocksPerSM, int maxRegistersPerSM,
                      int maxRegistersPerBlock, int maxRegistersPerThread, int sharedMemPerSM, int sharedMemPerBlock, int maxGridsPerDevice)
    {
        this.maxBlocksPerSM = maxBlocksPerSM;
        this.maxRegistersPerSM = maxRegistersPerSM;
        this.maxRegistersPerBlock = maxRegistersPerBlock;
        this.maxRegistersPerThread = maxRegistersPerThread;
        this.sharedMemPerSM = sharedMemPerSM;
        this.sharedMemPerBlock = sharedMemPerBlock;
        this.maxGridsPerDevice = maxGridsPerDevice;
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
    
    public int getMaxGridsPerDevice()
    {
    	return maxGridsPerDevice;
    }
}

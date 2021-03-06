package com.example.kernel.api;

import com.example.device.api.ComputeCapability;

public interface IKernelAnalyzer
{
    AnalysisReport analyzeKernel(ComputeCapability computeCapability, int gridDimX, int gridDimY, int blockDimX, int blockDimY,
                          int sharedMemPerBlock, int numRegistersPerThread);
}

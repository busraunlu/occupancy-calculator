package com.example.kernel.analyzer;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.example.device.api.ComputeCapability;
import com.example.device.api.IDeviceInfo;
import com.example.device.info.DeviceInfoProvider;
import com.example.kernel.api.IKernelAnalyzer;

@Component(name = "KernelAnalyzer", immediate = true, service = IKernelAnalyzer.class)
public class KernelAnalyzer implements IKernelAnalyzer{
	
	private IDeviceInfo device;

	@Override
	public boolean analyzeKernel(ComputeCapability computeCapability, int gridDimX, int gridDimY, int blockDimX, int blockDimY,
            int sharedMemPerBlock, int numRegistersPerThread) {
		
		
		return false;
	}
	
	@Reference(name = "KernelAnalyzer")
	public synchronized void bindKernelAnalyzer(IDeviceInfo device) {
        System.out.println("Service was set.");
        this.device = device;
    
    }

    // Method will be used by DS to unset the quote service
    public synchronized void unbindKernelAnalyzer(IDeviceInfo device) {
        System.out.println("Service was unset.");
        if (this.device == device) {
            this.device = null;
        }
    }
	
}

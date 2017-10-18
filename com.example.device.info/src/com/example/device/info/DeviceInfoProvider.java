package com.example.device.info;

import com.example.device.api.ComputeCapability;
import com.example.device.api.DeviceInfo;
import com.example.device.api.IDeviceInfo;

@Component(name = "device.info.provider", immediate = true, service = IDeviceInfo.class)
public class DeviceInfoProvider implements IDeviceInfo
{
	private static final int CONSTANT = 1024;
	
	@Override
	public DeviceInfo getDeviceInfo(ComputeCapability computeCapability) {
		int maxBlocksPerSM = 0;
		int maxRegistersPerSM = 0;
		int maxRegistersPerBlock = 0;
		int maxRegistersPerThread = 0;
		int sharedMemPerSM = 0;
		int sharedMemPerBlock = 0;
		int maxGridsPerDevice = 0;
		
		// TODO Auto-generated method stub
		if(computeCapability == ComputeCapability.CC_30 || computeCapability == ComputeCapability.CC_32 || computeCapability == ComputeCapability.CC_35 || computeCapability == ComputeCapability.CC_37)
			maxBlocksPerSM = 16;
		else 
			maxBlocksPerSM = 32;
		
		
		if(computeCapability == ComputeCapability.CC_30 || computeCapability == ComputeCapability.CC_32 || computeCapability == ComputeCapability.CC_35)
			maxRegistersPerSM = 64*CONSTANT;
		else if(computeCapability == ComputeCapability.CC_37)
			maxRegistersPerSM = 128*CONSTANT;
		else
			maxRegistersPerSM = 64*CONSTANT;
		
		
		if(computeCapability == ComputeCapability.CC_32 || computeCapability == ComputeCapability.CC_53 || computeCapability == ComputeCapability.CC_62)
			maxRegistersPerBlock = 32*CONSTANT;
		else 
			maxRegistersPerBlock = 64*CONSTANT;
		
		
		if(computeCapability == ComputeCapability.CC_30)
			maxRegistersPerThread = 63;
		else
			maxRegistersPerThread = 255;
		
		
		if(computeCapability == ComputeCapability.CC_30 || computeCapability == ComputeCapability.CC_32 || computeCapability == ComputeCapability.CC_35)
			sharedMemPerSM = 48*CONSTANT*8;
		else if(computeCapability == ComputeCapability.CC_37)
			sharedMemPerSM = 112*CONSTANT*8;
		else if(computeCapability == ComputeCapability.CC_52 || computeCapability == ComputeCapability.CC_61 || computeCapability == ComputeCapability.CC_70)
			sharedMemPerSM = 96*CONSTANT*8;
		else 
			sharedMemPerSM = 64*CONSTANT*8;
		
		
		if(computeCapability == ComputeCapability.CC_70)
			sharedMemPerBlock = 96*(CONSTANT^19)*8;
		else
			sharedMemPerBlock = 48*CONSTANT*8;
		
		
		if(computeCapability == ComputeCapability.CC_32)
			maxGridsPerDevice = 4;
		else if(computeCapability == ComputeCapability.CC_60 || computeCapability == ComputeCapability.CC_70)
			maxGridsPerDevice = 128;
		else if(computeCapability == ComputeCapability.CC_30 || computeCapability == ComputeCapability.CC_53 || computeCapability == ComputeCapability.CC_62)
			maxGridsPerDevice = 16;
		else
			maxGridsPerDevice = 32;
	
		return null;
	}
	
}
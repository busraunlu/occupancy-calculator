package com.example.kernel.analyzer;

import org.osgi.service.component.annotations.Component;  
import org.osgi.service.component.annotations.Reference;

import com.example.device.api.ComputeCapability;
import com.example.device.api.DeviceInfo;
import com.example.device.api.IDeviceInfo;
import com.example.device.info.DeviceInfoProvider;
import com.example.kernel.api.AnalysisReport;
import com.example.kernel.api.IKernelAnalyzer;

@Component(name = "KernelAnalyzer", immediate = true, service = IKernelAnalyzer.class)
public class KernelAnalyzer implements IKernelAnalyzer{
	
	private IDeviceInfo device;
	private DeviceInfo deviceInfo;
	private AnalysisReport analysisReport;
	private StringBuilder stringBuilder;
	
	@Override
	public AnalysisReport analyzeKernel(ComputeCapability computeCapability, int gridDimX, int gridDimY, int blockDimX, int blockDimY,
            int sharedMemPerBlock, int numRegistersPerThread) {
		
		stringBuilder.setLength(0);				// this line's purpose is reseting StringBuilder
		this.analysisReport = new AnalysisReport();
		
		deviceInfo = device.getDeviceInfo(computeCapability);
		
	    boolean dimensionControl = gridDimensionXControl(gridDimX) && gridDimensionYControl(gridDimY) && numberOfGridPerDeviceControl(gridDimX, gridDimY) && blockDimensionXControl(blockDimX) && blockDimensionYControl(blockDimY);
	    boolean threadControl = numberOfThreadPerBlockControl(blockDimX,blockDimY)  && numberOfRegisterPerThreadControl(numRegistersPerThread);

	    
	    boolean nControl = true;
	
		int N = findOptimalN(blockDimX, blockDimY, numRegistersPerThread,sharedMemPerBlock);
			if(N <= 0) {
				nControl = false;
		 	}


		boolean blockControl = numberOfRegisterPerBlockControl(blockDimX,blockDimY,numRegistersPerThread) && sharedMemoryPerBlockControl(sharedMemPerBlock);
		boolean SMControl =  maxNumberOfThreadPerSMControl(N,blockDimX,blockDimY) && maxNumberOfBlockPerSMControl(N) && RegisterPerSMControl(N, numRegistersPerThread, blockDimX, blockDimY) && sharedMemoryPerSMControl(sharedMemPerBlock, N);
		
		float occupancy = occupanyCalculator(N,blockDimX,blockDimY);
		
		   
		analysisReport.setMessage(stringBuilder.toString());
		//System.out.println(analysisReport.getMessage());
		
			if(dimensionControl && blockControl && threadControl && nControl && SMControl == true) {
				analysisReport.setOccupancy(occupancy);
				return analysisReport;
				}
		System.err.println("unsuccesful");
		analysisReport.setOccupancy(0);
		return analysisReport;
	}
	
	public int findOptimalN(int blockDimX,int blockDimY,int numRegistersPerThread, int sharedMemPerBlock) {
		
		
		
		 int nRegister= findNForRegister(blockDimX,blockDimY,numRegistersPerThread);
		 int nSM = findNForSharedMemory(blockDimX, blockDimY, numRegistersPerThread, sharedMemPerBlock);
		 int N = Math.min(nRegister, nSM);
		 int threadN = findNForThread(blockDimX,blockDimY);	 
		 N = Math.min(N, threadN);
		 
		 /*		 int threadPerSM = N*blockDimX*blockDimY;
		 
		 	if(threadPerSM > DeviceInfo.MAX_THREADS_PER_SM ) {
		 		
			
		 	}*/
		 
		 return N;
		 	
	}
	
	
	public int findNForThread(int blockDimX,int blockDimY) {
		
		float numberOfWarpForBlock = (blockDimX*blockDimY) / (float)DeviceInfo.MAX_THREADS_PER_WARP;
		numberOfWarpForBlock = (float) Math.ceil(numberOfWarpForBlock);
 		
 		int threadN = (int)(DeviceInfo.MAX_WARPS_PER_SM / numberOfWarpForBlock);
 		
 		
 		
		return threadN;
	}
	
	public int findNForRegister(int blockDimX,int blockDimY,int numRegistersPerThread) {
		int registerPerWarp = numRegistersPerThread * DeviceInfo.MAX_THREADS_PER_WARP;
		float numberOfWarpForBlock = (blockDimX*blockDimY) / (float)DeviceInfo.MAX_THREADS_PER_WARP;
		numberOfWarpForBlock = (float) Math.ceil(numberOfWarpForBlock);
		
		float numberOfRegisterPerBlock = (numberOfWarpForBlock * registerPerWarp);
		int N = (int)(deviceInfo.getMaxRegistersPerSM()/numberOfRegisterPerBlock); // N is the number of block. 
		if(N>deviceInfo.getMaxBlocksPerSM()) {								//  Max Register per SM is divided into max register per block.
			 N=deviceInfo.getMaxBlocksPerSM();
			}
																
			if(N == 0) { 
/*				 System.err.println("Number of register per Multiprocessor("+N*numberOfRegisterPerBlock+") is bigger than maximum limit("+deviceInfo.getMaxRegistersPerSM()+")");
				 throw new IllegalArgumentException("Number of register per Multiprocessor("+N*numberOfRegisterPerBlock+") is bigger than maximum limit("+deviceInfo.getMaxRegistersPerSM()+")");
*/			}
			
		return N;
	}
	
	public int findNForSharedMemory(int blockDimX,int blockDimY,int numRegistersPerThread,int sharedMemPerBlock) {
			int N = deviceInfo.getSharedMemPerSM()/sharedMemPerBlock; // N is the number of block. 
																//  Max shared memory per SM is divided into max shared memory per block.
			if(N>deviceInfo.getMaxBlocksPerSM()) {
				N=deviceInfo.getMaxBlocksPerSM();
			}
			if(N == 0) { 
			//	throw new IllegalArgumentException("Shared Memory per multiprocessor("+N+") is bigger than maximum limit("+deviceInfo.getSharedMemPerSM()+")");
			}		
			
				return N;
	}
	
	
	
	public float occupanyCalculator(int N, int blockDimX, int blockDimY){
		
		float numberOfWarpSM = (blockDimX*blockDimY) / (float)DeviceInfo.MAX_THREADS_PER_WARP;
		numberOfWarpSM = (float) Math.ceil(numberOfWarpSM);
		float occupancy = N*numberOfWarpSM*100/(float)DeviceInfo.MAX_WARPS_PER_SM;

		return occupancy;
	}
	
	
	public boolean maxNumberOfThreadPerSMControl(int N,int blockDimX,int blockDimY) {
		
		int threadPerSM = N*blockDimX*blockDimY;
		if(threadPerSM <= DeviceInfo.MAX_THREADS_PER_SM && threadPerSM > 0) {
			this.stringBuilder.append("\nNumber of resident thread per Multiprocessor("+threadPerSM+") is smaller than maximum limit("+DeviceInfo.MAX_THREADS_PER_SM+")");
			return true;
		}
		throw new IllegalArgumentException("Number of resident thread per Multiprocessor("+threadPerSM+") is bigger than maximum limit("+DeviceInfo.MAX_THREADS_PER_SM+")");

		
}
	
	public boolean maxNumberOfBlockPerSMControl(int N) {
						
			if(N <= deviceInfo.getMaxBlocksPerSM() && N > 0) {
				this.stringBuilder.append("\nNumber of resident blocks per Multiprocessor("+N+") is smaller than maximum limit("+deviceInfo.getMaxBlocksPerSM()+")");
				return true;
			}
			throw new IllegalArgumentException("Number of resident blocks per Multiprocessor("+N+") is bigger than maximum limit("+deviceInfo.getMaxBlocksPerSM()+")");

			
	}
	

	
	public boolean RegisterPerSMControl(int N,int numRegistersPerThread,int blockDimX,int blockDimY) {
			
		int numRegisterPerSM = N*numRegistersPerThread*blockDimX*blockDimY;
		if(numRegisterPerSM > deviceInfo.getMaxRegistersPerSM() ) {
			 throw new IllegalArgumentException("Number of register per Multiprocessor("+numRegisterPerSM+") is bigger than maximum limit("+deviceInfo.getMaxRegistersPerSM()+")");

		}
			this.stringBuilder.append("\nNumber of register per Multiprocessor("+numRegisterPerSM+") is smaller than maximum limit("+deviceInfo.getMaxRegistersPerSM()+")");

				return true;
	}
	
	
	public boolean sharedMemoryPerSMControl(int sharedMemPerBlock, int N) {
		
				if(N*sharedMemPerBlock > deviceInfo.getSharedMemPerSM()) {
					throw new IllegalArgumentException("Shared Memory per multiprocessor("+N*sharedMemPerBlock+") is bigger than maximum limit("+deviceInfo.getSharedMemPerSM()+")");
				}
		
			this.stringBuilder.append("\nShared Memory per multiprocessor("+N*sharedMemPerBlock+") is smaller than maximum limit("+deviceInfo.getSharedMemPerSM()+")");

			return true;
	}
	
	public boolean sharedMemoryPerBlockControl(int sharedMemPerBlock) {
		
		if(sharedMemPerBlock <= deviceInfo.getSharedMemPerBlock() && sharedMemPerBlock >= 0) {
			this.stringBuilder.append("\nShared Memory per block("+sharedMemPerBlock+") is smaller than maximum limit("+deviceInfo.getSharedMemPerBlock()+")");
			return true;
		}
		throw new IllegalArgumentException("Shared Memory per block("+sharedMemPerBlock+") is bigger than maximum limit("+deviceInfo.getSharedMemPerBlock()+")");

	}
	
	public boolean numberOfRegisterPerBlockControl(int blockDimX,int blockDimY, int numRegistersPerThread) {
		int numberOftread = blockDimX * blockDimY;
		int numberOfRegisterPerBlock = (numberOftread * numRegistersPerThread);
		
			if( numberOfRegisterPerBlock <= deviceInfo.getMaxRegistersPerBlock() && numberOfRegisterPerBlock >= 0) {
				this.stringBuilder.append("\nNumber of register per block("+numberOfRegisterPerBlock+") is smaller than maximum limit("+deviceInfo.getMaxRegistersPerBlock()+")");
				
				return true;
			}
			throw new IllegalArgumentException("Number of register per block("+numberOfRegisterPerBlock+") is bigger than maximum limit("+deviceInfo.getMaxRegistersPerBlock()+")");

	}
	

	
	public boolean numberOfRegisterPerThreadControl(int numRegistersPerThread) {
		
		if(numRegistersPerThread <= deviceInfo.getMaxRegistersPerThread() && numRegistersPerThread >= 0) {
			this.stringBuilder.append("\nNumber of register per thread("+numRegistersPerThread+") is smaller than maximum limit("+deviceInfo.getMaxRegistersPerThread()+")");
			return true;
		}
		throw new IllegalArgumentException("Number of register per thread("+numRegistersPerThread+") is bigger than maximum limit("+deviceInfo.getMaxRegistersPerThread()+")");

	}
	
	public boolean numberOfThreadPerBlockControl(int blockDimX,int blockDimY) {
		
		if(0 <= (blockDimX * blockDimY) && (blockDimX * blockDimY) <= DeviceInfo.MAX_THREADS_PER_BLOCK) {
			this.stringBuilder.append("\nNumber of thread per block("+(blockDimX * blockDimY)+") is smaller than maximum limit("+DeviceInfo.MAX_THREADS_PER_BLOCK+")");
			return true;
		}
		throw new IllegalArgumentException("\nNumber of thread per block("+(blockDimX * blockDimY)+") is bigger than maximum limit("+DeviceInfo.MAX_THREADS_PER_BLOCK+")");

	}
	
	public boolean blockDimensionYControl(int blockDimY) {
		
		if(blockDimY <= DeviceInfo.MAX_X_OR_Y_DIM_OF_BLOCK && blockDimY >= 0) {
			this.stringBuilder.append("\nY dimension of block("+blockDimY+") is smaller than maximum limit("+DeviceInfo.MAX_X_OR_Y_DIM_OF_BLOCK+")");
			return true;
		}
		throw new IllegalArgumentException("Y dimension of block("+blockDimY+") is bigger than maximum limit("+DeviceInfo.MAX_X_OR_Y_DIM_OF_BLOCK+")");
	
	}
	
	public boolean blockDimensionXControl(int blockDimX) {
		
		if(blockDimX <= DeviceInfo.MAX_X_OR_Y_DIM_OF_BLOCK && blockDimX >= 0) {
			this.stringBuilder.append("\nX dimension of block("+blockDimX+") is smaller than maximum limit("+DeviceInfo.MAX_X_OR_Y_DIM_OF_BLOCK+")");
			
			return true;
		}
		throw new IllegalArgumentException("\nX dimension of block("+blockDimX+") is bigger than maximum limit("+DeviceInfo.MAX_X_OR_Y_DIM_OF_BLOCK+")");

	}
	
	public boolean gridDimensionXControl(int gridDimX) {
		
		
		if(gridDimX <= DeviceInfo.MAX_X_DIM_OF_GRID_OF_THREAD_BLOCKS && gridDimX >= 0) {
			this.stringBuilder.append("\nX dimension of grid("+gridDimX+") is smaller than maximum limit("+DeviceInfo.MAX_X_DIM_OF_GRID_OF_THREAD_BLOCKS+")");

			return true;
		}
		throw new IllegalArgumentException("X dimension of grid("+gridDimX+") is bigger than maximum limit("+DeviceInfo.MAX_X_DIM_OF_GRID_OF_THREAD_BLOCKS+")");

	}
	
	public boolean gridDimensionYControl(int gridDimY){
	
			if(gridDimY <= DeviceInfo.MAX_Y_OR_Z_DIM_OF_GRID_OF_THREAD_BLOCKS && gridDimY >= 0) {
				this.stringBuilder.append("\nY dimension of grid("+gridDimY+") is smaller than maximum limit("+DeviceInfo.MAX_Y_OR_Z_DIM_OF_GRID_OF_THREAD_BLOCKS+")");
				return true;
			}
		
			
			
			throw new IllegalArgumentException("Y dimension of grid("+gridDimY+") is bigger than maximum limit("+DeviceInfo.MAX_Y_OR_Z_DIM_OF_GRID_OF_THREAD_BLOCKS+")");
	}
	
	public boolean numberOfGridPerDeviceControl(int gridDimX,int gridDimY) {
		
		if(0 <= (gridDimX * gridDimY) && (gridDimX * gridDimY) <= deviceInfo.getMaxGridsPerDevice()) {
			this.stringBuilder.append("\nNumber of resident grids per device("+(gridDimX * gridDimY)+") is bigger than maximum limit("+deviceInfo.getMaxGridsPerDevice()+")");
			return true;
		}
		
		throw new IllegalArgumentException("Number of resident grids per device("+(gridDimX * gridDimY)+") is bigger than maximum limit("+deviceInfo.getMaxGridsPerDevice()+")");

	}
	
	
	@Reference(name = "KernelAnalyzer")
	public synchronized void bindKernelAnalyzer(IDeviceInfo device) {
		this.stringBuilder = new StringBuilder();
		this.stringBuilder.append("Service was set.");
        this.device = device;
        //analyzeKernel(ComputeCapability.CC_53,5,2,15,4,10,100);
        //System.out.println(analysisReport.getOccupancy());
      //  analyzeKernel(computeCapability, gridDimX, gridDimY, blockDimX, blockDimY, sharedMemPerBlock, numRegistersPerThread)
    }

    // Method will be used by DS to unset the IdeviceInfo service
    public synchronized void unbindKernelAnalyzer(IDeviceInfo device) {
    	this.stringBuilder.append("\nService was unset.");
        	if (this.device == device) {
        		this.device = null;
        	}
    }
	
}

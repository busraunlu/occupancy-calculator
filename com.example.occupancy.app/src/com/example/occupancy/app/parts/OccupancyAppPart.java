package com.example.occupancy.app.parts;

import javax.annotation.PostConstruct;  
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.example.kernel.analyzer.*;
import com.example.kernel.api.*;
import com.example.device.api.ComputeCapability;
import com.example.device.api.IDeviceInfo;

public class OccupancyAppPart {
	private Text blockDimX;
	private Text gridDimY;
	private Text blockDimY;
	private Text gridDimX;
	private Text sharedMemoryPerBlock;
	private Text registersPerThread;
	
	@Inject private IKernelAnalyzer ikernelAnalyzer;
	
	
	@PostConstruct
	public void createControls(Composite parent) {
		
		
		GridLayout gl_parent = new GridLayout(4, false);
		parent.setLayout(gl_parent);
		
		Label lblGriddimx = new Label(parent, SWT.NONE);
		GridData gd_lblGriddimx = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblGriddimx.widthHint = 63;
		lblGriddimx.setLayoutData(gd_lblGriddimx);
		lblGriddimx.setText("gridDim.x :");
		
		gridDimX = new Text(parent, SWT.BORDER);
		gridDimX.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(parent, SWT.NONE);
		lblNewLabel_1.setText("blockDim.x :");
		
		blockDimX = new Text(parent, SWT.BORDER);
		blockDimX.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setText("gridDim.y :");
		
		gridDimY = new Text(parent, SWT.BORDER);
		gridDimY.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblBlockdimy = new Label(parent, SWT.NONE);
		lblBlockdimy.setText("blockDim.y :");
		
		blockDimY = new Text(parent, SWT.BORDER);
		blockDimY.setText("");
		blockDimY.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblcomputeCapability = new Label(parent, SWT.NONE);
		lblcomputeCapability.setText("Compute Capability :");
		
		Combo combo = new Combo(parent, SWT.NONE);
		combo.setItems(new String[] {"3.0", "3.2", "3.5", "3.7", "5.0", "5.2", "5.3", "6.0", "6.1", "6.2", "7.0"});
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo.widthHint = 227;
		combo.setLayoutData(gd_combo);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		Label lblNewLabel_2 = new Label(parent, SWT.NONE);
		lblNewLabel_2.setText("Shared Memory Per Block :");
		
		sharedMemoryPerBlock = new Text(parent, SWT.BORDER);
		sharedMemoryPerBlock.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_3 = new Label(parent, SWT.NONE);
		lblNewLabel_3.setText("bytes");
		new Label(parent, SWT.NONE);
		
		Label lblNewLabel_4 = new Label(parent, SWT.NONE);
		lblNewLabel_4.setText("Registers Per Thread (bytes) :");
		
		registersPerThread = new Text(parent, SWT.BORDER);
		registersPerThread.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
			
		Group group = new Group(parent, SWT.NONE);
		GridData gd_group = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_group.heightHint = 331;
		gd_group.widthHint = 560;
		group.setLayoutData(gd_group);
		group.setText("<output>");
		
		Label lblNewLabel_output = new Label(group, SWT.NONE);
		lblNewLabel_output.setBounds(10, 21, 500, 318);
//	    System.out.println(this.getClass().getSimpleName()
//	    + " @PostConstruct method called.");
		
		Label lblOccupancyRate = new Label(parent, SWT.NONE);
		lblOccupancyRate.setText("Occupancy Rate :");
	
		Label lblNewLabel_occupancyRate = new Label(parent, SWT.NONE);
		lblNewLabel_occupancyRate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(parent, SWT.NONE);
		
		Button btnNewButton = new Button(parent, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			String str = gridDimX.getText();
			int griddimx = 0;
			String str1 = gridDimY.getText();
			int griddimy = 0;
			String str2 = blockDimX.getText();
			int blockdimx = 0;
			String str3 = blockDimY.getText();
			int blockdimy = 0;
			String str4 = sharedMemoryPerBlock.getText();
			int sharedmem = 0;
			String str5 = registersPerThread.getText();
			int registersperthread = 0;
			int comboBoxIndex = combo.getSelectionIndex();
			try
			{
				griddimx =Integer.parseInt(str);
				griddimy =Integer.parseInt(str1);
				blockdimx =Integer.parseInt(str2);
				blockdimy =Integer.parseInt(str3);
				sharedmem =Integer.parseInt(str4);
				registersperthread =Integer.parseInt(str5);
			}
			catch (Exception griddimyException)
			
			{
				lblNewLabel_output.setText("Empty field is not accepted!");
				return;
				
			}
			
			
			if(combo.getSelectionIndex() == -1) {
				lblNewLabel_output.setText("Empty Compute Capability field is not accepted!");
				return;
			}
				
			ComputeCapability computeCapability = matchingComputeCapability(comboBoxIndex);
			
			
			try
			{
				
				AnalysisReport analysisReport = ikernelAnalyzer.analyzeKernel(computeCapability, griddimx, griddimy, blockdimx, blockdimy, sharedmem, registersperthread);	
				lblNewLabel_output.setText(analysisReport.getMessage());
				String occupancy = Float.toString(analysisReport.getOccupancy());
				lblNewLabel_occupancyRate.setText(occupancy);
				
			}
			catch (Exception ikernelException)
			{
				lblNewLabel_output.setText(ikernelException.getMessage());
				// print output on GUI
			}
			
		}});
		btnNewButton.setText("Calculate Occupancy");
		
		/*Group group = new Group(parent, SWT.NONE);
		GridData gd_group = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_group.widthHint = 432;
		group.setLayoutData(gd_group);
		group.setText("<output>");
		
		Label lblNewLabel_output = new Label(group, SWT.NONE);
		lblNewLabel_output.setBounds(10, 24, 178, 82);
	    System.out.println(this.getClass().getSimpleName()
	    + " @PostConstruct method called.");*/
	}
	
	public ComputeCapability matchingComputeCapability(int comboBoxIndex) {
		ComputeCapability c = null;
		
		if(comboBoxIndex == 0){
			 c = ComputeCapability.CC_30;
		}
		else if(comboBoxIndex == 1){
			 c = ComputeCapability.CC_32;
		}
		else if(comboBoxIndex == 2){
			 c = ComputeCapability.CC_35;
		}
		else if(comboBoxIndex == 3){
			 c = ComputeCapability.CC_37;
		}
		else if(comboBoxIndex == 4){
			 c = ComputeCapability.CC_50;
		}
		else if(comboBoxIndex == 5){
			 c = ComputeCapability.CC_52;
		}
		else if(comboBoxIndex == 6){
			 c = ComputeCapability.CC_53;
		}
		else if(comboBoxIndex == 7){
			 c = ComputeCapability.CC_60;
		}
		else if(comboBoxIndex == 8){
			 c = ComputeCapability.CC_61;
		}
		else if(comboBoxIndex == 9){
			 c = ComputeCapability.CC_62;
		}
		else if(comboBoxIndex == 10){
			 c = ComputeCapability.CC_70;
		}
		return c;
	}					// end of matchingComputeCapability function
}

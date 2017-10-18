package com.example.device.info;

import com.example.device.api.IDeviceInfo;

public @interface Component {

	boolean immediate();

	Class<IDeviceInfo> service();

	String name();

}

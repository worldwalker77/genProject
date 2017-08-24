package com.zhaogang.dragonstrike.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.commons.collections.CollectionUtils;

public class CommonUtil {
	
	public static String getCurServerAddress(){
		String address = "";
		String ip = "";
		String port = "";
		try {
			InetAddress inet = InetAddress.getLocalHost();
			ip = inet.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		MBeanServer mBeanServer = null;
		List<MBeanServer> mBeanServers = MBeanServerFactory.findMBeanServer(null);
		if (CollectionUtils.isNotEmpty(mBeanServers)) {
			mBeanServer = mBeanServers.get(0);
		}
		Set objectNames = null;
		try {
			//[Catalina:type=Connector,port=8080, Catalina:type=Connector,port=8010]
			ObjectName on = new ObjectName(port);
			objectNames = mBeanServer.queryNames(new ObjectName("Catalina:type=Connector,*"), null);
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		}
		if (CollectionUtils.isEmpty(objectNames)) {
			System.out.println("没有发现JVM中关联的MBeanServer : "+ mBeanServer.getDefaultDomain() + " 中的对象名称.");
		}
		for (Object objectName : objectNames) {
			String protocol = "";
			try {
				protocol = (String) mBeanServer.getAttribute((ObjectName)objectName, "protocol");
			} catch (AttributeNotFoundException e) {
				e.printStackTrace();
			} catch (InstanceNotFoundException e) {
				e.printStackTrace();
			} catch (MBeanException e) {
				e.printStackTrace();
			} catch (ReflectionException e) {
				e.printStackTrace();
			}
			if (protocol.equals("HTTP/1.1")) {
			try {
				port = String.valueOf(mBeanServer.getAttribute((ObjectName)objectName, "port"));
			} catch (AttributeNotFoundException e) {
				e.printStackTrace();
			} catch (InstanceNotFoundException e) {
				e.printStackTrace();
			} catch (MBeanException e) {
				e.printStackTrace();
			} catch (ReflectionException e) {
				e.printStackTrace();
			}
			}
		}
		address = ip + ":" + port;
		return address;
	}
}

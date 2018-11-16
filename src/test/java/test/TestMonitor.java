package test;

import java.util.Map;

import org.hyperic.sigar.Sigar;

import com.jeeplus.modules.monitor.utils.SystemInfo;

/**
 * 测试监控
 * @author zhangsc
 * @version 2018年2月7日
 */
public class TestMonitor {
	public static void main(String[] args) {
		Map<?, ?> sigar = SystemInfo.usage(new Sigar());
		
	}
}

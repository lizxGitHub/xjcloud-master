package gov.pbc.xjcloud.common.log.event;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import gov.pbc.xjcloud.common.core.constant.SecurityConstants;
//import gov.pbc.xjcloud.provider.usercenter.api.entity.SysLog;
//import gov.pbc.xjcloud.provider.usercenter.api.feign.RemoteLogService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SysLogListener {
	private final RemoteLogService remoteLogService;

	@Async
	@Order
	@EventListener(SysLogEvent.class)
	public void saveSysLog(SysLogEvent event) {
		SysLog sysLog = event.getSysLog();
		remoteLogService.saveLog(sysLog, SecurityConstants.FROM_IN);
	}
}

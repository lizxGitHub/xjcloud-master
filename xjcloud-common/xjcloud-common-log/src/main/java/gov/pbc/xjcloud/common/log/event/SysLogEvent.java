package gov.pbc.xjcloud.common.log.event;

import gov.pbc.xjcloud.provider.usercenter.api.entity.SysLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SysLogEvent {
	private final SysLog sysLog;
}

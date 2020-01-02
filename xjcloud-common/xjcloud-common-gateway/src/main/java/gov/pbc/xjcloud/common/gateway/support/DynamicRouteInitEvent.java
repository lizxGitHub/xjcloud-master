package gov.pbc.xjcloud.common.gateway.support;

import org.springframework.context.ApplicationEvent;

public class DynamicRouteInitEvent extends ApplicationEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8750566157501208363L;

	public DynamicRouteInitEvent(Object source) {
		super(source);
	}
}

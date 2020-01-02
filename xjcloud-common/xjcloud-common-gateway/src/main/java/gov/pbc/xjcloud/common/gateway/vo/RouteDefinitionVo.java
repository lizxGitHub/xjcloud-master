package gov.pbc.xjcloud.common.gateway.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class RouteDefinitionVo extends RouteDefinition implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5421373581483201481L;
	/**
	 * 路由名称
	 */
	private String routeName;
}

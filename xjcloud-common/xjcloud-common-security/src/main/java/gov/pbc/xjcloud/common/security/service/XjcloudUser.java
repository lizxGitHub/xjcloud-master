package gov.pbc.xjcloud.common.security.service;

import java.util.Collection;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

public class XjcloudUser extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 691535632436113074L;
	/**
	 * 用户ID
	 */
	@Getter
	private Integer id;
	/**
	 * 部门ID
	 */
	@Getter
	private Integer deptId;
	
	/**
	 * 所在部门
	 */
	@Getter
	private String deptName;

	/**
	 * 租户ID
	 */
	@Getter
	private Integer tenantId;

	/**
	 * Construct the <code>User</code> with the details required by
	 * {@link DaoAuthenticationProvider}.
	 *
	 * @param id                    用户ID
	 * @param deptId                部门ID
	 * @param tenantId              租户ID
	 * @param username              the username presented to the
	 *                              <code>DaoAuthenticationProvider</code>
	 * @param password              the password that should be presented to the
	 *                              <code>DaoAuthenticationProvider</code>
	 * @param enabled               set to <code>true</code> if the user is enabled
	 * @param accountNonExpired     set to <code>true</code> if the account has not expired
	 * @param credentialsNonExpired set to <code>true</code> if the credentials have not
	 *                              expired
	 * @param accountNonLocked      set to <code>true</code> if the account is not locked
	 * @param authorities           the authorities that should be granted to the caller if they
	 *                              presented the correct username and password and the user is enabled. Not null.
	 * @throws IllegalArgumentException if a <code>null</code> value was passed either as
	 *                                  a parameter or as an element in the <code>GrantedAuthority</code> collection
	 */
	public XjcloudUser(Integer id, Integer deptId, String deptName, Integer tenantId, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = id;
		this.deptId = deptId;
		this.deptName = deptName;
		this.tenantId = tenantId;
	}
}

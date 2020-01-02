package gov.pbc.xjcloud.common.core.util;

import java.io.Serializable;

import gov.pbc.xjcloud.common.core.constant.CommonConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Builder
@ToString
@Accessors(chain = true)
@AllArgsConstructor
public class R<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private int code = CommonConstants.SUCCESS;

	@Getter
	@Setter
	private String msg = "success";


	@Getter
	@Setter
	private T data;

	public R() {
		super();
	}

	public R(T data) {
		super();
		this.data = data;
	}

	public R(T data, String msg) {
		super();
		this.data = data;
		this.msg = msg;
	}

	public R(Throwable e) {
		super();
		this.msg = e.getMessage();
		this.code = CommonConstants.FAIL;
	}


}

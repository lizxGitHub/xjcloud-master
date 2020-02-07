package gov.pbc.xjcloud.provider.contract.utils;

import gov.pbc.xjcloud.provider.contract.constants.CommonConstants;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Builder
@ToString
@Accessors(chain = true)
@AllArgsConstructor
public class R2<T> implements Serializable {
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

	public R2() {
		super();
	}

	public R2(T data) {
		super();
		this.data = data;
	}

	public R2(T data, String msg) {
		super();
		this.data = data;
		this.msg = msg;
	}

	public R2(Throwable e) {
		super();
		this.msg = e.getMessage();
		this.code = CommonConstants.FAIL;
	}


}

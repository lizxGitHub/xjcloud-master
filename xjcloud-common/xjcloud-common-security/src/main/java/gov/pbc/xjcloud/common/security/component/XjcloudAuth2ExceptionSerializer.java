package gov.pbc.xjcloud.common.security.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import gov.pbc.xjcloud.common.core.constant.CommonConstants;
import gov.pbc.xjcloud.common.security.exception.XjcloudAuth2Exception;
import lombok.SneakyThrows;

public class XjcloudAuth2ExceptionSerializer extends StdSerializer<XjcloudAuth2Exception> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6229534493935994577L;

	public XjcloudAuth2ExceptionSerializer() {
		super(XjcloudAuth2Exception.class);
	}

	@Override
	@SneakyThrows
	public void serialize(XjcloudAuth2Exception value, JsonGenerator gen, SerializerProvider provider) {
		gen.writeStartObject();
		gen.writeObjectField("code", CommonConstants.FAIL);
		gen.writeStringField("msg", value.getMessage());
		gen.writeStringField("data", value.getErrorCode());
		gen.writeEndObject();
	}
}

package gov.pbc.xjcloud.common.minio.vo;

import io.minio.ObjectStat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MinioObject {
	private String bucketName;
	private String name;
	private Date createdTime;
	private Long length;
	private String etag;
	private String contentType;
	private String matDesc;

	public MinioObject(ObjectStat os) {
		this.bucketName = os.bucketName();
		this.name = os.name();
		this.createdTime = os.createdTime();
		this.length = os.length();
		this.etag = os.etag();
		this.contentType = os.contentType();
		//this.matDesc = os.matDesc();
	}

}

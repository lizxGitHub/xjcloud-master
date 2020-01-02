package gov.pbc.xjcloud.common.data.modal;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CascadeModel {
	
	private Object value;
	private String label;
	private List<CascadeModel> children = new ArrayList<CascadeModel>();

}

package ${groupId}.clientdomain;

import java.io.Serializable;

public class HessianTestDto implements Serializable{
	
	private static final long serialVersionUID = 5736809480913645948L;
	
	private Integer type;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}

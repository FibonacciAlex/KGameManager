package data.userdata;

import java.util.HashMap;
import java.util.Map;


public enum UserTypeEnum {
	
	USER_TYPE_SYS_ADMIN(1,"系统管理员"),
	
	USER_TYPE_SENIOR_ADMIN(2,"高级管理员"),
	
	USER_TYPE_NORMAL(3,"普通用户"),
	
	USER_TYPE_MARKETING(4,"推广渠道用户");
	
	public final byte userType; // 数据类型的标识
	public final String typeName; // 数据类型的描述

	private UserTypeEnum(int pDataType, String pDataDesc) {
		this.userType = (byte) pDataType;
		this.typeName = pDataDesc;
	}

	// 所有枚举
	private static final Map<Byte, UserTypeEnum> enumMap = new HashMap<Byte, UserTypeEnum>();
	static {
		UserTypeEnum[] enums = UserTypeEnum.values();
		UserTypeEnum type;
		for (int i = 0; i < enums.length; i++) {
			type = enums[i];
			enumMap.put(type.userType, type);
		}
	}

	/**
	 * <pre>
	 * 通过标识数值获取枚举对象
	 * 
	 * @param type
	 * @return
	 * @creation 2012-12-3 下午3:53:28
	 * </pre>
	 */
	public static UserTypeEnum getEnum(byte missionFunType) {
		return enumMap.get(missionFunType);
	}

}

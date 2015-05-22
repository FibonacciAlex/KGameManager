package data.userdata;

public interface UserData {
	/**
	 * 登录用户名
	 * @return
	 */
	public String userName();
	
	/**
	 * 登录用户密码	
	 * @return
	 */
	public String password();
	
	/**
	 * 登录用户类型
	 * @return
	 */
	public UserTypeEnum userType();
	
	/**
	 * 登录用户的报表权限
	 * @return
	 */
	public UserPermission userPermission();

}

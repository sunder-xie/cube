package com.sharingif.cube.security.authentication.password;

import com.sharingif.cube.components.password.IOriginalPassword;
import com.sharingif.cube.core.exception.validation.ValidationCubeException;


/**
 *
 * @Description:  [验证原始密码]
 * @Author:       [Joly]
 * @CreateDate:   [2014年6月4日 下午4:00:00]
 * @UpdateUser:   [Joly]
 * @UpdateDate:   [2014年6月4日 下午4:00:00]
 * @UpdateRemark: [说明本次修改内容]
 * @Version:      [v1.0]
 *
 */
public interface IOriginalPasswordHandler {
	
	/**
	 * 验证原始密码
	 * @param username : 用户名
	 * @param originalPassword : 当前输入原始密码
	 */
	void handleOriginalPassword(String username, IOriginalPassword originalPassword)throws ValidationCubeException;
	
}

package com.sharingif.cube.components.monitor;

import java.util.Date;

/**
 *
 * @Description:  [对象操作历史]
 * @Author:       [Joly_Huang]
 * @CreateDate:   [2014年5月8日 下午12:29:01]
 * @UpdateUser:   [Joly_Huang]
 * @UpdateDate:   [2014年5月8日 下午12:29:01]
 * @UpdateRemark: [说明本次修改内容]
 * @Version:      [v1.0]
 *
 */

public interface IObjectOperationHistory {
	
	void setCreateUser(String value);
	
	String getCreateUser();
	
	void setModifyUser(String value);
	
	String getModifyUser();
	
	void setCreateTime(Date value);
	
	Date getCreateTime();
	
	void setModifyTime(Date value);
	
	Date getModifyTime();

}

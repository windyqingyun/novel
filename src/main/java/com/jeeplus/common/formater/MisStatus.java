package com.jeeplus.common.formater;
/**
 * @ClassName: MisStatus
 * @Description:
 * 		MIS信息响应状态对象
 * @Date:     2015-2-4 上午11:17:59
 * @author    mayongkang
 * @version   mis_alpha
 * @since     JDK 1.6
 */
public class MisStatus {
	private String statusCode;//状态码
	private String statusName;//状态码描述
	
	/**
	 * 无参构造方法
	 * Creates a new instance of MisStatus.
	 * @Date:	2015-3-27 下午8:37:06
	 * @Auther:	lining
	 */
	public MisStatus() {
		super();
	}
	/**
	 * 含参构造方法
	 * Creates a new instance of MisStatus.
	 * @Date:	2015-3-27 下午8:37:44
	 * @Auther:	lining
	 * @param statusCode
	 * @param statusName
	 */
	public MisStatus(String statusCode, String statusName) {
		super();
		this.statusCode = statusCode;
		this.statusName = statusName;
	}

	/**
	 * statusCode.
	 * @Date:	2015-2-4 上午11:17:52
	 * @Auther:	lining
	 * @return  the statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}
	/**
	 * statusCode.
	 * @Date:	2015-2-4 上午11:17:52
	 * @Auther:	lining
	 * @param   statusCode
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * statusName.
	 * @Date:	2015-2-4 上午11:17:52
	 * @Auther:	lining
	 * @return  the statusName
	 */
	public String getStatusName() {
		return statusName;
	}
	/**
	 * statusName.
	 * @Date:	2015-2-4 上午11:17:52
	 * @Auther:	lining
	 * @param   statusName
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	

	/**
	 * @MethodName：toString
	 * @HowToImplement
	 * 		TODO 描述一下实现该法的逻辑-可选
	 * @HowToUse:
	 *		TODO 这里描述这个方法的使用方法 – 可选
	 * @Notice:
	 *		TODO 这里描述这个方法的注意事项 – 可选
	 * @Date:   2015-2-4 下午6:37:55
	 * @author  lining
	 * @return
	 */
	@Override
	public String toString() {
		return "MisStatus [statusCode=" + statusCode + ", statusName="
				+ statusName +"]";
	}
	
}

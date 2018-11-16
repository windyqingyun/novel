/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.jeeplus.modules.bus.entity;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.common.persistence.DataEntity;

/**
 * 附件存储Entity
 * @author chenzw
 * @version 2016-10-24
 */
public class NodArchive extends DataEntity<NodArchive> {
	
	private static final long serialVersionUID = 1L;
	private String officeId;		// 所属机构
	private String fileName;		// 文件名称
	private String extension;		// 扩展名称
	private byte[] content;		// 附件内容
	
	public NodArchive() {
		super();
	}

	public NodArchive(String id){
		super(id);
	}

	
	@Length(min=1, max=50, message="文件名称长度必须介于 1 和 50 之间")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Length(min=1, max=10, message="扩展名称长度必须介于 1 和 10 之间")
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte [] content) {
		this.content = content;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
}
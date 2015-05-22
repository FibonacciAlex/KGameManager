package com.kola.core.data;

import com.kola.core.util.StringUtil;

public class Row implements IRow {
	private String id;

	public String getId() {
		if(this.id==null){
			this.id=StringUtil.getUUID();
			return this.id;
		}
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

}

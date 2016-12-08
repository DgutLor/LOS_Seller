package com.wsns.lor.entity;

/**
 * @author 泽恩 报修信息实体类
 */
public class Goods {
	private String brand;//品牌商
	private String type;//型号

	public Goods() {
	}


	public String getManufacturers() {
		return brand;
	}

	public void setManufacturers(String manufacturers) {
		this.brand = brand;
	}

	public String getMatName() {
		return type;
	}

	public void setMatName(String matName) {
		this.type = type;
	}

	public String toString(){
		return "brand="+brand+" type:"+type;
	}



}

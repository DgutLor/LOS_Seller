package com.wsns.lor.entity;


/**
 * @author 泽恩 订单实体类
 */
public class Order {
	private String id;// 订单编号
	private String seller;//店名
	private String tel;//卖家联系电话
	private String state;/*
	 * 订单状态（取值范围：{'A','B','C','D','E'},分别代表“未受理”，“退回”，“已受理
	 * ”，“待评价”，“已评价”）              这些状态到时候再看需要改改，保留
	 */
	private String type;//型号
	private String address;// 买家地址
	private String describe;//描述哪里出问题了
	private String time;// 买家期望时间
	private String createtime;// 订单创建时间
	private String confirmtime;// 商家受理时间
	private String finishtime;// 订单完成时间
	private String starttime;// 开始解决时间
	private String canceltime;//取消时间
	private String cancelConfirm;//取消时间

	private String buyername;//用户名
	
	private String cancelConfirmTime;

	public String getCancelConfirmTime() {
		
		return cancelConfirmTime;
	}

	@Override
	public String toString() {
		return "id"+id;
	}

	public void setCancelConfirmTime(String cancelConfirmTime) {
		this.cancelConfirmTime = cancelConfirmTime;
	}


	public String getCanceltime() {
		return canceltime;
	}



	public void setCanceltime(String canceltime) {
		this.canceltime = canceltime;
	}




	public Order() {

	}







	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}


	public String getDescribe() {
		return describe;
	}



	public void setDescribe(String describe) {
		this.describe = describe;
	}



	public String getTime() {
		return time;
	}



	public void setTime(String time) {
		this.time = time;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getConfirmtime() {
		return confirmtime;
	}

	public void setConfirmtime(String confirmtime) {
		this.confirmtime = confirmtime;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getFinishtime() {
		return finishtime;
	}

	public void setFinishtime(String finishtime) {
		this.finishtime = finishtime;
	}

	public String getCancelConfirm() {
		return cancelConfirm;
	}

	public void setCancelConfirm(String cancelConfirm) {
		this.cancelConfirm = cancelConfirm;
	}

	public String getBuyername() {
		return buyername;
	}

	public void setBuyername(String buyername) {
		this.buyername = buyername;
	}
}


package com.wsns.lor.entity;



/**
 * @author 泽恩 订单实体类
 */
public class OrderHttpResult<T> {
	private String code;

	//用来模仿Data
	private T order;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public T getOrder() {
		return order;
	}

	public void setOrder(T order) {
		this.order = order;
	}

	@Override
	public String toString() {
		System.out.println("OrderHttpResult");
		StringBuffer sb = new StringBuffer();
		sb.append("code="+ code);
		if (null != order) {
			sb.append("order:" + order.toString());
		}
		return sb.toString();
	}
}


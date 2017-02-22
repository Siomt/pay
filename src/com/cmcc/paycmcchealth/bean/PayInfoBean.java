package com.cmcc.paycmcchealth.bean;

/**
 * 支付宝支付订单实体类 
 * Created by liuhuan 2016/01/05
 */
public class PayInfoBean {

	// 商户网站唯一订单号
	private String tradeNo;

	// 商品名称
	public String subject;

	// 商品详情
	public String body;

	// 商品金额
	public String price;

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}

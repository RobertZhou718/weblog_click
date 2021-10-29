package com.weblog.domain;

public class StayTimePV {
	private Integer id;
	
	private String requestUrl;

  private Integer stayTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public Integer getStayTime() {
		return stayTime;
	}

	public void setStayTime(Integer stayTime) {
		this.stayTime = stayTime;
	}

	@Override
	public String toString() {
		return "StayTimePV [id=" + id + ", requestUrl=" + requestUrl + ", stayTime=" + stayTime + "]";
	}
  
}

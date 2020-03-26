package com.yao.spider.common.exception;


public class HttpClientException extends RuntimeException{

	
	private Integer status;
	private String httpResult;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1413861554881895281L;

	public HttpClientException(String msg) {
		super(msg);
	}
	
	public HttpClientException(String msg, Integer status, String result) {
		super(msg);
		this.status = status;
		this.setHttpResult(result);
	}
	
	public HttpClientException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public HttpClientException(String msg, Throwable cause, Integer status, String result) {
		super(msg, cause);
		this.status = status;
		this.setHttpResult(result);
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getHttpResult() {
		return httpResult;
	}

	public void setHttpResult(String httpResult) {
		this.httpResult = httpResult;
	}

    public String getMessage() {
        return "status:" + status + ", httpResult:" + httpResult  + ", msg:"+ super.getMessage();
    }
}

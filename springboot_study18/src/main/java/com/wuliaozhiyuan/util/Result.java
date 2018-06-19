package com.wuliaozhiyuan.util;

/**
 * 返回前台数据集包装类
 * @author wuliaozhiyuan
 *
 */
public class Result {
	/**
	 * 信息
	 */
	private String msg;
	/**
	 * 结果集
	 */
	private Object data;
	/**
	 * 状态：成功、失败
	 */
	private boolean success;
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Result(String msg, Object data) {
		this.msg = msg;
		this.data = data;
		this.success = true;
	}
	public Result(Object data) {
		this.success = true;
		this.msg = "success";
		this.data = data;
	}
	public Result(String msg, Object data, boolean success) {
		this.msg = msg;
		this.data = data;
		this.success = success;
	}
	public static Result build(String msg, Object data) {
        return new Result(msg, data);
    }
	
	public static Result build(String msg, Object data, boolean success) {
		return new Result(msg, data, success);
	}
	
	 public static Result success(Object data) {
        return new Result(data);
    }
	 public static Result success() {
        return new Result(null);
    }
	
}

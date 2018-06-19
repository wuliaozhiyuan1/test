package com.wuliaozhiyuan.util;

/**
 * 自定义异常，属于正常业务逻辑下的异常
 * @author wuliaozhiyuan
 *
 */
public class CustomException extends RuntimeException{

	public CustomException(String message){
		super(message);
	}
}

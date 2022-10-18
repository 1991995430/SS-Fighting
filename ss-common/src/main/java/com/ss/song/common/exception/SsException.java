/*
 * ***************************************************************************************
 * Copyright (c) 2015.
 * Shenzhen SEGI information technology co., LTD
 * All rights reserved.
 * ****************************************************************************************
 */

package com.ss.song.common.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/*********
 * 租赁异常接口类
 */
@SuppressWarnings("serial")
public class SsException extends RuntimeException {

	private Throwable wrappedThrowable = null;

	private String code;

	public SsException() {
		super();
	}

	public SsException(String code, String message) {
		super(message);
		this.code = code;
	}
	public SsException(String message) {
		super(message);
	}

	public SsException(Throwable wrappedThrowable) {
		super();
		this.wrappedThrowable = wrappedThrowable;
	}

	public SsException(String message, Throwable wrappedThrowable) {
		super(message);
		this.wrappedThrowable = wrappedThrowable;
	}
	public Throwable getWrappedThrowable() {
		return wrappedThrowable;
	}

	@Override
	public void printStackTrace() {
		printStackTrace(System.err);
	}

	@Override
	public void printStackTrace(PrintStream out) {
		super.printStackTrace(out);
		if (wrappedThrowable != null) {
			out.println("Nested Exception: ");
			wrappedThrowable.printStackTrace(out);
		}
	}

	@Override
	public void printStackTrace(PrintWriter out) {
		super.printStackTrace(out);
		if (wrappedThrowable != null) {
			out.println("Nested Exception: ");
			wrappedThrowable.printStackTrace(out);
		}
	}

	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		String message = super.getMessage();
		if (message != null) {
			buf.append(message).append(": ");
		}
		if (wrappedThrowable != null) {
			buf.append("\n  -- caused by: ").append(wrappedThrowable);
		}

		return buf.toString();
	}
}

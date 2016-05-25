/*
 * Copyright 2012-2016 Alex Garrett. All rights reserved.
 *
 */
package com.trufl.tinysql;

@SuppressWarnings("serial")
public class TinySqlException extends RuntimeException {

	public TinySqlException() {
	}

	public TinySqlException(String arg0) {
		super(arg0);
	}

	public TinySqlException(Throwable arg0) {
		super(arg0);
	}

	public TinySqlException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}

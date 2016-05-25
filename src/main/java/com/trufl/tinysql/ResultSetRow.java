/*
 * Copyright 2012-2016 Alex Garrett. All rights reserved.
 *
 */
package com.trufl.tinysql;


public interface ResultSetRow {

	String getString(String _column);
	Object getObject(String _column);
	int getInt(String _column);
}

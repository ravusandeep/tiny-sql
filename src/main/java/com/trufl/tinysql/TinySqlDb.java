/*
 * Copyright 2012-2016 Alex Garrett. All rights reserved.
 *
 */
package com.trufl.tinysql;

public interface TinySqlDb {
	public int execute(String _sql);
	public DbMetadata getMetadata();
	public ResultSet query(String _sql);
}

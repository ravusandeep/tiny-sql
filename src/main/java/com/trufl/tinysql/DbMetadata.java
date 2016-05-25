/*
 * Copyright 2012-2016 Alex Garrett. All rights reserved.
 */
package com.trufl.tinysql;

import java.util.Set;

public interface DbMetadata {
	public Set<String> getTableNames();
}

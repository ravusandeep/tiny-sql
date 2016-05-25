package com.trufl.tinysql;

import java.util.HashSet;
import java.util.Set;

public class DbMetadataImpl implements DbMetadata {

	Set<String> values = new HashSet<String>();
	
	@Override
	public Set<String> getTableNames() {
		return values;
	}

}

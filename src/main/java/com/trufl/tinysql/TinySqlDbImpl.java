/*
 * Copyright 2012-2016 Alex Garrett. All rights reserved.
 *
 */

package com.trufl.tinysql;

import java.util.HashSet;
import java.util.Set;

public class TinySqlDbImpl implements TinySqlDb {
	
	public static final int _ZERO = 0;
	public static final int _ONE = 1;
	public static final int _TWO = 2;
	public static final int _THREE = 3;
	public static final String MORE_WHITE_SPACE = "\\s+";
	public static final String SINGLE_WHITE_SPACE = " ";
	public static final String COMMA = ","; 
	public static final String FORWARD_PARENTHESES = "(";

	DbMetadata metaData = new DbMetadataImpl();
	
	@Override
	public int execute(String _sql) {

		String columns = getColumns(_sql);
		if (columns.length() <= _ZERO) {// no columns
			throw new TinySqlException();
		} else {// has some columns
			columnNamesValidation(columns);
		}
		String tableName = getTableName(_sql);
		addTableName(tableName);

		return _ONE;

	}

	@Override
	public DbMetadata getMetadata() {
		return this.metaData;
	}

	@Override
	public ResultSet query(String _sql) {
		throw new UnsupportedOperationException("Implement me");
	}

	private void columnNamesValidation(String columns) {
		Set<String> columnNames = new HashSet<String>();
		String column[] = columns.split(COMMA);
		for (String eachColumn : column) {

			validateEachColum(eachColumn);
			validateColumType(eachColumn);
			String columnName = getColumnName(eachColumn);
			if (columnNames.contains(columnName))
				throw new TinySqlException();
			columnNames.add(columnName);
		}

	}

	private void validateColumType(String eachColumn) {
		boolean flag = false;
		for (TinySqlTypes type : TinySqlTypes.values()) {
			if (checkSqlType(type, eachColumn)) {
				flag = true;
			}
		}
		if (!flag)
			throw new TinySqlException();

	}

	private void validateEachColum(String eachColumn) {
		if (getEachColumn(eachColumn).length != _TWO)
			throw new TinySqlException();
	}

	private String getTableName(String _sql) {
		String tempArray[] = _sql.substring(_ZERO, _sql.indexOf(FORWARD_PARENTHESES)).trim()
				.split(SINGLE_WHITE_SPACE);
		String tableName = tempArray[tempArray.length - _ONE];
		if (tempArray.length != _THREE)
			throw new TinySqlException();
		if (this.metaData.getTableNames().contains(tableName.toLowerCase())) {
			throw new TinySqlException();
		}
		return tableName;
	}
	
	private void addTableName(String tableName) {
		this.metaData.getTableNames().add(tableName.toLowerCase());
	}

	public String getColumns(String _sql) {
		return _sql.substring(_sql.indexOf(FORWARD_PARENTHESES) + _ONE, _sql.length() - _ONE);
	}

	public String[] getEachColumn(String _column) {
		return _column.replaceAll(MORE_WHITE_SPACE, SINGLE_WHITE_SPACE).trim().split(SINGLE_WHITE_SPACE);
	}

	public String getColumnName(String eachColumn) {
		return eachColumn.replaceAll(MORE_WHITE_SPACE, SINGLE_WHITE_SPACE).trim().split(SINGLE_WHITE_SPACE)[_ZERO].trim()
				.toUpperCase();
	}

	public boolean checkSqlType(TinySqlTypes sqlType, String eachColumn) {
		return sqlType.toString().equals(
				getEachColumn(eachColumn)[_ONE].trim().toUpperCase());
	}

}

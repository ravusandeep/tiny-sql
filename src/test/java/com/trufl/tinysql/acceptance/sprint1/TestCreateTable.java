 /*
 * Copyright 2012-2016 Alex Garrett. All rights reserved.
 *
 */
package com.trufl.tinysql.acceptance.sprint1;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.trufl.tinysql.TinySqlDb;
import com.trufl.tinysql.TinySqlException;

public class TestCreateTable {
	private TinySqlDb db;
	
	@Before
	public void setup() {
		this.db = (TinySqlDb)new ClassPathXmlApplicationContext("application-context.xml").getBean("tinysql-db");
	}
	
	@Test(expected=TinySqlException.class)
	public void tableWithNoColumnsIsInvalid() {
		String sql = "create table foo()";
		this.db.execute(sql);
	}
	
	@Test
	public void tableWithOneColumnIsValid() {
		String sql = "create table foo(id integer)";
		this.db.execute(sql);
		assertTrue(this.db.getMetadata().getTableNames().contains("foo"));
	}
	
	@Test
	public void tablesWithMultipleColumnsAreValid() {
		String sql = "create table foo(id integer, label string)";
		this.db.execute(sql);
		assertTrue(this.db.getMetadata().getTableNames().contains("foo"));
	}

	@Test
	public void tableSqlIsCaseInsensitive() {
		String sql = "CREATE TABLE FOO(ID INTEGER)";
		this.db.execute(sql);
		assertTrue(this.db.getMetadata().getTableNames().contains("foo"));
	}
	
	@Test(expected=TinySqlException.class)
	public void tablesMustHaveUniqueNames() {
		String sql = "create table foo(id integer)";
		this.db.execute(sql);
		assertTrue(this.db.getMetadata().getTableNames().contains("foo"));
		this.db.execute(sql);
	}
	
	@Test
	public void createTableSqlShouldAcceptWhitespace() {
		String sql = "create table foo(" +
				"	id integer," +
				"	label string," +
				"	price				integer" + // contains tabs
				")";
		this.db.execute(sql);
		assertTrue(this.db.getMetadata().getTableNames().contains("foo"));
	}

	@Test(expected=TinySqlException.class)
	public void tableColumnsMustHaveValidTypes() {
		this.db.execute("create table foo (label varchar)");
	}
	
	@Test(expected=TinySqlException.class)
	public void tableColumnsMustBeUnique() {
		this.db.execute("create table foo (label string, label string)");
	}
	
	@Test(expected=TinySqlException.class)
	public void tableMustHaveName() {
		this.db.execute("create table (label string)");
	}
	
	@Test(expected=TinySqlException.class)
	public void tableColumnsMustHaveNames() {
		this.db.execute("create table foo (string)");
	}
	
	@Test(expected=TinySqlException.class)
	public void tableColumnsMustHaveTypes() {
		this.db.execute("create table foo (label)");
	}
	
	@Test(expected=TinySqlException.class)
	public void tableColumnsMustBeSeparatedByCommas() {
		this.db.execute("create table foo (label string label string)");
	}
	
	@Test(expected=TinySqlException.class)
	public void tableColumnDefinitionsMustBeEnclosedInParentheses_missingLeft() {
		this.db.execute("create table foo label string)");
	}
	
	@Test(expected=TinySqlException.class)
	public void tableColumnDefinitionsMustBeEnclosedInParentheses_missingRight() {
		this.db.execute("create table foo (label string");
	}
}

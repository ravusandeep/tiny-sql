/*
 * Copyright 2012-2016 Alex Garrett. All rights reserved.
 *
 */
package com.trufl.tinysql.acceptance.sprint1;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.trufl.tinysql.ResultSet;
import com.trufl.tinysql.TinySqlDb;
import com.trufl.tinysql.TinySqlException;

public class TestUpdate {
	private TinySqlDb db;
	
	@Before
	public void setup() {
		this.db = (TinySqlDb)new ClassPathXmlApplicationContext("application-context.xml").getBean("tinysql-db");
		createBaseTable();
	}
	
	@Test
	public void dbShouldSupportSimpleUpdateAndSelect() {
		this.db.execute("insert into foo (id, label) values(1, 'foo')");
		this.db.execute("update foo set label = 'bar' where id = 1");
		String selectSql = "select id, label from foo";
		ResultSet rs = this.db.query(selectSql);
		assertResultSet(rs).withRow(1).withColumns("id", "label").withValues(1, "bar");
	}
	
	@Test
	public void missingWhereClauseShouldUpdateAllRows() throws Exception{
		insertLots();
		assertThat(this.db.execute("update foo set label = 'bar'")).isEqualTo(100);
		String selectSql = "select id, label from foo where label = 'bar'";
		ResultSet rs = this.db.query(selectSql);
		assertResultSet(rs).hasRows(100);
	}
	
	@Test
	public void updateShouldWorkWithMultipleColumns() throws Exception{
		this.db.execute("insert into foo (id, label) values(1, 'foo')");
		this.db.execute("update foo set (id, label) = (2, 'bar') where id = 1");
		String selectSql = "select id, label from foo";
		ResultSet rs = this.db.query(selectSql);
		assertResultSet(rs).withRow(1).withColumns("id", "label").withValues(2, "bar");
	}
	
	@Test(expected=TinySqlException.class)
	public void whereClauseIsTypeSensitiveStringForInt() throws Exception{
		insertLots();
		this.db.execute("update foo set label = 'bar' where id = 'abactinally'");
	}
	
	@Test(expected=TinySqlException.class)
	public void whereClauseIsTypeSensitiveIntForString() throws Exception{
		insertLots();
		this.db.execute("update foo set label = 'bar' where label = 3");
	}
	
	@Test(expected=TinySqlException.class)
	public void updateShouldFailWhenGivenUnknownColumn() {
		this.db.execute("update foo set zot = 'bar' ");
	}
	
	@Test
	public void updateShouldWorkWithZeroRowsInDb() {
		assertThat(this.db.execute("update foo set label = 'bar'")).isEqualTo(0);
	}
	
	@Test
	public void updateIsCaseInsensitive() {
		this.db.execute("insert into foo (id, label) values(1, 'foo')");
		this.db.execute("UPDATE FOO SET LABEL = 'bar' WHERE ID = 1");
		String selectSql = "select id, label from foo";
		ResultSet rs = this.db.query(selectSql);
		assertResultSet(rs).withRow(1).withColumns("id", "label").withValues(1, "bar");
	}
	
	@Test
	public void updateIsWhitespaceInsensitive() {
		String insertSql = "insert into foo (" +
				"	id, " +
				"	label" +
				") values (" +
				"	1, " +
				"	'foo'" +
				")";
		this.db.execute(insertSql);
		String selectSql = "select " +
				"	id, " +
				"	label " +
				"from foo";
		ResultSet rs = this.db.query(selectSql);
		assertResultSet(rs).withRow(1).withColumns("id", "label").withValues(1, "foo");
	}
	
	@Test(expected=TinySqlException.class)
	public void updateShouldFailWhenColumnCardinalityDoesntMatchValueCardinality() {
		this.db.execute("update foo set (id, label) = 'bar'");
	}
	
	@Test(expected=TinySqlException.class)
	public void updateShouldFailWhenValuesAreAbsent() {
		this.db.execute("update foo set id = ");
	}
	
	@Test(expected=TinySqlException.class)
	public void updateShouldFailWhenTableIsntInSchema() {
		this.db.execute("update bar set id = 3");
	}
	
	private void insertLots() throws Exception {
		@SuppressWarnings("unchecked")
		List<String> words = IOUtils.readLines(getClass().getClassLoader().getResourceAsStream("words"));
		int index = 0;
		for(String word : words) {
			String sql = "insert into foo (id, label) values (" + index++ +", " + "'" + word +"')";
			this.db.execute(sql);
			if (index == 100) { break; }
		}
	}

	private void createBaseTable() {
		String sql = "create table foo(id integer, label string)";
		this.db.execute(sql);
	}
	
	private ResultSetAssertion assertResultSet(ResultSet _rs) {
		return new ResultSetAssertion(_rs);
	}
}

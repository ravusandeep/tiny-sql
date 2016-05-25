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
import com.trufl.tinysql.TinySqlTypes;

public class TestInsertAndSelect {
	private TinySqlDb db;
	
	@Before
	public void setup() {
		this.db = (TinySqlDb)new ClassPathXmlApplicationContext("application-context.xml").getBean("tinysql-db");
		createBaseTable();
	}
	
	@Test
	public void dbShouldSupportSimpleInsertAndSelect() {
		String insertSql = "insert into foo (id, label) values(1, 'foo')";
		assertThat(this.db.execute(insertSql)).isEqualTo(1);
		String selectSql = "select id, label from foo";
		ResultSet rs = this.db.query(selectSql);
		assertResultSet(rs).withRow(1).withColumns("id", "label").withValues(1, "foo");
	}
	
	@Test
	public void dbShouldSupportMultipleInserts() throws Exception{
		insertLots();
		ResultSet rs = this.db.query("select * from foo");
		assertResultSet(rs).hasRows(100).withRow(30).withColumns("label").withValues("abactinally");
	}
	
	@Test
	public void selectCanFilterOnIdentityString() throws Exception{
		insertLots();
		ResultSet rs = this.db.query("select * from foo where label = 'abactinally'");
		assertResultSet(rs).hasRows(1).withRow(1).withColumns("label").withValues("abactinally");
	}
	
	@Test
	public void selectCanFilterOnIdentityInteger() throws Exception{
		insertLots();
		ResultSet rs = this.db.query("select * from foo where id = 75");
		assertResultSet(rs).hasRows(1).withRow(1).withColumns("id", "label").withValues(75, "abasia");
	}
	
	@Test(expected=TinySqlException.class)
	public void whereClauseIsTypeSensitiveStringForInt() throws Exception{
		insertLots();
		this.db.query("select * from foo where id = 'abactinally'");
	}
	
	@Test(expected=TinySqlException.class)
	public void whereClauseIsTypeSensitiveIntForString() throws Exception{
		insertLots();
		this.db.query("select * from foo where label = 3");
	}
	
	@Test(expected=TinySqlException.class)
	public void selectShouldFailWhenGivenUnknownColumn() {
		this.db.query("select zot from foo");
	}
	
	@Test
	public void selectShouldWorkWithZeroRowsInDb() {
		assertResultSet(this.db.query("select * from foo")).hasRows(0);
	}
	
	@Test
	public void selectAndInsertAreCaseInsensitive() {
		String insertSql = "INSERT INTO FOO (ID, LABEL) VALUES (1, 'foo')";
		this.db.execute(insertSql);
		String selectSql = "SELECT ID, LABEL FROM FOO";
		ResultSet rs = this.db.query(selectSql);
		assertResultSet(rs).withRow(1).withColumns("id", "label").withValues(1, "foo");
	}
	
	@Test
	public void selectAndInsertAreWhitespaceInsensitive() {
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
	public void insertShouldFailWhenGivenUnknownColumn() {
		this.db.execute("insert into foo (zot) values (3)");
	}
	
	@Test(expected=TinySqlException.class)
	public void insertShouldFailWhenColumnCardinalityDoesntMatchValueCardinality() {
		this.db.execute("insert into foo (id, label) values (3)");
	}
	
	@Test(expected=TinySqlException.class)
	public void insertShouldFailWhenValuesAreAbsent() {
		this.db.execute("insert into foo (id) values ()");
	}
	
	@Test(expected=TinySqlException.class)
	public void insertShouldFailWhenTableIsntInSchema() {
		this.db.execute("insert into bar (id) values ()");
	}
	
	@Test
	public void columnOrderShouldntMatter() {
		String insertSql = "insert into foo (id, label) values(1, 'foo')";
		this.db.execute(insertSql);
		String selectSql = "select label, id from foo";
		ResultSet rs = this.db.query(selectSql);
		assertResultSet(rs).withRow(1).withColumns("id", "label").withValues(1, "foo");
	}
	
	@Test
	public void omittedValuesShouldBeNull() {
		String insertSql = "insert into foo (id) values(1)";
		this.db.execute(insertSql);
		String selectSql = "select label, id from foo";
		ResultSet rs = this.db.query(selectSql);
		assertResultSet(rs).withRow(1).withColumns("id", "label").withValues(1, TinySqlTypes.NULL);
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

/*
 /*
 * Copyright 2012-2016 Alex Garrett. All rights reserved.
 *
 */
package com.trufl.tinysql.acceptance.sprint1;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.trufl.tinysql.ResultSet;
import com.trufl.tinysql.ResultSetRow;
import com.trufl.tinysql.TinySqlDb;

public class TestBasicUsageScenario {
	private static final String createTable = "create table foo(" +
			"	id integer," +
			"	name string," +
			"	email string" +
			")";
	
	private static final String insert = "insert into foo (id, name, email) values (1, \"dimwit\", \"dimwit@flathead.gue\")";
	private static final String select = "select id, name, email from foo";

	private static final String update = "update foo set name = \"dimwit_3rd\" where id = 1";
	
	private TinySqlDb db;
	
	
	@Before
	public void setup() {
		this.db = (TinySqlDb)new ClassPathXmlApplicationContext("application-context.xml").getBean("tinysql-db");
	}
	
	@Test
	public void basicUsageScenario() {
		this.db.execute(createTable);
		this.db.execute(insert);
		ResultSet rs = this.db.query(select);
		for (ResultSetRow row : rs) {
			assertEquals(1, row.getInt("id"));
			assertEquals("dimwit", row.getString("name"));
			assertEquals("dimwit@flathead.gue", row.getString("email"));
		}
		rs = this.db.query("select id from foo");
		this.db.execute(update);
		rs = this.db.query(select);
		for (ResultSetRow row : rs) {
			assertEquals(1, row.getInt("id"));
			assertEquals("dimwit", row.getString("name"));
			assertEquals("dimwit_3rd@flathead.gue", row.getString("email"));
		}
	}
}
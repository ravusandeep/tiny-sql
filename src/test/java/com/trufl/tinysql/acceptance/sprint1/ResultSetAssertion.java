package com.trufl.tinysql.acceptance.sprint1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import com.trufl.tinysql.ResultSet;
import com.trufl.tinysql.ResultSetRow;

public class ResultSetAssertion {
	private List<ResultSetRow> rows = new ArrayList<ResultSetRow>();
	private String[] columns;
	private ResultSetRow currentRow;
	
	public ResultSetAssertion(ResultSet _rs) {
		for(ResultSetRow r : _rs) {
			rows.add(r);
		}
	}
	
	public ResultSetAssertion hasRows(int _expectedRows) {
		assertEquals(_expectedRows, rows.size());
		return this;
	}
	
	public ResultSetAssertion withRow(int _rowNum) {
		this.currentRow = rows.get(_rowNum - 1);
		return this;
	}
	
	public ResultSetAssertion withColumns(String... _columns) {
		if (this.currentRow == null) { throw new RuntimeException("need to specify current row first."); }
		this.columns = _columns;
		if (this.rows.isEmpty()) {
			fail("No rows");
		}
		for(String column : _columns) {
			currentRow.getObject(column);
		}
		return this;
	}
	
	public ResultSetAssertion withValues(Object...  _values) {
		if (this.columns == null) { throw new RuntimeException("Need to specify columns first"); }
		for (int i = 0; i < this.columns.length; ++i) {
			assertEquals(_values[i], currentRow.getObject(columns[i]));
		}
		return this;
	}
}

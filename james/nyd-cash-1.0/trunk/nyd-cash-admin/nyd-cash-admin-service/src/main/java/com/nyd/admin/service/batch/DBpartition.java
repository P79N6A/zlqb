package com.nyd.admin.service.batch;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class DBpartition implements Partitioner {
	private static final String _MINRECORD = "_minRecord";
	private static final String _MAXRECORD = "_maxRecord";
	private static final String MIN_SELECT_PATTERN = "select min({0}) from {1}";
	private static final String MAX_SELECT_PATTERN = "select max({0}) from {1}";
	private JdbcTemplate jdbcTemplate ;
	private DataSource dataSource;
	private String table ;
	private String column;
	
	public Map<String, ExecutionContext> partition(int gridSize) {
		validateAndInit();
		Map<String, ExecutionContext> resultMap = new HashMap<String, ExecutionContext>();
		int min = jdbcTemplate.queryForObject(MessageFormat.format(MIN_SELECT_PATTERN, new Object[]{column,table}),Integer.class);
		int max = jdbcTemplate.queryForObject(MessageFormat.format(MAX_SELECT_PATTERN, new Object[]{column,table}),Integer.class);
		int targetSize = (max-min)/gridSize +1;
		int number=0;
		int start =min;
		int end = start+targetSize-1;
		
		while(start <= max){
			ExecutionContext context = new ExecutionContext();
			if(end>=max){
				end=max;
			}
			context.putInt(_MINRECORD, start);
			context.putInt(_MAXRECORD, end);
			start+=targetSize;
			end+=targetSize;
			resultMap.put("partition"+(number++), context);
		}
		return resultMap;
	}
	
	public void validateAndInit(){
		if(isEmpty(table)){
			throw new IllegalArgumentException("table cannot be null");
		}
		if(isEmpty(column)){
			throw new IllegalArgumentException("column cannot be null");
		}
		if(dataSource!=null && jdbcTemplate==null){
			jdbcTemplate = new JdbcTemplate(dataSource);
		}
		if(jdbcTemplate==null){
			throw new IllegalArgumentException("jdbcTemplate cannot be null");
		}
	}

	public static boolean isEmpty(String info){
		if(info!=null){
			if(info.trim().length()>1){
				return false;
			}
		}
		return true;
	}
	
	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}

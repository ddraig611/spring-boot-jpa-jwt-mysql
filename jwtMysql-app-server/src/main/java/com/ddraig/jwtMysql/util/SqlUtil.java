package com.ddraig.jwtMysql.util;

public class SqlUtil {
	
	public static String createPaginationSqlNormal(String sql, int pageIndex, int pageSize) {
		String resultSql = "";
		if(pageSize > 0 && pageIndex != 999999) {
			resultSql += "SELECT * FROM (SELECT * FROM ( SELECT a.*, rownum row_stt from (" + sql + ") a )";
			resultSql += "WHERE rownum < " + (pageIndex * pageSize + 1) + ")";
			resultSql += "WHERE row_stt >=  " + ( (pageIndex - 1) * pageSize + 1);
		} else {
			resultSql += "select * from ( select * from (select rownum row_stt,b.* from (";
			resultSql += sql;
			resultSql += ") b order by row_stt desc ";
			resultSql += "where rownum <= " + pageSize;
		}
		return resultSql;
	}
	
	public static String createPaginationSqlUsingOffSet(String sql, int pageIndex, int pageSize) {
		String resultSql = "";
		if(pageIndex > 1) {
			resultSql = sql + " OFFSET "  + ((pageIndex - 1) * pageSize) + " ROWS  FETCH NEXT "  + pageSize + " ROWS ONLY";
		} else {
			resultSql = sql + " FETCH NEXT "  + pageSize + " ROWS ONLY";
		}
		return resultSql;
	}
}


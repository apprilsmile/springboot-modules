/*
 * Licensed to the Wiseco Software Corporation under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appril.datasource.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 数据库JDBC连接工具类
 * Created by yuandl on 2016-12-16.
 */
@Slf4j
public class DbUtil {


    private static JdbcTemplate jdbcTemplate;
    private static TransactionTemplate internalTransactionTemplate;

    static {
        jdbcTemplate = SpringContextHolder.getBean("internalJdbcTemplate");
        internalTransactionTemplate = SpringContextHolder.getBean("internalTransactionTemplate");
    }
    private static final String TABLE_COLUMN = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = '%s'";

    /**
     * getTableColumn
     * @param tableName
     * @return columnList
     */
    public static List<String> getTableColumn(String tableName){
        String sql = String.format(TABLE_COLUMN, tableName);
        return jdbcTemplate.queryForList(sql,String.class);
    }

    /**
     * 执行数据库插入操作
     *
     * @param valueMap  插入数据表中key为列名和value为列对应的值的Map对象
     * @param tableName 要插入的数据库的表名
     * @return 影响的行数
     */
    public static int insert(String tableName, Map<String, Object> valueMap) throws Exception {
        //要插入的字段sql，其实就是用key拼起来的
        StringBuilder columnSql = new StringBuilder();
        //要插入的字段值，其实就是？
        StringBuilder unknownMarkSql = new StringBuilder();
        Object[] bindArgs = new Object[valueMap.size()];
        int i = 0;
        for (Map.Entry<String, Object> valueEntry : valueMap.entrySet()) {
            columnSql.append(i == 0 ? "" : ",");
            columnSql.append(valueEntry.getKey());
            unknownMarkSql.append(i == 0 ? "" : ",");
            unknownMarkSql.append("?");
            bindArgs[i] = valueEntry.getValue();
            i++;
        }
        //开始拼插入的sql语句
        String sql = "INSERT INTO "
                + tableName
                + " ("
                + columnSql
                + " )  VALUES ("
                + unknownMarkSql
                + " )";
        return executeUpdate(sql, bindArgs);
    }

    /**
     * 执行数据库批量插入操作
     *
     * @param mapList   插入数据表中key为列名和value为列对应的值的Map对象的List集合
     * @param tableName 要插入的数据库的表名
     * @return 影响的行数
     */
    public static int batchInsert(String tableName, List<Map<String, Object>> mapList) throws Exception {
        //影响的行数
        int affectRowCount = -1;
        Map<String, Object> valueMap = mapList.get(0);
        StringBuilder columnSql = new StringBuilder();
        //要插入的字段值，其实就是？
        StringBuilder unknownMarkSql = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, Object> valueEntry : valueMap.entrySet()) {
            columnSql.append(i == 0 ? "" : ",");
            columnSql.append(valueEntry.getKey());
            unknownMarkSql.append(i == 0 ? "" : ",");
            unknownMarkSql.append("?");
            i++;
        }
        //开始拼插入的sql语句
        String sql = "INSERT INTO "
                + tableName
                + " ("
                + columnSql
                + " )  VALUES ("
                + unknownMarkSql
                + " )";
        List<Object[]> batchArgs = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            Object[] bindArgs = new Object[map.size()];
            int j = 0;
            for (Map.Entry<String, Object> valueEntry : map.entrySet()) {
                bindArgs[j] = valueEntry.getValue();
                j++;
            }
            batchArgs.add(bindArgs);
        }
        int[] arr = jdbcTemplate.batchUpdate(sql, batchArgs);
        affectRowCount = arr.length;
        log.info("成功了插入了" + affectRowCount + "行");
        return affectRowCount;
    }


    /**
     * 执行更新操作
     *
     * @param tableName 表名
     * @param valueMap  要更改的值
     * @param whereMap  条件
     * @return 影响的行数
     */
    public static int update(String tableName, Map<String, Object> valueMap, Map<String, Object> whereMap) throws Exception {
        //开始拼插入的sql语句
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append(tableName);
        sql.append(" SET ");
        //要更改的的字段sql，其实就是用key拼起来的
        StringBuilder columnSql = new StringBuilder();
        int i = 0;
        List<Object> objects = new ArrayList<>();
        for (Map.Entry<String, Object> valueEntry : valueMap.entrySet()) {
            columnSql.append(i == 0 ? "" : ",");
            columnSql.append(valueEntry.getKey()).append(" = ? ");
            objects.add(valueEntry.getValue());
            i++;
        }
        sql.append(columnSql);
        //更新的条件:要更改的的字段sql，其实就是用key拼起来的
        StringBuilder whereSql = new StringBuilder();
        int j = 0;
        if (!CollectionUtils.isEmpty(whereMap)) {
            whereSql.append(" WHERE ");
            for (Map.Entry<String, Object> whereEntry : whereMap.entrySet()) {
                whereSql.append(j == 0 ? "" : " AND ");
                whereSql.append(whereEntry.getKey()).append(" = ? ");
                objects.add(whereEntry.getValue());
                j++;
            }
            sql.append(whereSql);
        }
        return executeUpdate(sql.toString(), objects.toArray());
    }

    /**
     * 执行删除操作
     *
     * @param tableName 要删除的表名
     * @param whereMap  删除的条件
     * @return 影响的行数
     */
    public static int delete(String tableName, Map<String, Object> whereMap) throws Exception {
        //准备删除的sql语句
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append(tableName);
        //更新的条件:要更改的的字段sql，其实就是用key拼起来的
        StringBuilder whereSql = new StringBuilder();
        Object[] bindArgs = null;
        if (!CollectionUtils.isEmpty(whereMap)) {
            whereSql.append(" WHERE ");
            bindArgs = new Object[whereMap.size()];
            int i = 0;
            for (Map.Entry<String, Object> whereEntry : whereMap.entrySet()) {
                whereSql.append(i == 0 ? "" : " AND ");
                whereSql.append(whereEntry.getKey()).append(" = ? ");
                bindArgs[i] = whereEntry.getValue();
                i++;
            }
            sql.append(whereSql);
        }
        return executeUpdate(sql.toString(), bindArgs);
    }

    /**
     * 可以执行新增，修改，删除
     *
     * @param sql      sql语句
     * @param bindArgs 绑定参数
     * @return 影响的行数
     */
    public static int executeUpdate(String sql, Object[] bindArgs) throws Exception {
        //执行sql
        return jdbcTemplate.update(sql, bindArgs);
    }

    /**
     * 通过sql查询数据,
     * 慎用，会有sql注入问题
     *
     * @param sql
     * @return 查询的数据集合
     * @throws SQLException
     */
    public static List<Map<String, Object>> query(String sql) throws SQLException {
        return executeQuery(sql, null);
    }

    /**
     * 执行sql通过 Map<String, Object>限定查询条件查询
     *
     * @param tableName 表名
     * @param whereMap  where条件
     * @return List<Map < String, Object>>
     */
    public static List<Map<String, Object>> query(String tableName,
                                                  Map<String, Object> whereMap) {
        StringBuilder whereClause = new StringBuilder();
        Object[] whereArgs = null;
        if (!CollectionUtils.isEmpty(whereMap)) {
            whereArgs = new Object[whereMap.size()];
            int i = 0;
            for (Map.Entry<String, Object> whereEntry : whereMap.entrySet()) {
                whereClause.append(i == 0 ? "" : " AND ");
                whereClause.append(whereEntry.getKey()).append(" = ? ");
                whereArgs[i] = whereEntry.getValue();
                i++;
            }
        }
        return query(tableName, false, null, whereClause.toString(), whereArgs, null, null, null, null);
    }

    /**
     * 执行sql条件参数绑定形式的查询
     *
     * @param tableName   表名
     * @param whereClause where条件的sql
     * @param whereArgs   where条件中占位符中的值
     * @return List<Map < String, Object>>
     * @throws SQLException
     */
    public static List<Map<String, Object>> query(String tableName,
                                                  String whereClause,
                                                  String[] whereArgs) throws SQLException {
        return query(tableName, false, null, whereClause, whereArgs, null, null, null, null);
    }

    /**
     * 执行全部结构的sql查询
     *
     * @param tableName     表名
     * @param distinct      去重
     * @param columns       要查询的列名
     * @param selection     where条件
     * @param selectionArgs where条件中占位符中的值
     * @param groupBy       分组
     * @param having        筛选
     * @param orderBy       排序
     * @param limit         分页
     * @return List<Map < String, Object>>
     */
    public static List<Map<String, Object>> query(String tableName,
                                                  boolean distinct,
                                                  String[] columns,
                                                  String selection,
                                                  Object[] selectionArgs,
                                                  String groupBy,
                                                  String having,
                                                  String orderBy,
                                                  String limit) {
        String sql = buildQueryString(distinct, tableName, columns, selection, groupBy, having, orderBy, limit);
        return executeQuery(sql, selectionArgs);

    }

    /**
     * 执行全部结构的sql查询
     *
     * @param tableName     表名
     * @param distinct      去重
     * @param columns       要查询的列名
     * @param selection     where条件
     * @param selectionArgs where条件中占位符中的值
     * @param groupBy       分组
     * @param having        筛选
     * @param orderBy       排序
     * @param limit         分页
     * @return 记录数
     */
    public static int queryCount(String tableName,
                                 boolean distinct,
                                 String[] columns,
                                 String selection,
                                 Object[] selectionArgs,
                                 String groupBy,
                                 String having,
                                 String orderBy,
                                 String limit) {
        String sql = buildQueryCountString(distinct, tableName, columns, selection, groupBy, having, orderBy, limit);
        return executeQueryCount(sql, selectionArgs);
    }

    /**
     * 执行sql通过 Map<String, Object>限定查询条件查询记录数
     *
     * @param tableName 表名
     * @param whereMap  where条件
     * @return int
     */
    public static int queryCount(String tableName,
                                 Map<String, Object> whereMap) {
        StringBuilder whereClause = new StringBuilder();
        Object[] whereArgs = null;
        if (!CollectionUtils.isEmpty(whereMap)) {
            whereArgs = new Object[whereMap.size()];
            int i = 0;
            for (Map.Entry<String, Object> whereEntry : whereMap.entrySet()) {
                whereClause.append(i == 0 ? "" : " AND ");
                whereClause.append(whereEntry.getKey()).append(" = ? ");
                whereArgs[i] = whereEntry.getValue();
                i++;
            }
        }
        return queryCount(tableName, false, null, whereClause.toString(), whereArgs, null, null, null, null);
    }

    /**
     * 执行查询
     *
     * @param sql      要执行的sql语句
     * @param bindArgs 绑定的参数
     * @return List<Map < String, Object>>结果集对象
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object[] bindArgs) {
        List<Map<String, Object>> datas = new ArrayList<>();
        try {
            datas = jdbcTemplate.queryForList(sql, bindArgs);
        } catch (Exception e) {
            log.error("executeQuery error :{}", e.getMessage());
        }
        return datas;
    }

    /**
     * 执行查询
     *
     * @param sql      要执行的sql语句
     * @param bindArgs 绑定的参数
     * @return List<Map < String, Object>>结果集对象
     */
    public static int executeQueryCount(String sql, Object[] bindArgs) {
        Integer count = 0;
        try {
            count = jdbcTemplate.queryForObject(sql, Integer.class, bindArgs);
        } catch (Exception e) {
            log.error("executeQuery error :{}", e.getMessage());
        }
        return count != null ? count : 0;
    }


    /**
     * Build an SQL query string from the given clauses.
     *
     * @param distinct true if you want each row to be unique, false otherwise.
     * @param tables   The table names to compile the query against.
     * @param columns  A list of which columns to return. Passing null will
     *                 return all columns, which is discouraged to prevent reading
     *                 data from storage that isn't going to be used.
     * @param where    A filter declaring which rows to return, formatted as an SQL
     *                 WHERE clause (excluding the WHERE itself). Passing null will
     *                 return all rows for the given URL.
     * @param groupBy  A filter declaring how to group rows, formatted as an SQL
     *                 GROUP BY clause (excluding the GROUP BY itself). Passing null
     *                 will cause the rows to not be grouped.
     * @param having   A filter declare which row groups to include in the cursor,
     *                 if row grouping is being used, formatted as an SQL HAVING
     *                 clause (excluding the HAVING itself). Passing null will cause
     *                 all row groups to be included, and is required when row
     *                 grouping is not being used.
     * @param orderBy  How to order the rows, formatted as an SQL ORDER BY clause
     *                 (excluding the ORDER BY itself). Passing null will use the
     *                 default sort order, which may be unordered.
     * @param limit    Limits the number of rows returned by the query,
     *                 formatted as LIMIT clause. Passing null denotes no LIMIT clause.
     * @return the SQL query string
     */
    private static String buildQueryString(
            boolean distinct, String tables, String[] columns, String where,
            String groupBy, String having, String orderBy, String limit) {
        if (StringUtils.isEmpty(groupBy) && !StringUtils.isEmpty(having)) {
            throw new IllegalArgumentException(
                    "HAVING clauses are only permitted when using a groupBy clause");
        }
        if (!StringUtils.isEmpty(limit) && !S_LIMIT_PATTERN.matcher(limit).matches()) {
            throw new IllegalArgumentException("invalid LIMIT clauses:" + limit);
        }

        StringBuilder query = new StringBuilder(CommonNumberUtils.INTEGER_120);

        query.append("SELECT ");
        if (distinct) {
            query.append("DISTINCT ");
        }
        if (columns != null && columns.length != 0) {
            appendColumns(query, columns);
        } else {
            query.append(" * ");
        }
        query.append("FROM ");
        query.append(tables);
        appendClause(query, " WHERE ", where);
        appendClause(query, " GROUP BY ", groupBy);
        appendClause(query, " HAVING ", having);
        appendClause(query, " ORDER BY ", orderBy);
        appendClause(query, " LIMIT ", limit);
        return query.toString();
    }


    private static String buildQueryCountString(
            boolean distinct, String tables, String[] columns, String where,
            String groupBy, String having, String orderBy, String limit) {
        if (StringUtils.isEmpty(groupBy) && !StringUtils.isEmpty(having)) {
            throw new IllegalArgumentException(
                    "HAVING clauses are only permitted when using a groupBy clause");
        }
        if (!StringUtils.isEmpty(limit) && !S_LIMIT_PATTERN.matcher(limit).matches()) {
            throw new IllegalArgumentException("invalid LIMIT clauses:" + limit);
        }

        StringBuilder query = new StringBuilder(CommonNumberUtils.INTEGER_120);

        query.append("SELECT ");
        if (distinct) {
            query.append("DISTINCT ");
        }
        query.append(" COUNT(*) ");
        query.append("FROM ");
        query.append(tables);
        appendClause(query, " WHERE ", where);
        appendClause(query, " GROUP BY ", groupBy);
        appendClause(query, " HAVING ", having);
        appendClause(query, " ORDER BY ", orderBy);
        appendClause(query, " LIMIT ", limit);
        return query.toString();
    }

    /**
     * Add the names that are non-null in columns to s, separating
     * them with commas.
     *
     * @param s
     * @param columns
     */
    private static void appendColumns(StringBuilder s, String[] columns) {
        int n = columns.length;
        for (int i = 0; i < n; i++) {
            String column = columns[i];
            if (column != null) {
                if (i > 0) {
                    s.append(", ");
                }
                s.append(column);
            }
        }
        s.append(' ');
    }

    /**
     * addClause
     *
     * @param s      the add StringBuilder
     * @param name   clauseName
     * @param clause clauseSelection
     */
    private static void appendClause(StringBuilder s, String name, String clause) {
        if (!StringUtils.isEmpty(clause)) {
            s.append(name);
            s.append(clause);
        }
    }


    /**
     * the pattern of limit
     */
    private static final Pattern S_LIMIT_PATTERN =
            Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");

    /**
     * After the execution of the complete SQL statement, not necessarily the actual implementation of the SQL statement
     *
     * @param sql      SQL statement
     * @param bindArgs Binding parameters
     * @return Replace? SQL statement executed after the
     */
    private static String getExecSql(String sql, Object[] bindArgs) {
        StringBuilder sb = new StringBuilder(sql);
        if (bindArgs != null && bindArgs.length > 0) {
            int index = 0;
            for (int i = 0; i < bindArgs.length; i++) {
                index = sb.indexOf("?", index);
                sb.replace(index, index + 1, String.valueOf(bindArgs[i]));
            }
        }
        return sb.toString();
    }

    public static  void doTransactionExecuteDemo(){
        internalTransactionTemplate.execute(transactionStatus -> {
            try {
                String tableName = "";
                Map<String, Object> whereMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(whereMap)) {
                    if (DbUtil.queryCount(tableName, whereMap) > 0) {
                        DbUtil.delete(tableName, whereMap);
                    }
                }
                Map<String, Object> valueMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(valueMap)) {
                    DbUtil.insert(tableName, valueMap);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                transactionStatus.setRollbackOnly();
            }
            return null;
        });
    }
}

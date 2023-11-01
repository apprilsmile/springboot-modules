package com.appril.adaptor.impl;

import com.alibaba.fastjson.JSON;
import com.appril.adaptor.OutsideServiceAdaptor;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 * @author zhangyang
 * @date 2023/10/31 18:36
 */
public class ClickHouseOutsideServiceAdaptor implements OutsideServiceAdaptor {
    String selectSql = "SELECT\n" + " \t outside_service_serial_no \n"
            + "FROM\n" + "\t trace_report_external \n"
            + "limit 10";
    private JdbcTemplate clickHouseJdbcTemplate;

    public ClickHouseOutsideServiceAdaptor(JdbcTemplate clickHouseJdbcTemplate) {
        this.clickHouseJdbcTemplate = clickHouseJdbcTemplate;
    }
    @Override
    public String query() {
        return JSON.toJSONString(clickHouseJdbcTemplate.queryForList(selectSql, String.class));
    }
}

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


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.appril.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 数据库JDBC连接工具类
 */
@Slf4j
public class JdbcTemplateUtil {


    private static JdbcTemplate jdbcTemplate;
    private static final Pattern COMPILE = Pattern.compile("\\#\\{([\\w\\.]+)\\}");

    static {
        jdbcTemplate = SpringContextHolder.getBean("internalJdbcTemplate");
    }

    /**
     * queryList
     *   #{}替换
     * @param template
     * @param paramMap
     * @return List<Map < String, Object>>
     */
    public static List<Map<String, Object>> queryList(String template, Map<String, Object> paramMap) {
        String sql = processSql(template, paramMap);
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * queryPageList
     * #{}替换
     * @param template
     * @param paramMap
     * @return PageResult<Map < String, Object>>
     */
    public static PageResult<Map<String, Object>> queryPageList(String template, Map<String, Object> paramMap) {
        String sql = processSql(template, paramMap);
        List<Map<String, Object>> records = jdbcTemplate.queryForList(sql);
        long total = records.size();
        String countSql = sql;
        String limitStr = "limit";
        if (sql.contains(limitStr)) {
            String[] selects = sql.split(limitStr);
            String select = selects[0];
            String fromStr = "from";
            if (select.contains(fromStr)) {
                String[] froms = select.split(fromStr);
                countSql = "select count(*) from " + froms[1];
            }
            total = queryCount(countSql, null);
        }
        return PageResult.page(total, records);
    }

    /**
     * queryCount
     * #{}替换
     * @param template
     * @param paramMap
     * @return int
     */
    public static int queryCount(String template, Map<String, Object> paramMap) {
        String sql = processSql(template, paramMap);
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    /**
     * update
     * #{}替换
     * @param template
     * @param paramMap
     * @return boolean
     */
    public static boolean update(String template, Map<String, Object> paramMap) {
        String sql = processSql(template, paramMap);
        int update = jdbcTemplate.update(sql);
        return update > 0;
    }

    /**
     * insert
     * #{}替换
     * @param template
     * @param paramMap
     * @return boolean
     */
    public static boolean insert(String template, Map<String, Object> paramMap) {
        return update(template, paramMap);
    }

    /**
     * queryObject
     * #{}替换
     * @param template
     * @param paramMap
     * @return Object
     */
    public static Map<String, Object> queryObject(String template, Map<String, Object> paramMap) {
        String sql = processSql(template, paramMap);
        return jdbcTemplate.queryForMap(sql);
    }

    /**
     * processSql
     * #{}替换 a.b.c
     * @param template
     * @param params
     * @return String
     */
    public static String processSql(String template, Map<String, Object> params) {
        Matcher m = COMPILE.matcher(template);
        if (CollectionUtils.isEmpty(params)) {
            return template;
        }
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String param = m.group();
            String paramStr = param.substring(CommonNumberUtils.INT_TWO, param.length() - 1);
            Object value = JSONUtil.getByPath(JSONUtil.parse(params), paramStr);
            String paramValue = StringPool.SINGLE_QUOTE + StringPool.SINGLE_QUOTE;
            if (value != null) {
                if (value instanceof JSONArray) {
                    JSONArray valueArray = JSONUtil.parseArray(value);
                    String valueString = valueArray.stream().map(e -> StringPool.SINGLE_QUOTE + e + StringPool.SINGLE_QUOTE).collect(Collectors.joining(StringPool.COMMA));
                    paramValue = StringPool.LEFT_SQ_BRACKET + valueString + StringPool.RIGHT_SQ_BRACKET;
                } else {
                    paramValue = StringPool.SINGLE_QUOTE + value + StringPool.SINGLE_QUOTE;
                }
            }
            m.appendReplacement(sb, paramValue);
        }
        m.appendTail(sb);
        return sb.toString();
    }


}

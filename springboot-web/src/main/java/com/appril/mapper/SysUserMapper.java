package com.appril.mapper;

import com.appril.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author appril
 * @since 2020-09-25
 */
public interface SysUserMapper extends BaseMapper<SysUser> {


    @Select("<script>\n" +
            "select * from SYS_USER "+
            "<where>\n" +
            "is_del = #{user.isDel}"+
            "</where>\n" +
            "</script>")
    Page<SysUser> getCustomizedPageList(@Param("user")SysUser sysUser, Page<SysUser> userPage);
    /*@Select("<script>\n" +
            "select   t.RYBH from person_info t\n" +
            "<where>\n" +
            "t.upload_status = #{uploadStatus} and t.is_deleted = '2'" +
            "<if test='queryType == 2 '>\n" +
            "<if test='organizations != null and  organizations.size>0'>\n" +
            " and t.JGXX_GAJGJGDM in\n" +
            "<foreach collection=\"organizations\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">\n" +
            "#{item}" +
            "</foreach>\n" +
            "</if>\n" +
            "</if>\n" +
            "<if test='queryType == 3 '>\n" +
            "  and t.user_id = #{userId}\n" +
            "</if>\n" +
            "<if test='queryType == 4 '>\n" +
            "  and t.JGXX_GAJGJGDM in\n" +
            "<foreach collection=\"notifiedBody\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">\n" +
            "#{item}" +
            "</foreach>\n" +
            "</if>\n" +
            "</where>\n" +
            "</script>")*/
}

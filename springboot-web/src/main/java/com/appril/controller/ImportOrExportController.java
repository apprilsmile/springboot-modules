package com.appril.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.appril.excel.dto.SysUserEasyDto;
import com.appril.excel.dto.SysUserPoiDto;
import com.appril.excel.listener.SysUserListener;
import com.appril.service.SysUserService;
import com.appril.utils.ApiResult;
import com.appril.utils.ExcelPoiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyang
 * @date 2020/10/16
 * @description
 **/
@RestController
@RequestMapping("/excelHandle")
public class ImportOrExportController {

    @Autowired
    private SysUserService userService;

    /**
     * poi导入excel
     */
    @PostMapping("/poiImport")
    public ApiResult poiImport(@RequestParam("fileUpName") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResult.isErrMessage("导入文件不能为空");
        }
        List<SysUserPoiDto> sysUserPoiDtos = ExcelPoiUtils.readExcel("", SysUserPoiDto.class, file);
        System.out.println(JSONUtil.toJsonStr(sysUserPoiDtos));
        return ApiResult.isOkMessage("导入成功");
    }

    /**
     * poi导出excel
     */
    @GetMapping("/poiExport")
    public ApiResult poiExport(HttpServletRequest request, HttpServletResponse response) {
        List<SysUserPoiDto> dataList = new ArrayList<>();
        SysUserPoiDto sysUserPoiDto = new SysUserPoiDto();
        sysUserPoiDto.setId(1L);
        sysUserPoiDto.setUsrNm("java");
        sysUserPoiDto.setRlNm("李白");
        sysUserPoiDto.setPwd("lb4564");
        sysUserPoiDto.setMp("49794948");
        sysUserPoiDto.setEMail("libai.@l63.com");
        dataList.add(sysUserPoiDto);

        sysUserPoiDto = new SysUserPoiDto();
        sysUserPoiDto.setId(2L);
        sysUserPoiDto.setUsrNm(".net");
        sysUserPoiDto.setRlNm("杜甫");
        sysUserPoiDto.setPwd("df4564");
        sysUserPoiDto.setMp("87487");
        sysUserPoiDto.setEMail("dufu.@l63.com");
        dataList.add(sysUserPoiDto);

        sysUserPoiDto = new SysUserPoiDto();
        sysUserPoiDto.setId(3L);
        sysUserPoiDto.setUsrNm("php");
        sysUserPoiDto.setRlNm("苏轼");
        sysUserPoiDto.setPwd("ss4564");
        sysUserPoiDto.setMp("497947987948");
        sysUserPoiDto.setEMail("sushi.@l63.com");
        dataList.add(sysUserPoiDto);
        //stream
        // ExcelPoiUtils.writeExcel(response,dataList, sysUserPoiDto.class,ExcelPoiUtils.EXCEL_STREAM);
        //url
        ExcelPoiUtils.writeExcel(response, dataList, SysUserPoiDto.class, ExcelPoiUtils.EXCEL_URL);

        return ApiResult.isOkNoToken("导出成功", "http://localhost:28080/static/ces/default.xlsx");
    }

    /**
     * https://alibaba-easyexcel.github.io/index.html
     * easyexcel导入excel
     */
    @PostMapping("/easyImport")
    public ApiResult easyImport(@RequestParam("fileUpName") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResult.isErrMessage("导入文件不能为空");
        }
        InputStream in = null;
        try {
            in = file.getInputStream();
            EasyExcel.read(in, SysUserEasyDto.class, new SysUserListener(userService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiResult.isOkMessage("导入成功");
    }


    /**
     * easy导出excel
     */
    @GetMapping("/easyExport")
    public ApiResult easyExport(HttpServletRequest request, HttpServletResponse response) {
        List<SysUserEasyDto> dataList = new ArrayList<>();
        SysUserEasyDto sysUserEasyDto = new SysUserEasyDto();
        sysUserEasyDto.setId(1L);
        sysUserEasyDto.setUsrNm("java");
        sysUserEasyDto.setRlNm("李白");
        sysUserEasyDto.setPwd("lb4564");
        sysUserEasyDto.setMp("49794948");
        sysUserEasyDto.setEmail("libai@l63.com");
        dataList.add(sysUserEasyDto);

        sysUserEasyDto = new SysUserEasyDto();
        sysUserEasyDto.setId(2L);
        sysUserEasyDto.setUsrNm(".net");
        sysUserEasyDto.setRlNm("杜甫");
        sysUserEasyDto.setPwd("df4564");
        sysUserEasyDto.setMp("87487");
        sysUserEasyDto.setEmail("dufu@l63.com");
        dataList.add(sysUserEasyDto);

        sysUserEasyDto = new SysUserEasyDto();
        sysUserEasyDto.setId(3L);
        sysUserEasyDto.setUsrNm("php");
        sysUserEasyDto.setRlNm("苏轼");
        sysUserEasyDto.setPwd("ss4564");
        sysUserEasyDto.setMp("497947987948");
        sysUserEasyDto.setEmail("sushi@l63.com");
        dataList.add(sysUserEasyDto);
        //stream
        /*response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("测试", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        try {
            EasyExcel.write(response.getOutputStream(), SysUserEasyDto.class).sheet("模板").doWrite(dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //url
        try {
            EasyExcel.write("E:/ces/easyUser.xlsx", SysUserEasyDto.class).sheet("模板").doWrite(dataList);
        }catch (Exception e){
            return ApiResult.isErrMessage(e.getMessage());
        }


        return ApiResult.isOkNoToken("导出成功", "http://localhost:28080/static/ces/easyUser.xlsx");
    }
}
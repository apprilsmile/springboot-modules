package com.appril.controller;

import com.appril.utils.ApiResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhangyang
 * @date 2020/10/16
 * @description
 **/
@RestController
@RequestMapping("/fileHandle")
public class UploadOrDownloadController {
    /**
     * 上传单个文件
     */
    @ApiOperation(value = "上传单个文件", notes = "文件上传")
    @ApiImplicitParam(name = "fileUpName", value = "文件参数",  paramType = "path", required = true, dataType =  "File")
    @PostMapping("/uploadSingle")
    public ApiResult uploadSingleFile(@RequestParam("fileUpName") MultipartFile file) {//HttpServletRequest request
        /**
         * 这里提到两种接收参数的方式：需要注意一点区别 下面的上传多个文件也是同理
         * 直接用 @RequestParam("fileUpName") MultipartFile file
         *      接收文件参数 如果前端没有选择文件 请求后台报错，Current request is not a multipart request
         *      前端显示的是500错误
         * 替换为  HttpServletRequest request
         *        MultipartFile file = ((MultipartHttpServletRequest)request).getFile("fileUpName"); 获取文件参数
         *        如果没有选择文件 后台报错 cannot be cast to org.springframework.web.multipart.MultipartHttpServletRequest
         *        不过方便在该行代码做异常处理等返回设置对应的code msg等统一形式。
         */
        //MultipartFile file = ((MultipartHttpServletRequest)request).getFile("fileUpName");

        if (file.isEmpty()) {
            return ApiResult.isErrMessage("上传文件不能为空");
        }
        //获取文件名，带后缀
        String fileName = file.getOriginalFilename();
        /*如果限制文件上传类型例如：该接口限制只能上传图片，则增加图片格式校验*/
        List<String> imageType = Stream.of("jpg", "jpeg", "png", "bmp", "gif").collect(Collectors.toList());
        // 获取文件的后缀格式
        String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!imageType.contains(fileSuffix)) return ApiResult.isErrMessage("非法文件");

        //加个UUID拼接，尽量避免文件名称重复
        String path = "E:" + File.separator + "ces" + File.separator + UUID.randomUUID().toString().replace("-", "") + "_" + fileName;
        File dest = new File(path);
        //判断文件是否已经存在，如果可以直接覆盖则忽略 或 再重新生成该文件的时间戳文件直到不重复
        if (dest.exists()) return ApiResult.isErrMessage("上传的文件名已存在");
        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) dest.getParentFile().mkdir();
        //保存文件
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResult.isErrMessage("上传文件异常");
        }
        return ApiResult.isOkMessage("上传成功");
    }

    /**
     * 上传多个文件
     */
    @PostMapping("/uploadMore")
    public ApiResult uploadMoreFile(@RequestParam("fileNames") List<MultipartFile> files) {//HttpServletRequest request
        //List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("fileName");
        if (Objects.isNull(files) || files.size() < 1) return ApiResult.isErrMessage("文件不能为空");
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            //加个UUID拼接，尽量避免文件名称重复
            String path = "E:" + File.separator + "ces" + File.separator + UUID.randomUUID().toString().replace("-", "") + "_" + fileName;
            File dest = new File(path);
            //判断文件是否已经存在，如果可以直接覆盖则忽略 或 再重新生成该文件的时间戳文件直到不重复
            //if (dest.exists()) return "上传的文件名已存在";
            //判断文件父目录是否存在
            if (!dest.getParentFile().exists()) dest.getParentFile().mkdir();
            //保存文件
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
                return ApiResult.isErrMessage("上传文件异常");
            }
        }
        return ApiResult.isOkMessage("上传成功 !");
    }

    /**
     * 下载文件
     */
    @RequestMapping("/downloadFile")
    public ApiResult downloadFile(HttpServletResponse response, @RequestParam("fileName") String filePathName) {
        File file = new File(filePathName);
        if (!file.exists()) return ApiResult.isErrMessage("文件不存在");
        try (
                InputStream inStream = new FileInputStream(filePathName);
                OutputStream os = response.getOutputStream();
        ) {
            response.reset();
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(filePathName, "UTF-8"));
            byte[] buff = new byte[1024];
            int len = -1;
            while ((len = inStream.read(buff)) > 0) {
                os.write(buff, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.isErrMessage("下载文件异常");
        }
        return ApiResult.isOkMessage("下载成功");
    }
}

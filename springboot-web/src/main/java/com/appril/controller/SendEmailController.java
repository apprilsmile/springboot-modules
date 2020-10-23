package com.appril.controller;

import cn.hutool.core.util.RandomUtil;
import com.appril.utils.ApiResult;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author zhangyang
 * @date 2020/10/22
 * @description 开启POP3/SMTP服务 获取授权码
 **/
@RestController
@RequestMapping("/sendEmail")
public class SendEmailController {

    public static final String  EMAIL_SEND_FROM = "1262169627@qq.com";
    public static final String  EMAIL_SEND_TO = "zhangyang@shiqiao.com";
    public static final String  EMAIL_SEND_SUBJECT = "JavaMail 测试";
    public static final String  EMAIL_SEND_FILE_PATH = "C:\\Users\\Administrator\\Desktop\\试用期转正总结.doc";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 简单文本邮
     */
    @PostMapping ("/simpleMsg")
    public ApiResult simpleMsg(){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(EMAIL_SEND_FROM);
            mailMessage.setTo(EMAIL_SEND_TO);
            mailMessage.setCc("781616300@qq.com");//抄送
            mailMessage.setSubject("加油打工人");
            mailMessage.setText("打工累吗？累。但是我不能哭，因为骑电动车的时候擦眼泪不安全。");
            mailSender.send(mailMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.isErrMessage("发送失败");
        }
        return ApiResult.isOkMessage("发送成功");
    }

    /**
     * 附带html邮件
     */
    @PostMapping ("/htmlMsg")
    public ApiResult htmlMsg(){
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            //是否发送的邮件是富文本（附件，图片，html等）
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(EMAIL_SEND_FROM);
            messageHelper.setTo(EMAIL_SEND_TO);
            messageHelper.setSubject("加油打工人Html");
            String htmlString = "<font color=\"blue\">打工累吗？累。但是我不能哭，因为骑电动车的时候擦眼泪不安全。</font>";
            messageHelper.setText(htmlString,true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.isErrMessage("发送失败");
        }
        return ApiResult.isOkMessage("发送成功");
    }


    /**
     * 带附件邮件
     */
    @PostMapping ("/withFileMsg")
    public ApiResult withFileMsg(){
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            //是否发送的邮件是富文本（附件，图片，html等）
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(EMAIL_SEND_FROM);// 发送邮箱
            messageHelper.setTo(EMAIL_SEND_TO);// 接收邮箱
//            messageHelper.setSentDate(new Date());// 发送时间
            messageHelper.setSubject(EMAIL_SEND_SUBJECT);
            File file = new File(EMAIL_SEND_FILE_PATH);
            String fileName = EMAIL_SEND_FILE_PATH.substring(EMAIL_SEND_FILE_PATH.lastIndexOf(File.separator));

            messageHelper.setText("fileName",true);// 邮件内容
            messageHelper.addAttachment(fileName,file);
            FileInputStream fileInputStream = new FileInputStream("C:/Users/Administrator/Desktop/东方时尚.png");
            ByteArrayResource attachment = new ByteArrayResource(IOUtils.toByteArray(fileInputStream));
            messageHelper.addAttachment("static/东方时尚.png", attachment,"application/png");
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.isErrMessage("发送失败");
        }
        return ApiResult.isOkMessage("发送成功");
    }


    /**
     * 带静态资源的邮件
     */
    @PostMapping ("/inlineMsg")
    public ApiResult inlineMsg(){
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            //是否发送的邮件是富文本（附件，图片，html等）
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(EMAIL_SEND_FROM);
            messageHelper.setTo(EMAIL_SEND_TO);
            messageHelper.setSubject("东方时尚驾校");
            messageHelper.setText("<html><body>东方时尚：<img src='cid:img'/></body></html>", true); // 内容
            // 传入附件
            FileSystemResource file = new FileSystemResource(new File("E:\\apprilsmile\\springboot-modules\\springboot-web\\src\\main\\resources\\static\\dfss.png"));
            messageHelper.addInline("img", file);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.isErrMessage("发送失败");
        }
        return ApiResult.isOkMessage("发送成功");
    }
    @PostMapping ("/templateMsg")
    public ApiResult templateMsg(HttpServletRequest request, HttpServletResponse response){
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            //是否发送的邮件是富文本（附件，图片，html等）
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(EMAIL_SEND_FROM);
            messageHelper.setTo(EMAIL_SEND_TO);
            messageHelper.setSubject("模板邮件");
            Context context = new Context();
            context.setVariable("code", RandomUtil.randomString(5));
            String template = templateEngine.process("emailTemplate.html", context);
            messageHelper.setText(template, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.isErrMessage("发送失败");
        }
        return ApiResult.isOkMessage("发送成功");
    }
}

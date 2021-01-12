package com.cxyzj.cxyzjback.Utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @Package com.cxyzj.cxyzjback.Utils
 * @Author Yaser
 * @Date 2018/09/19 11:27
 * @Description: 工具类
 */

@Slf4j
@Component
public class Utils {
    @Autowired
    JavaMailSenderImpl javaMailSender;

    /**
     * @param str     传入的手机或邮箱
     * @param isPhone 是否是手机
     * @return 返回对邮箱或手机进行掩码操作后的邮箱或手机
     */
    public String maskEmailPhone(String str, boolean isPhone) {
        StringBuilder sb = new StringBuilder(str);
        int end;
        int start = 3;
        if (isPhone) {
            end = str.length() - 2;
        } else {
            end = str.indexOf('@');
        }
        if (end < start) {
            start = end;
        }
        StringBuilder maskCode = new StringBuilder();
        for (int i = start; i < end; i++) {
            maskCode.append("*");
        }
        sb.replace(start, end, maskCode.toString());
        return sb.toString();
    }


    /**
     * 随机字符组成的邮箱验证码
     */
    public String mailCode() {
        char[] ch = "abcdefghijkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXY3456789".toCharArray();

        Random r = new Random();
        int len = ch.length;
        int index;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            index = r.nextInt(len);
            sb.append(ch[index]);
        }
        return sb.toString();
    }

    /**
     * 随机六位数的手机验证码
     */
    public String phoneCode() {
        char[] ch = "0123456789".toCharArray();

        Random r = new Random();
        int len = ch.length;
        int index;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            index = r.nextInt(len);
            sb.append(ch[index]);
        }
        return sb.toString();  //每次调用生成一次六位数的随机数
    }

    /**
     * 手机验证码发送
     *
     * @param phone 手机号
     * @param code  验证码
     * @return sendSuccess||sendFailure
     */
    public boolean phoneSend(String phone, String code)  {
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        final String product = "Dysmsapi";
        final String domain = "dysmsapi.aliyuncs.com";

        final String accessKeyId = "";
        final String accessKeySecret = "";
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            e.printStackTrace();
        }

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName("");
        request.setTemplateCode("");
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        request.setOutId("yourOutId");

        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }

        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功
            log.info("phone code is ------------------------ " + code);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 邮箱验证码发送  (支持qq邮箱)
     *
     * @param email 邮箱
     * @param code  验证码
     * @return sendSuccess||sendFailure
     */
    public boolean mailSend(String email, String code, String text) {
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "utf-8");
            helper.setFrom("");// 设置发件人
            helper.setTo(email);// 设置收件人
            helper.setSubject("验证码");// 设置主题
            helper.setText(text + "\n验证码:【" + code + "】");// 邮件体
            javaMailSender.send(mailMessage);// 发送邮件
            log.info("邮件发送成功...");
            log.info("-----验证码是：" + code);
            return true;
        } catch (Exception e) {
            log.error("邮件发送发生异常:" + e.getMessage(), e);
            return false;
        }
    }

    @Bean(name = "javaMailSender")
    public JavaMailSenderImpl javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.qq.com");
        javaMailSender.setPort(465);
        javaMailSender.setUsername("");
        javaMailSender.setPassword("");
        Properties properties = new Properties();
        properties.setProperty("mail.host", "smtp.qq.com");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");

        javaMailSender.setJavaMailProperties(properties);
        return javaMailSender;
    }

}

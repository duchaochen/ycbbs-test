package com.ycbbs.crud.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMail {

    /**
     * 指定发送邮件的主机为qq邮箱主机
     */
    private final static String HOST_MAIL = "smtp.qq.com";
    /**
     * 邮件服务器密码,需要qq邮箱后台生成的授权码
     */
    private String password;
    /**
     * 发件人电子邮箱,也是邮件服务器用户名
     */
    private String userNameFromMail;


    public String getUserNameFromMail() {
        return userNameFromMail;
    }

    public void setUserNameFromMail(String userNameFromMail) {
        this.userNameFromMail = userNameFromMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SendMail() {
        this.userNameFromMail = "";
    }

    /**
     * 获取session
     * @return
     */
    private Session getSession() {
        Properties properties = this.getProperties();
        // 获取默认session对象
        return Session.getDefaultInstance(properties,new Authenticator(){
            @Override
            public PasswordAuthentication getPasswordAuthentication()
            {
                //发件人邮件用户名、授权码
                return new PasswordAuthentication(getUserNameFromMail(),
                        getPassword());
            }
        });
    }
    //获取设置对象
    private Properties getProperties() {
        // 获取系统属性
        Properties properties = System.getProperties();
        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", HOST_MAIL);
        properties.put("mail.smtp.auth", "true");
        return properties;
    }

    /**
     * 收件人的邮箱
     * @param toMail 收件人邮箱
     * @param head  邮件头部([猿程社区] 邮箱激活通知)
     * @param msg   要发送的消息
     */
    public void sendMessage(String toMail,String head,String msg){
        Session session = getSession();
        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(this.getUserNameFromMail()));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toMail));

            // Set Subject: 头部头字段
            message.setSubject(head);
            // 设置消息体
//            message.setText("This is actual message");
            //注册成功之后发邮件
            message.setContent(msg,"text/html;charset=UTF-8");

            // 发送消息
            Transport.send(message);
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }


}

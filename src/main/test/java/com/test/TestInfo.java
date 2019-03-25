package com.test;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.UUID;

public class TestInfo {

    @Test
    public void testHashMd5() {
        String salt = "qwerty";
        Md5Hash md5Hash = new Md5Hash("111111",salt,1);
        String password = md5Hash.toString();
        System.out.println(password.equalsIgnoreCase("f3694f162729b7d0254c6e40260bf15c"));
    }

    @Test
    public void testuuid() {
        String s = UUID.randomUUID().toString();
        System.out.println(s);
    }


    @Test
    public void mail() {

        String ativeUrl = "http://localhost:8081/active?d=1&m=123456";
        // 收件人电子邮箱
        String to = "113214862@qq.com";

        // 发件人电子邮箱
        String from = "939705214@qq.com";

        // 指定发送邮件的主机为 localhost
        String host = "smtp.qq.com";




        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("939705214@qq.com",
                        "gtereezqonxnbedc"); //发件人邮件用户名、授权码
            }
        });
        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: 头部头字段
            message.setSubject("[猿程社区] 邮箱激活通知");
            // 设置消息体
//            message.setText("This is actual message");
            // 发送 HTML 消息, 可以插入html标签
            StringBuilder sb = new StringBuilder();
            sb.append("<style>" +
                    ".subject{\n" +
                    "        width:550px;\n" +
                    "        padding: 30px 30px 30px 100px;\n" +
                    "        margin: 0 auto;\n" +
                    "        background: rgb(235,237,240);\n" +
                    "        border-radius: 10px;\n" +
                    "    }\n" +
                    "    p{\n" +
                    "        font-size: 14px;\n" +
                    "    }\n" +
                    "    a{\n" +
                    "        font-size: 18px;\n" +
                    "    }");
            sb.append("</style>\n" +
                    "<div style=\" padding-top: 50px; padding-bottom:200px;\">\n" +
                    "    <div class=\"subject\">\n" +
                    "        <h4>尊敬的939705214@qq.com，您好,</h4>\n" +
                    "        <p style=\"padding-left: 5px;\">感谢您使用猿程社区服务</p>\n" +
                    "        <p style=\"padding-left: 5px;\">请点击以下链接进行邮箱验证，以便开始使用您的猿程社区账户：</p>\n" +
                    "        <p style=\"padding-left: 5px;\">\n" +
                    "            <a href='"+ativeUrl+"'>http://ycbbs.com/active?d=1&m=123456</a>\n" +
                    "            </br>\n" +
                    "            如果您并未申请猿程社区服务账户，可能是其他用户误输入了您的邮箱地址。请忽略此邮件，或者联系我们。\n" +
                    "        </p>\n" +
                    "    </div>\n" +
                    "</div>");
            message.setContent(sb.toString(),"text/html;charset=UTF-8");

            // 发送消息
            Transport.send(message);
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    /**

     * 将对象转换字符串,用来处理request.getParameterMap()中的值

     * @param obj

     * @return

     */

    private String objToString(Object obj) {
        if (obj != null) {
            //判断对象是否是数组
            if (obj.getClass().isArray()) {
                String[] value = (String[]) obj;
                return correctDisplay(value[0]);
            }
            return correctDisplay(obj.toString());
        }
        return "";
    }
    /**
     * 乱码处理
     * @param temp
     * @return
     */
    private String correctDisplay(String temp){
        try {
            temp = new String(temp.getBytes("ISO-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return temp;
    }
}

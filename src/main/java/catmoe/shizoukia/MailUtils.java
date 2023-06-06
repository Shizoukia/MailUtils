// Decompiled with: CFR 0.152
// Class Version: 17
package catmoe.shizoukia.util;

import catmoe.shizoukia.LuckPermsAlert;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class MailUtils {

    private static final String EMAIL_USERNAME = "catmoe_team@163.com"; // 发送方邮箱
    private static final String EMAIL_PASSWORD = "ROMJIBIGSXZHUDGU"; // 发送方邮箱密码

    public static void sendAlert(String subject, String title, String content) {
        // 配置 SMTP 服务器属性
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "false");
        properties.put("mail.smtp.host", "smtp.163.com"); // SMTP 服务器地址
        properties.put("mail.smtp.port", "25"); // SMTP 服务器端口号

        // 创建会话
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            // 创建邮件消息
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME, "YozutkixAI"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("XYBLOfficial@outlook.com")); // 收件人邮箱
            message.setSubject(subject);
            message.setText("[!] " + title + " [!]" + "\n\n" + content + "\n\n" + "CatMoe Contorl System");

            // 发送邮件
            Transport.send(message);
            System.out.println("邮件发送成功！");
        } catch (MessagingException | UnsupportedEncodingException e) {
            System.out.println("邮件发送失败：" + e.getMessage());
        }
    }


    public static void writeHistory(String name, String uuid, String context) {
        try (FileChannel fc = FileChannel.open(Path.of((String)LuckPermsAlert.getInstance().getDataFolder().getPath(), (String[])new String[]{"history.csv"}), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.APPEND);){
            fc.write(ByteBuffer.wrap(String.format("\"%s\",\"%s\",\"%s\",\"%s\"\n", name, uuid, context, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).getBytes(StandardCharsets.UTF_8)));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

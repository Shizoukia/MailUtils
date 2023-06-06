// Decompiled with: CFR 0.152
// Class Version: 17
package catmoe.shizoukia.MailUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MailUtils {
/*弃用
    private static final String EMAIL_USERNAME = "null"; // 发送方邮箱
    private static final String EMAIL_PASSWORD = "null"; // 发送方邮箱密码
    */
    private ExecutorService executorService;
    private static Properties properties;
    public static void loadConfig(String filePath) {
        properties = new Properties();
        File configFile = new File(filePath);

        if (configFile.exists()) {
            try (FileInputStream inputStream = new FileInputStream(configFile)) {
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 生成默认的配置文件
            generateDefaultConfig(configFile);
        }
    }


    private static void generateDefaultConfig(File configFile) {
        try (OutputStream output = new FileOutputStream(configFile)) {
            properties.setProperty("config.version", "1.0");
            properties.setProperty("email.username", "your_email@example.com");
            properties.setProperty("email.password", "your_password");
            properties.setProperty("email.smtp.host", "smtp.example.com");
            properties.setProperty("email.smtp.port", "587");
            properties.setProperty("email.recipient", "recipient@example.com");
            properties.setProperty("email.sandername", "Example");


            properties.store(output, "MailUtils-API");
            properties.store(output, "Mail Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMail(String subject, String title, String content) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            // 配置 SMTP 服务器属性
            Properties smtpProperties = new Properties();
            smtpProperties.put("mail.smtp.auth", "true");
            smtpProperties.put("mail.smtp.starttls.enable", "true");
            smtpProperties.put("mail.smtp.host", properties.getProperty("email.smtp.host"));
            smtpProperties.put("mail.smtp.port", properties.getProperty("email.smtp.port"));

            // 创建会话
            Session session = Session.getInstance(smtpProperties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            properties.getProperty("email.username"),
                            properties.getProperty("email.password")
                    );
                }
            });

            try {
                // 创建邮件消息
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(properties.getProperty("email.username"), properties.getProperty("email.sendername")));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(properties.getProperty("email.recipient")));
                message.setSubject(subject);
                message.setText(title + "\n\n" + content);


//                message.setText("[!] " + title + " [!]" + "\n\n" + content + "\n\n" + "CatMoe Contorl System");

            // 发送邮件
            Transport.send(message);
            System.out.println("邮件发送成功！");
        } catch (MessagingException | UnsupportedEncodingException e) {
            System.out.println("邮件发送失败：" + e.getMessage());
        } finally {
            executorService.shutdown();
        }
    });


/*    public static void writeHistory(String name, String uuid, String context) {
        try (FileChannel fc = FileChannel.open(Path.of((String)LuckPermsAlert.getInstance().getDataFolder().getPath(), (String[])new String[]{"history.csv"}), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.APPEND);){
            fc.write(ByteBuffer.wrap(String.format("\"%s\",\"%s\",\"%s\",\"%s\"\n", name, uuid, context, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).getBytes(StandardCharsets.UTF_8)));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}}

package com.example.store;

import android.os.AsyncTask;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendEmail extends AsyncTask<Void, Void, Void> {

    private String to;
    private String code;
    public SendEmail() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String from = "phuongtrnh02122002@gmail.com";
        // Mật khẩu email người gửi
        String password = "scimoqwvcaehvbnh";

        // Cấu hình thông tin host email server
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        // Xác thực thông tin đăng nhập
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });

        try {
            // Tạo đối tượng MimeMessage
            MimeMessage message = new MimeMessage(session);

            // Thiết lập thông tin người gửi, người nhận và tiêu đề email
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.to));
            message.setSubject("This is your code for forgot password");

            // Thiết lập nội dung email
            message.setText("Your code: " + this.code);

            // Gửi email
            Transport.send(message);

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
        return null;
    }

    public SendEmail(String to, String code) {
        this.to = to;
        this.code = code;
    }
}
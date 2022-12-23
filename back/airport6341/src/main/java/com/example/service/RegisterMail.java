package com.example.service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class RegisterMail implements MailServiceInter {

    @Autowired
    JavaMailSender emailSender; // MailConfig에서 등록해둔 Bean을 autowired하여 사용하기

    private String ePw; // 사용자가 메일로 받을 인증번호

    // 메일 내용 작성 
    @Override
    public MimeMessage creatMessage(String to) throws MessagingException, UnsupportedEncodingException {
    
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to); // 메일 받을 사용자
        message.setSubject("[Fligent] 비밀번호 변경을 위한 이메일 인증코드 입니다"); // 이메일 제목

        String msgg = "";
        msgg += "<div align='center' style='width:1300px'>";
        // msgg += "<p><img src=http://127.0.0.1:8080/fligent/image/mail_header.png/><p>"; // header image
        msgg += "<p><img src='https://postfiles.pstatic.net/MjAyMjExMjhfNTIg/MDAxNjY5NjA0MzIwODM2.9-oex6OmJW2ICzggXeRV3_6HylLCHnUoxIXGv4rHfk4g.bFld2jYhlkf6rmBWQWzS6fGB3eGW6TpqOo3uQLh_KKkg.PNG.um9623/mail_header.png?type=w966'/><p>";
        msgg += "<div style= width:'1300px'>";
        msgg += "<h2>안녕하세요. 공항정보 플랫폼 Fligent 입니다.</h1>";
        msgg += "<h3 style='color:darkblue'>회원가입 인증코드 입니다</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "<strong>" + ePw + "</strong></div><br/>" ; // 메일에 인증번호 ePw 넣기
        msgg += "</div>";
        msgg += "<div align='center' style='width:1300px'>";
        msgg += "<p><img src='https://postfiles.pstatic.net/MjAyMjExMjhfNiAg/MDAxNjY5NjA0MTQxMDQ4.WISHXivd_cfMh98y2KcVE1IpR0kXJ86adOwZjucrElAg.MYVvB-2B9Apzm1fbcDKMcAxOHInkAXGk8Zx8fKm33mYg.PNG.um9623/mail_footer.png?type=w966'/><p>";
        msgg += "</div>";
        msgg += "</div>";        
        // msgg += "<img src=../resources/static/image/emailheader.jpg />"; // footer image

        message.setText(msgg, "utf-8", "html"); // 메일 내용, charset타입, subtype
        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom(new InternetAddress("mah95@naver.com", "Fligent_Admin"));
        return message;
    }

    // 랜덤 인증코드 생성
    @Override
    public String createKey() {
            int leftLimit = 48; // numeral '0'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 10;
            Random random = new Random();
            String key = random.ints(leftLimit, rightLimit + 1)
                                           .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                                           .limit(targetStringLength)
                                           .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                           .toString();
            // System.out.println("생성된 랜덤 인증코드"+ key);
            return key;
    }

    // 메일 발송
    // sendSimpleMessage 의 매개변수 to는 이메일 주소가 되고,
    // MimeMessage 객체 안에 내가 전송할 메일의 내용을 담는다
    // bean으로 등록해둔 javaMail 객체를 사용하여 이메일을 발송한다
    @Override
    public String sendSimpleMessage(String to) throws Exception {

        ePw = createKey(); // 랜덤 인증코드 생성

        MimeMessage message = creatMessage(to); // "to" 로 메일 발송


        try { // 예외처리
            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }

        return ePw; // 메일로 사용자에게 보낸 인증코드를 서버로 반환! 인증코드 일치여부를 확인하기 위함 
    }
    
}

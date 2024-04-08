package kr.vintly.util.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.vintly.util.mail.model.MailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.beans.factory.annotation.Value;

import org.thymeleaf.context.Context;
import java.io.IOException;
import java.util.HashMap;

@Service
public class MailService {
    private JavaMailSender mailSender;

    private SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Autowired
    public MailService(SpringTemplateEngine springTemplateEngine, JavaMailSender javaMailSender){
        this.templateEngine = springTemplateEngine;
        this.mailSender = javaMailSender;
    }

    public void mailSend(MailDTO mailDTO, HashMap<String, String> values, String htmlName) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        //메일 제목 설정
        helper.setSubject(mailDTO.getTitle());

        // 발신자 설정
        helper.setFrom(fromAddress);

        //수신자 설정
        helper.setTo(mailDTO.getAddress());

        //템플릿에 전달할 데이터 설정
        Context context = new Context();
        values.forEach((key, value)->{
            context.setVariable(key, value);
        });

        //메일 내용 설정 : 템플릿 프로세스
        String html = templateEngine.process(htmlName,context);
        helper.setText(html,true);

        mailSender.send(message);
    }
}

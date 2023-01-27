package globant.team.seven.imageprocessingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

@Component
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmailAttachment(String subject, String uuidRequeriment, File fileToUpload, List<String> sendTo)  {
        try {
            if (sendTo != null && !sendTo.isEmpty()) {
                String arrayTo[] = new String[sendTo.size()];
                for (int i = 0; i < sendTo.size(); i++) {
                    arrayTo[i] = sendTo.get(i);
                }
                MimeMessage msg = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(msg, true);
                helper.setTo(arrayTo);
                helper.setSubject(subject);
                String html = "<h1>Image information</h1>" +
                        "<br/>" +
                        "<h3>UUID: </h3>" + uuidRequeriment +
                        "<br/>" +
                        "<h3>Status: </h3> Completed";
                helper.setText(html, true);
                //Aqui convertir MIME
                FileSystemResource file = new FileSystemResource(fileToUpload);
                helper.addAttachment(uuidRequeriment + ".jpg", file);
                javaMailSender.send(msg);
            } else {
                throw new NullPointerException("List empty");
            }
        } catch (MessagingException me){
            System.out.println(me);
        }
    }

}

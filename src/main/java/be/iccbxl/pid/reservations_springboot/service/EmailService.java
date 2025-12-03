package be.iccbxl.pid.reservations_springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailService.class);

	private final JavaMailSender mailSender;

	@Value("${spring.application.frontend-url}")
	private String frontendUrl;

	@Value("${app.mail.from:no-reply@example.com}")
	private String from; // optionnel mais recommandé

	public void sendPasswordResetMail(String to, String token) {
		String url = frontendUrl + "/reset-password?token=" + token;

		String html = """
				<p>Vous avez demandé la réinitialisation du mot de passe.</p>
				<p><a href="%s">Cliquez ici pour réinitialiser votre mot de passe</a></p>
				<p>Si le bouton ne fonctionne pas, copiez le lien ci-dessous dans votre navigateur.<br/>
				<code>%s</code></p>
				<p>Ce lien expire dans 1 heure. Si vous n'avez pas demandé cela, ignorez cet e-mail.</p>
				""".formatted(url, url);

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
			helper.setTo(to);
			helper.setFrom(from);
			helper.setSubject("Réinitialisation du mot de passe");
			helper.setText(html, true); // true pour HTML

			mailSender.send(message);

			if (log.isDebugEnabled()) {
				String masked = token.length() > 6 ? token.substring(0, 6) + "****" : "****";
				log.debug("Reset link envoyé à {}. token={}", to, masked);
			}
		} catch (MailException | MessagingException e) {
			log.error("Échec d’envoi de l’e-mail de reset. to={}, cause={}", to, e.getMessage(), e);
			throw new IllegalStateException("Envoi d’e-mail impossible pour le moment");
		}
	}
}

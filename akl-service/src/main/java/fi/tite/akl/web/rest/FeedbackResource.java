package fi.tite.akl.web.rest;

import com.github.mkopylec.recaptcha.validation.RecaptchaValidationException;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;
import fi.tite.akl.dto.FeedbackDto;
import fi.tite.akl.service.MailService;
import fi.tite.akl.web.rest.errors.CustomParameterizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("api/feedback")
public class FeedbackResource {

    @Autowired
    private RecaptchaValidator recaptchaValidator;

    @Inject
    private MailService mailService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> addFeedback(@Valid @RequestBody FeedbackDto feedbackDto, HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        try {
            ValidationResult result = recaptchaValidator.validate(feedbackDto.getRecaptcha(), remoteAddr);
            if (result.isSuccess()) {
                mailService.sendEmail("akl@t3g.fi", "Feedback from " + feedbackDto.getSender(),
                        feedbackDto.getMessage(), false, false);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                throw new CustomParameterizedException("Invalid reCAPTCHA");
            }
        } catch (RecaptchaValidationException ex) {
            log.error(ex.getLocalizedMessage());
            throw new CustomParameterizedException("Invalid reCAPTCHA");
        }
    }
}

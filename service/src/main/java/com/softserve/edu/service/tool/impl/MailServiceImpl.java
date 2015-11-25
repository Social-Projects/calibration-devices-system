package com.softserve.edu.service.tool.impl;

import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.tool.MailService;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.ui.velocity.VelocityEngineUtils.mergeTemplateIntoString;

@Service
@PropertySource("classpath:properties/mail.properties")
public class MailServiceImpl implements MailService {

    @Autowired
    Environment env;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Value("${mail.credentials.username}")
    private String userName;

    @Value("${site.protocol}")
    private String protocol;

    Logger logger = Logger.getLogger(MailServiceImpl.class);


    @Async
    public void sendMail(String to, String userName, String clientCode, String providerName, String deviceType) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(to);
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Calibration devices system"));
                String domain = null;
                try {
                    domain = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException ue) {
                    logger.error("Cannot get host address", ue);
                }

                SimpleDateFormat form = new SimpleDateFormat("dd-MM-yyyy");
                String date = form.format(new Date());
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("name", userName);
                templateVariables.put("protocol", protocol);
                templateVariables.put("domain", domain);
                templateVariables.put("applicationId", clientCode);
                templateVariables.put("providerName", providerName);
                templateVariables.put("deviceType", deviceType);
                templateVariables.put("date", date);
                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates/mailTemplate.vm", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");
            }
        };
        mailSender.send(preparator);
    }


    @Async
    public void sendNewPasswordMail(String employeeEmail, String employeeName, String newPassword) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws MessagingException, UnsupportedEncodingException {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(employeeEmail);
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Calibration devices system"));
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("name", employeeName);
                templateVariables.put("password", newPassword);
                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates" + "/mailNewPasswordEmployee.vm", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");
            }
        };
        mailSender.send(preparator);
    }

    @Async
    public void sendAdminNewPasswordMail(String employeeEmail, String employeeName, String newPassword) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws MessagingException, UnsupportedEncodingException {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(employeeEmail);
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Calibration devices system"));
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("name", employeeName);
                templateVariables.put("password", newPassword);
                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates" + "/createdAdminPassword", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");
            }
        };
        mailSender.send(preparator);
    }

    @Async
    public void sendOrganizationPasswordMail(String organizationMail, String organizationName, String username, String password) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws UnsupportedEncodingException, MessagingException {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(organizationMail);
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Calibration devices system"));
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("name", organizationName);
                templateVariables.put("username", username);
                templateVariables.put("password", password);
                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates" + "/organizationAdminMail.vm", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");
            }
        };
        mailSender.send(preparator);
    }


    @Async
    public void sendRejectMail(String to, String userName, String verificationId, String msg, String deviceType) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(to);
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Calibration devices system"));
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("name", userName);
                templateVariables.put("verificationId", verificationId);
                templateVariables.put("deviceType", deviceType);
                templateVariables.put("message", msg);
                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates" + "/rejectVerification.vm", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");

            }
        };
        mailSender.send(preparator);
    }

    /** Notifies (sends mail to) customer about assignment of an employee to the verification*/
    @Async
    public void sendAcceptMail(String to, String verificationId, String deviceType) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(to);
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Calibration devices system"));
                String domain = null;
                try {
                    domain = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException ue) {
                    logger.error("Cannot get host address", ue);
                }
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("verificationId", verificationId);
                templateVariables.put("deviceType", deviceType);
                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates" + "/accepted.vm", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");
            }
        };
        mailSender.send(preparator);
    }

    /**
     * Notifies (sends mail to) customer about changed status of  the verification
     */
    public void sendPassedTestMail(String to, String verificationId, String status) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(to);
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Calibration devices system"));
                String domain = null;
                try {
                    domain = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException ue) {
                    logger.error("Cannot get host address", ue);
                }
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("verificationId", verificationId);
                templateVariables.put("status", status);
                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates" + "/changedStatus.vm", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");
            }
        };
        mailSender.send(preparator);
    }


    /**
     * Send email from client (for example to SYS_ADMIN)
     * @param to
     * @param from
     * @param userFirstName
     * @param userLastName
     * @param verificationId
     * @param msg
     */
    @Async
    public void sendClientMail(String to, String from, String userFirstName, String userLastName, String verificationId, String msg) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);

                message.setTo(to);
                message.setFrom(new InternetAddress(from));
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("firstName", userFirstName);
                templateVariables.put("lastName", userLastName);
                templateVariables.put("mailAddress", from);
                templateVariables.put("message", msg);
                templateVariables.put("applicationId", verificationId);
                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates" + "/clientMail.vm", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");

            }
        };
        mailSender.send(preparator);
    }

    @Async
    public void sendTimeExceededMail(String verificationId, int processTimeExceeding, int maxProcessTime, String mailTo) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(mailTo);
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Calibration devices system"));
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("processTimeExceeding", processTimeExceeding);
                templateVariables.put("verificationId", verificationId);
                templateVariables.put("maxProcessTime", maxProcessTime);
                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates" + "/processTimeExceeded.vm", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");
            }
        };
        mailSender.send(preparator);
    }

    @Async
    /**
     * When information about organization is changed,
     * this method sends the dataset containing all data
     * to the email of the organization
     * @param organization Organization, whose data was changed
     * @param admin Admin of the organization
     *
     * */
    public  void sendOrganizationChanges (Organization organization, User admin){
        MimeMessagePreparator preparator = new MimeMessagePreparator() {

            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(organization.getEmail());
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Calibration devices system"));
                String domain = null;
                try {
                    domain = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException ue) {
                    logger.error("Cannot get host address", ue);
                }
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("name", organization.getName());
                templateVariables.put("email", organization.getEmail());
                templateVariables.put("phone", organization.getPhone());
                templateVariables.put("types", organization.getOrganizationTypes());
                templateVariables.put("employeesCapacity", organization.getEmployeesCapacity());
                templateVariables.put("maxProcessTime", organization.getMaxProcessTime());
                templateVariables.put("region", organization.getAddress().getRegion());
                templateVariables.put("locality", organization.getAddress().getLocality());
                templateVariables.put("district", organization.getAddress().getDistrict());
                templateVariables.put("street", organization.getAddress().getStreet());
                templateVariables.put("building", organization.getAddress().getBuilding());
                templateVariables.put("flat", organization.getAddress().getFlat());
                templateVariables.put("firstName", admin.getFirstName());
                templateVariables.put("middleName", admin.getMiddleName());
                templateVariables.put("lastName", admin.getLastName());

                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates" + "/organizationChanges", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");
            }
        };
        mailSender.send(preparator);
    }
}

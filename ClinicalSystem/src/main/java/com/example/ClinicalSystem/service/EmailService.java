package com.example.ClinicalSystem.service;

import com.example.ClinicalSystem.model.Patient;
import com.example.ClinicalSystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.UUID;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;


    @Autowired
    private Environment env;

    @Autowired
    private PatientService patientService;

    @Async
    public void sendDeclineNotificaitionAsync(User user, String explanation) throws MailException, InterruptedException {


        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setFrom(env.getProperty("spring.mail.username"));
        mail.setSubject("Clinical System: Registration");
        mail.setText("Hello " + user.getName() + ",\n\nYour request for registration on our website has been denied. Here's an explanation:\n\n" + explanation + "\n\n\nClinical System Team");
        javaMailSender.send(mail);

    }

    @Async
    public void sendAcceptNotificaitionAsync(Patient patient) throws MailException, InterruptedException {

        //ConfirmationToken confirmationToken = new ConfirmationToken(patient);
        //confirmationTokenService.save(confirmationToken);
        patient.setVerificationCode(UUID.randomUUID().toString());
        patientService.updatePatient(patient);

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(patient.getEmail());
        mail.setFrom(env.getProperty("spring.mail.username"));
        mail.setSubject("Clinical System: Confirm account");
        mail.setText("Hello " + patient.getName() + ",\n\nYour request for registration has been accepted. Click the following link to activate your account:\n\n" + "http://localhost:8081/api/patients/confirm-account/" + patient.getVerificationCode() + "\n\n\nClinical System Team");
        javaMailSender.send(mail);

    }


}
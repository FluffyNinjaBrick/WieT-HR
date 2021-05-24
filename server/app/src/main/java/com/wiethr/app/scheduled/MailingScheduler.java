package com.wiethr.app.scheduled;

import com.wiethr.app.model.Contract;
import com.wiethr.app.repository.IWietHRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class MailingScheduler {

    private final JavaMailSenderImpl mailSender;
    private final IWietHRRepository repository;
    private final ArrayList<Long> monthReminders;
    private final ArrayList<Long> weekReminders;


    @Autowired
    public MailingScheduler(EmailConfiguration emailConfiguration, IWietHRRepository repository) {
        this.repository = repository;
        this.monthReminders = new ArrayList<>();
        this.weekReminders = new ArrayList<>();

        // configure mail sender
        mailSender = new JavaMailSenderImpl();

        mailSender.setHost(emailConfiguration.getHost());
        mailSender.setPort(emailConfiguration.getPort());
        mailSender.setUsername(emailConfiguration.getUsername());
        mailSender.setPassword(emailConfiguration.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
    }

    @Scheduled(fixedDelayString = "PT24H")
    void emailReminderJob() {

        List<String> monthReminderEmails = new ArrayList<>();
        List<String> weekReminderEmails = new ArrayList<>();

        // check which contracts end soon
        List<Contract> contracts = this.repository.getAllContracts();
        for (Contract c: contracts) {

            // skip conditions
            if (
                    c.getDateTo() == null                                // untimed contract
                 || c.getDateTo().isBefore(LocalDate.now())              // contract over
                 || c.getDateTo().getYear() != LocalDate.now().getYear() // different year
            ) continue;

            // case 1: contract ends in a week
            if (c.getDateTo().getDayOfYear() - LocalDate.now().getDayOfYear() <= 7 && !weekReminders.contains(c.getId())) {
                weekReminders.add(c.getId());
                weekReminderEmails.add(c.employeeObject().getEmail());
            }

            // case 2: contract ends in a month
            else if (c.getDateTo().getDayOfYear() - LocalDate.now().getDayOfYear() <= 30 && !monthReminders.contains(c.getId())) {
                monthReminders.add(c.getId());
                monthReminderEmails.add(c.employeeObject().getEmail());
            }

            // TODO - consider what happens if someone extends a contract a month before it ends
        }

        // TODO - create messages and send them

        // create email message instance
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("reminders.wiethr@gmail.com");
        message.setTo("reminders.wiethr@gmail.com");
        message.setSubject("Reservation reminder");
        message.setText("Your reservation is due soon, don't forget!");

        // send message
        System.out.println("Sending mail...");
        mailSender.send(message);
        System.out.println("Mail sent!");
    }


}

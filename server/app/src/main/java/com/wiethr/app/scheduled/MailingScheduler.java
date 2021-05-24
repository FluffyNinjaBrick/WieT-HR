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

        List<String> weekReminderEmails = new ArrayList<>();
        List<String> monthReminderEmails = new ArrayList<>();

        findContractsEndingSoon(weekReminderEmails, monthReminderEmails);
        sendMessages(weekReminderEmails, monthReminderEmails);

    }


    // this method finds the contracts ending soon and fills the lists passed to it with the emails of the employees
    private void findContractsEndingSoon(List<String> weekReminderEmails, List<String> monthReminderEmails) {

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

    }


    private void sendMessages(List<String> weekReminderEmails, List<String> monthReminderEmails) {

        // week reminder message
        if (weekReminderEmails.size() != 0) {
            String[] weekArray = (String[]) weekReminderEmails.toArray();
            SimpleMailMessage weekReminderMessage = new SimpleMailMessage();
            weekReminderMessage.setFrom("reminders.wiethr@gmail.com");
            weekReminderMessage.setTo(weekArray);
            weekReminderMessage.setSubject("Przypomnienie o końcu umowy");
            weekReminderMessage.setText("Do końca Twojej umowy pozostał jedynie tydzień.");

            System.out.println("Sending mail...");
            mailSender.send(weekReminderMessage);
            System.out.println("Mail sent!");
        }

        // month reminder message
        if (monthReminderEmails.size() != 0) {
            String[] monthArray = (String[]) monthReminderEmails.toArray();
            SimpleMailMessage monthReminderMessage = new SimpleMailMessage();
            monthReminderMessage.setFrom("reminders.wiethr@gmail.com");
            monthReminderMessage.setTo(monthArray);
            monthReminderMessage.setSubject("Przypomnienie o końcu umowy");
            monthReminderMessage.setText("Do końca Twojej umowy pozostał jedynie miesiąc.");

            System.out.println("Sending mail...");
            mailSender.send(monthReminderMessage);
            System.out.println("Mail sent!");
        }

    }

}

package system.service;

import com.google.common.eventbus.Subscribe;

import data.Config;
import event.NotificationEvent;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import util.Log;

/**
 * Created by Peter Mösenthin.
 */
public class NotificationService {

    public static final String DEBUG_TAG = NotificationService.class.getSimpleName();

    private static final NotificationService instance = new NotificationService();

    public static void register(){
        EventBusService.register(instance);
    }

    @Subscribe
    public void handleNotificationEvent(final NotificationEvent e){
        Thread notThread = new Thread(new Runnable() {
            public void run() {
                String email_address = Config.getConf().getString("email.admin");
                String sendmail_address = Config.getConf().getString("email.sender.email");
                String sendmail_pw = Config.getConf().getString("email.sender.pw");
                Log.add(DEBUG_TAG,"Sending email to " + email_address);
                Email email = new SimpleEmail();
                email.setHostName("smtp.gmail.com");
                email.setSmtpPort(587);
                //email.setDebug(true);
                email.setAuthentication(sendmail_address, sendmail_pw);
                email.setTLS(true);
                email.setSSL(true);
                email.setSubject(e.getSubject());
                //email.setSSLOnConnect(true);
                try {
                    email.setFrom("rbltestuser01@gmail.com","RaspberryLife");
                } catch (EmailException e1) {
                    //e1.printStackTrace();
                }
                try {
                    email.setMsg(e.getMessage());
                } catch (EmailException e1) {
                    //e1.printStackTrace();
                }
                try {
                    email.addTo(email_address);
                } catch (EmailException e1) {
                    //e1.printStackTrace();
                }
                try {
                    email.send();
                } catch (EmailException e1) {
                    //e1.printStackTrace();
                }
            }
        });
        notThread.start();
    }

}

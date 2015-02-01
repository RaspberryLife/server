package data.model;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name="rbl_notification_message")
public class NotificationMessage {

    @Id
    @GeneratedValue
    @Column(name="notification_id")
    private int id;

    @Column(name="message")
    private int message;

    //----------------------------------------------------------------------------------------------
    //                                      GETTER & SETTER
    //----------------------------------------------------------------------------------------------


    public int getId() {
        return id;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }
}

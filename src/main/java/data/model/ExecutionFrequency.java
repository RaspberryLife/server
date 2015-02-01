package data.model;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name="rbl_execution_frequency")
public class ExecutionFrequency {

    public static final int TYPE_IMMEDIATELY =  0;
    public static final int TYPE_MINUTELY =  1;
    public static final int TYPE_HOURLY =  2;
    public static final int TYPE_DAILY =  3;
    public static final int TYPE_WEEKLY =  4;
    public static final int TYPE_MONTHLY =  5;

    @Id
    @GeneratedValue
    @Column(name="execution_frequency_id")
    private int execution_frequency_id;

    @OneToOne(cascade=CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Logic logic;

    @Column(name="type")
    private int type;

    @Column(name="minute")
    private int minute;

    @Column(name="hour")
    private int hour;

    @Column(name="day")
    private int day;

    @Column(name="week")
    private int week;

    //----------------------------------------------------------------------------------------------
    //                                      GETTER & SETTER
    //----------------------------------------------------------------------------------------------

    public int getExecution_frequency_id() {
        return execution_frequency_id;
    }

    public Logic getLogic() {
        return logic;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }
}

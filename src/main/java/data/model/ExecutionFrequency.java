package data.model;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name="execution_frequency")
public class ExecutionFrequency {

    public static final int TYPE_IMMEDIATELY =  0;
    public static final int TYPE_MINUTELY =  1;
    public static final int TYPE_HOURLY =  2;
    public static final int TYPE_DAILY =  3;
    public static final int TYPE_WEEKLY =  4;
    public static final int TYPE_MONTHLY =  5;

    @Id
    @GeneratedValue
    @Column(name="execution_frequency_table_id")
    private int execution_frequency_table_id;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Logic logic;

    @Column(name="execution_frequency_type")
    private int execution_frequency_type;

    @Column(name="execution_frequency_minute")
    private int execution_frequency_minute;

    @Column(name="execution_frequency_hour")
    private int execution_frequency_hour;

    @Column(name="execution_frequency_day")
    private int execution_frequency_day;

    @Column(name="execution_frequency_week")
    private int execution_frequency_week;

    public int getExecution_frequency_table_id() {
        return execution_frequency_table_id;
    }

    public void setExecution_frequency_table_id(int execution_frequency_table_id) {
        this.execution_frequency_table_id = execution_frequency_table_id;
    }

    public Logic getLogic() {
        return logic;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    public int getExecution_frequency_type() {
        return execution_frequency_type;
    }

    public void setExecution_frequency_type(int execution_frequency_type) {
        this.execution_frequency_type = execution_frequency_type;
    }

    public int getExecution_frequency_minute() {
        return execution_frequency_minute;
    }

    public void setExecution_frequency_minute(int execution_frequency_minute) {
        this.execution_frequency_minute = execution_frequency_minute;
    }

    public int getExecution_frequency_hour() {
        return execution_frequency_hour;
    }

    public void setExecution_frequency_hour(int execution_frequency_hour) {
        this.execution_frequency_hour = execution_frequency_hour;
    }

    public int getExecution_frequency_day() {
        return execution_frequency_day;
    }

    public void setExecution_frequency_day(int execution_frequency_day) {
        this.execution_frequency_day = execution_frequency_day;
    }

    public int getExecution_frequency_week() {
        return execution_frequency_week;
    }

    public void setExecution_frequency_week(int execution_frequency_week) {
        this.execution_frequency_week = execution_frequency_week;
    }
}

package data.model;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name="execution_frequency_table")
public class ExecutionFrequency {

    public static final int IMMEDIATELY =  0;
    public static final int MINUTELY =  1;
    public static final int HOURLY =  2;
    public static final int DAILY =  3;
    public static final int WEEKLY =  4;
    public static final int MONTHLY =  5;

    @Id
    @GeneratedValue
    @Column(name="execution_frequency_table_id")
    private int execution_frequency_table_id;

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

}

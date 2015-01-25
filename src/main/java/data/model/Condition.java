package data.model;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name="condition_table")
public class Condition {

    @Id
    @GeneratedValue
    @Column(name="condition_table_id")
    private int condition_table_id;

    @Column(name="condition_field_id")
    private int condition_field_id;

    @Column(name="condition_threshold_over")
    private int condition_threshold_over;

    @Column(name="condition_threshold_under")
    private int condition_threshold_under;

    @Column(name="condition_state")
    private int condition_state;
}

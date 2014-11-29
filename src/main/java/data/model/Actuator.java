package data.model;

import javax.persistence.*;
import javax.persistence.metamodel.ListAttribute;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name="ACTUATOR")
public class Actuator {

    public static final String TYPE_SYSTEM = "SYSTEM";
    public static final String TYPE_MODULE = "MODULE";
    public static final String TYPE_CLIENT = "CLIENT";

    @Column(name="ACTUATOR_MAP_ID")
    private int id;

    @Column(name="ACTUATOR_NAME")
    private String name;

    @ManyToMany(mappedBy="initiator")
    private Set<Logic> logic_initiator;

    @ManyToMany(mappedBy="receiver")
    private Set<Logic> Logic_receiver;

    @Id
    @GeneratedValue
    @Column(name="ACTUATOR_ID")
    private int actuatorId;

    @Column(name="ACTUATOR_TYPE")
    private String type;

    public Actuator(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }
}

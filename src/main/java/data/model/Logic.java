package data.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Peter Mösenthin.
 */

@Entity
@Table(name="LOGIC")
public class Logic {

    @Id
    @GeneratedValue
    @Column(name="LOGIC_ID")
    private Long id;

    @Column(name="LOGIC_NAME")
    private String name;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="LOGIC_INITIATOR",
            joinColumns={@JoinColumn(name="LOGIC_ID")},
            inverseJoinColumns={@JoinColumn(name="ACTUATOR_ID")})
    private Set<Actuator> initiator = new HashSet<Actuator>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="LOGIC_RECEIVER",
            joinColumns={@JoinColumn(name="LOGIC_ID")},
            inverseJoinColumns={@JoinColumn(name="ACTUATOR_ID")})
    private Set<Actuator> receiver = new HashSet<Actuator>();

    //private Condition condition;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Actuator> getInitiator() {
        return initiator;
    }

    public void setInitiator(Set<Actuator> initiator) {
        this.initiator = initiator;
    }

    public Set<Actuator> getReceiver() {
        return receiver;
    }

    public void setReceiver(Set<Actuator> receiver) {
        this.receiver = receiver;
    }
}

package entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
public class FRA extends Trade<FRA> {

    private String tradeType;

    private String legPay;

    private Double notional;

    private Double fixedRate;

    private String indexTenor;

    private String index;

    @Relationship(type = "PAYS")
    private Set<Leg> payLegs;

    @Relationship(type = "RECEIVE")
    private Set<Leg> receiveLegs;

}

package entity

import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

@NodeEntity
class FRA extends Trade<FRA> {

    String tradeType

    String legPay

    Double notional

    Double fixedRate

    String indexTenor

    String index

    @Relationship(type = "PAYS")
    Set<Leg> payLegs

    @Relationship(type = "RECEIVE")
    Set<Leg> receiveLegs

}

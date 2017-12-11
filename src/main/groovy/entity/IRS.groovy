package entity

import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

@NodeEntity
class IRS extends Trade<IRS>{

    String tradeType

    @Relationship(type = "PAYS")
    Set<Leg> payLegs

    @Relationship(type = "RECEIVE")
    Set<Leg> receiveLegs

}

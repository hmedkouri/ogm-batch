package entity;

import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
class PricingSource extends Entity<PricingSource> {

    @Index(primary = true)
    PricingProvider name

}
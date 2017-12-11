package entity

import org.neo4j.ogm.annotation.Index
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property

@NodeEntity
class TradingAccount extends Entity<TradingAccount> {

    @Property(name="id")
    @Index(primary = true)
    String accountId

    String name

}

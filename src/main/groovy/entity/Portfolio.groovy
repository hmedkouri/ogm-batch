package entity

import com.acuo.common.ids.PortfolioId
import converters.TypedStringConverter
import org.neo4j.ogm.annotation.Index
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.typeconversion.Convert

@NodeEntity
class Portfolio extends Entity<Portfolio> {

    @Property(name = "id")
    @Index(primary = true)
    @Convert(TypedStringConverter.PortfolioIdConverter)
    PortfolioId portfolioId

    String name
}

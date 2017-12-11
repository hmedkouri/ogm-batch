package ids

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.joda.convert.FromString

class PortfolioId extends TypedString<PortfolioId> {

    PortfolioId(String name) {
        super(name)
    }

    @FromString
    @JsonCreator
    static PortfolioId fromString(@JsonProperty("name") String id) {
        Objects.requireNonNull(id, "id")
        return new PortfolioId(id)
    }

}

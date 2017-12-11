package ids

import com.fasterxml.jackson.annotation.JsonCreator
import org.joda.convert.FromString

class TradeId extends TypedString<TradeId> {

    private TradeId(String name) {
        super(name)
    }

    @FromString
    @JsonCreator
    static TradeId fromString(String id) {
        Objects.requireNonNull(id, "id")
        return new TradeId(id)
    }

}

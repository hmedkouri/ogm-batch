package ids

import com.fasterxml.jackson.annotation.JsonCreator
import org.joda.convert.FromString

class MarginStatementId extends TypedString<MarginStatementId> {

    private MarginStatementId(String name) {
        super(name)
    }

    @FromString
    @JsonCreator
    static MarginStatementId fromString(String id) {
        Objects.requireNonNull(id, "id")
        return new MarginStatementId(id)
    }
}

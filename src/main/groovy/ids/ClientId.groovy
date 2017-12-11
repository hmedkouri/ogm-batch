package ids

import com.fasterxml.jackson.annotation.JsonCreator
import org.joda.convert.FromString

class ClientId extends TypedString<ClientId> {

    private ClientId(String name) {
        super(name)
    }

    @FromString
    @JsonCreator
    static ClientId fromString(String id) {
        Objects.requireNonNull(id, "id")
        return new ClientId(id)
    }
}

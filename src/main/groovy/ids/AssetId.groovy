package ids

import com.fasterxml.jackson.annotation.JsonCreator
import org.joda.convert.FromString

class AssetId extends TypedString<AssetId> {

    private AssetId(String name) {
        super(name)
    }

    @FromString
    @JsonCreator
    static AssetId fromString(String id) {
        Objects.requireNonNull(id, "id")
        return new AssetId(id)
    }

}
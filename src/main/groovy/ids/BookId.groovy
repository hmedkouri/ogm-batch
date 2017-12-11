package ids

import com.fasterxml.jackson.annotation.JsonCreator
import org.joda.convert.FromString

class BookId extends TypedString<BookId> {

    private BookId(String name) {
        super(name)
    }

    @FromString
    @JsonCreator
    static BookId fromString(String id) {
        Objects.requireNonNull(id, "id")
        return new BookId(id)
    }

}

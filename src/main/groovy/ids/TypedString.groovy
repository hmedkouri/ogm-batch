package ids

import com.fasterxml.jackson.annotation.JsonValue
import org.joda.convert.ToString

import java.util.regex.Pattern
/**
 * An abstract class designed to enable typed strings.
 * <p>
 * The purpose of {@code TypedString} is to provide a Java type to a concept
 * that might otherwise be represented as a string.
 * It could be thought of as a way to provide a type alias for a string.
 * <p>
 * The string wrapped by this type must not be empty.
 * <p>
 * Subclasses must be written as follows:
 * <pre>
 *  public final class FooType
 *      extends TypedString&lt;FooType&gt; {
 *    private static final long serialVersionUID = 1L;
 *    &#64;FromString
 *    public static FooType of(String name) {
 *      return new FooType(name);
 *    }
 *    private FooType(String name) {
 *      super(name);
 *    }
 *  }
 * </pre>
 * <p>
 * The net result is that an API can be written with methods taking
 * {@code FooType} as a method parameter instead of {@code String}.
 *
 * @param <T> the implementation subclass of this class
 */
abstract class TypedString<T extends TypedString<T>>
        implements Comparable<T>, Serializable {

    /**
     * Serialization version.
     */
    private static final long serialVersionUID = 1L

    /**
     * The name.
     */
    final String name

    /**
     * Creates an instance.
     *
     * @param name the name, not empty
     */
    protected TypedString(String name) {
        this.name = Objects.requireNonNull(name, "name")
    }

    /**
     * Creates an instance, validating the name against a regex.
     *
     * @param name    the name, not empty
     * @param pattern the regex pattern for validating the name
     * @param msg     the message to use to explain validation failure
     */
    protected TypedString(String name, Pattern pattern, String msg) {
        Objects.requireNonNull(name, "name")
        Objects.requireNonNull(pattern, "pattern")
        Objects.requireNonNull(msg, "msg")
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(msg)
        }
        this.name = name
    }

    //-------------------------------------------------------------------------

    /**
     * Compares this type to another.
     * <p>
     * Instances are compared in alphabetical order based on the name.
     *
     * @param other the object to compare to
     * @return the comparison
     */
    @Override
    final int compareTo(T other) {
        return name.compareTo(other.toString())
    }

    /**
     * Checks if this type equals another.
     * <p>
     * Instances are compared based on the name.
     *
     * @param obj the object to compare to, null returns false
     * @return true if equal
     */
    @Override
    final boolean equals(Object obj) {
        if (obj == this) {
            return true
        }
        if (obj != null && obj.getClass() == getClass()) {
            TypedString<?> other = (TypedString<?>) obj
            return name.equals(other.name)
        }
        return false
    }

    /**
     * Returns a suitable hash code.
     *
     * @return a suitable hash code
     */
    @Override
    final int hashCode() {
        return name.hashCode() ^ getClass().hashCode()
    }

    /**
     * Returns the name.
     *
     * @return the string form, not empty
     */
    @Override
    @ToString
    @JsonValue
    final String toString() {
        return name
    }

}
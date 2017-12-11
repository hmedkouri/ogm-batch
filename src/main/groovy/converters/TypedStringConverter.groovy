package converters

import groovy.util.logging.Slf4j
import ids.AssetId
import ids.BookId
import ids.ClientId
import ids.MarginStatementId
import ids.PortfolioId
import ids.TradeId
import ids.TypedString
import org.neo4j.ogm.typeconversion.AttributeConverter

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@Slf4j
abstract class TypedStringConverter<T extends TypedString> implements AttributeConverter<T, String> {

    static class PortfolioIdConverter extends TypedStringConverter<PortfolioId> {}

    static class ClientIdConverter extends TypedStringConverter<ClientId> {}

    static class TradeIdConverter extends TypedStringConverter<TradeId> {}

    static class MarginStatementIdConverter extends TypedStringConverter<MarginStatementId> {}

    static class AssetIdConverter extends TypedStringConverter<AssetId> {}

    static class BookIdConverter extends TypedStringConverter<BookId> {}

    @Override
    String toGraphProperty(T value) {
        return value.toString()
    }

    @Override
    T toEntityAttribute(String value) {
        Class<T> clazz = getReturnTypeFor()
        try {
            return (T) clazz.getMethod("fromString", String.class).invoke(null, value)
        } catch (IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException e) {
            throw new RuntimeException(e)
        }
    }

    private Class getReturnTypeFor() {
        Type type = getClass().getGenericSuperclass()
        if (type instanceof ParameterizedType) {
            ParameterizedType t = (ParameterizedType) type
            if (t.getRawType().equals(TypedStringConverter.class)) {
                Type argument = t.getActualTypeArguments()[0]
                if (argument instanceof ParameterizedType) {
                    return (Class) ((ParameterizedType) argument).getRawType()
                }
                return (Class) argument
            }
        }
        throw new RuntimeException("Your converter does not define its return type? ")
    }
}

package ee.kristofer.rental;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ee.kristofer.rental.exception.RentalException;
import org.springframework.stereotype.Component;

@Component
public class MapperUtil {
    private final ObjectMapper objectMapper;

    public MapperUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static MapperUtil getInstance() {
        return new MapperUtil(new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
    }

    public String objectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (final JsonProcessingException jpe)  {
            throw new RentalException("Failed to write object into json", jpe);
        }
    }

    public <T> T jsonToObject(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (final JsonProcessingException jpe) {
            throw new RentalException("Failed to read json type " + type + " into object", jpe);
        }
    }
}

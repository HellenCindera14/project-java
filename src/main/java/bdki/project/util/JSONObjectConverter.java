package bdki.project.util;

import org.json.JSONObject;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JSONObjectConverter implements AttributeConverter<JSONObject, String> {

    @Override
    public String convertToDatabaseColumn(JSONObject jsonObject) {
        return (jsonObject != null) ? jsonObject.toString() : null;
    }

    @Override
    public JSONObject convertToEntityAttribute(String jsonString) {
        return (jsonString != null) ? new JSONObject(jsonString) : null;
    }
}

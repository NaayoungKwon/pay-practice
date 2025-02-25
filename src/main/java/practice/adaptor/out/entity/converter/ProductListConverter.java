package practice.adaptor.out.entity.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import practice.payment.domain.Product;

@Component
@RequiredArgsConstructor
public class ProductListConverter implements AttributeConverter<List<Product>, String> {

  private final ObjectMapper mapper;

  @Override
  public String convertToDatabaseColumn(List<Product> meta) {
    if (meta == null) {
      return null;
    }
    try {
      return mapper.writeValueAsString(meta);
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public List<Product> convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return null;
    }
    try {
      return mapper.readValue(dbData, new TypeReference<List<Product>>() {
      });
    } catch (Exception e) {
      return null;
    }
  }
}

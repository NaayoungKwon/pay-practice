package practice.adaptor.out.entity.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Component
import practice.payment.domain.Product

@Converter
@Component
@RequiredArgsConstructor
class ProductListConverter(
    val mapper: ObjectMapper
) : AttributeConverter<List<Product>, String> {


    override fun convertToDatabaseColumn(meta: List<Product>?): String? {
        if (meta == null) {
            return null
        }

        return try {
            mapper.writeValueAsString(meta)
        } catch (e: Exception) {
            null
        }
    }

    override fun convertToEntityAttribute(dbData: String?): List<Product>? {
        if (dbData == null) {
            return null
        }

        return try {
            mapper.readValue(dbData, object : TypeReference<List<Product>>() {})
        } catch (e: Exception) {
            null
        }
    }
}
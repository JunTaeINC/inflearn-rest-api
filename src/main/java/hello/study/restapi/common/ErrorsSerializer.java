package hello.study.restapi.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

/*
 	Jackson 라이브러리에 직렬화 또는 역직렬화를 위한 컴포넌트를 등록하는 어노테이션
 	Errors 타입의 객체가 JSON으로 변환될 때 ErrorsSerializer에 정의된 로직이 사용
 */
@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {

	@Override
	public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeFieldName("errors");
		gen.writeStartArray();
		errors.getFieldErrors().forEach(e -> {
			try {
				gen.writeStartObject();
				gen.writeStringField("field", e.getField());
				gen.writeStringField("objectName", e.getObjectName());
				gen.writeStringField("code", e.getCode());
				gen.writeStringField("defaultMessage", e.getDefaultMessage());
				Object rejectedValue = e.getRejectedValue();
				if (rejectedValue != null) {
					gen.writeStringField("rejectedValue", rejectedValue.toString());
				}
				gen.writeEndObject();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		errors.getGlobalErrors().forEach(e -> {
			try {
				gen.writeStartObject();
				gen.writeStringField("objectName", e.getObjectName());
				gen.writeStringField("code", e.getCode());
				gen.writeStringField("defaultMessage", e.getDefaultMessage());
				gen.writeEndObject();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		gen.writeEndArray();
	}
}
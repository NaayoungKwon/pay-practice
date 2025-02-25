package practice.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {

  @Builder.Default
  String code = ResponseCode.Success.name();

  @Builder.Default
  String message = ResponseCode.Success.getMessage();

  T detail;

}

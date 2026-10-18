package by.lab.registry.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Тело запроса регистрации: прикладной сервис присылает всё о себе одним JSON.
 */
public record RegisterServiceRequest(
    @NotBlank @Size(max = 100) String name,
    @NotBlank @Size(max = 500) @Pattern(regexp = "^https?://.+", message = "baseUrl должен начинаться с http:// или https://") String baseUrl,
    @Size(max = 1000) String description,
    @NotEmpty @Valid List<MethodDto> methods) {

  public record MethodDto(
      @NotBlank @Size(max = 100) String name,
      @NotBlank @Pattern(regexp = "GET|POST|PUT|PATCH|DELETE") String httpMethod,
      @NotBlank @Size(max = 300) String path,
      @Size(max = 1000) String description,
      Boolean paid,
      @PositiveOrZero BigDecimal price,
      @Valid List<ParamDto> parameters) {
  }

  public record ParamDto(
      @NotBlank @Size(max = 100) String name,
      @NotBlank @Size(max = 50) String type,
      @Pattern(regexp = "query|path|body|header") String location,
      Boolean required) {
  }
}

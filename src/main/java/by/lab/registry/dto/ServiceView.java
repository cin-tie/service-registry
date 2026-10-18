package by.lab.registry.dto;

import by.lab.registry.model.MethodParameter;
import by.lab.registry.model.RegisteredService;
import by.lab.registry.model.ServiceMethod;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/** Ответ реестра. Служебные (paid=false) методы наружу не отдаются. */
public record ServiceView(Long id, String name, String baseUrl, String description,
    Instant registeredAt, List<MethodView> methods) {

  public record MethodView(Long id, String name, String httpMethod, String path,
      String description, BigDecimal price, List<ParamView> parameters) {
  }

  public record ParamView(String name, String type, String location, boolean required) {
  }

  public static ServiceView from(RegisteredService s) {
    List<MethodView> methods = s.getMethods().stream()
        .filter(ServiceMethod::isPaid)
        .map(ServiceView::method)
        .toList();
    return new ServiceView(s.getId(), s.getName(), s.getBaseUrl(), s.getDescription(),
        s.getRegisteredAt(), methods);
  }

  public static MethodView method(ServiceMethod m) {
    List<ParamView> ps = m.getParameters().stream().map(ServiceView::param).toList();
    return new MethodView(m.getId(), m.getName(), m.getHttpMethod(), m.getPath(),
        m.getDescription(), m.getPrice(), ps);
  }

  private static ParamView param(MethodParameter p) {
    return new ParamView(p.getName(), p.getType(), p.getLocation(), p.isRequired());
  }
}

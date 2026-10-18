package by.lab.registry.api;

import by.lab.registry.dto.RegisterServiceRequest;
import by.lab.registry.dto.ServiceView;
import by.lab.registry.model.MethodParameter;
import by.lab.registry.model.RegisteredService;
import by.lab.registry.model.ServiceMethod;
import by.lab.registry.repo.ServiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
public class RegistryService {

  private final ServiceRepository repo;

  public RegistryService(ServiceRepository repo) {
    this.repo = repo;
  }

  /** Однократная регистрация: повторная с тем же именем даёт 409. */
  @Transactional
  public ServiceView register(RegisterServiceRequest req) {
    if (repo.existsByNameIgnoreCase(req.name())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          "Сервис '" + req.name() + "' уже зарегистрирован");
    }
    long distinct = req.methods().stream().map(m -> m.name().toLowerCase()).distinct().count();
    if (distinct != req.methods().size()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Имена методов должны быть уникальны");
    }

    RegisteredService s = new RegisteredService();
    s.setName(req.name().trim());
    s.setBaseUrl(stripTrailingSlash(req.baseUrl().trim()));
    s.setDescription(req.description());

    for (var md : req.methods()) {
      ServiceMethod m = new ServiceMethod();
      m.setService(s);
      m.setName(md.name().trim());
      m.setHttpMethod(md.httpMethod());
      m.setPath(md.path().startsWith("/") ? md.path() : "/" + md.path());
      m.setDescription(md.description());
      m.setPaid(md.paid() == null || md.paid());
      m.setPrice(md.price());
      if (md.parameters() != null) {
        for (var pd : md.parameters()) {
          MethodParameter p = new MethodParameter();
          p.setMethod(m);
          p.setName(pd.name());
          p.setType(pd.type());
          p.setLocation(pd.location() == null ? "query" : pd.location());
          p.setRequired(pd.required() == null || pd.required());
          m.getParameters().add(p);
        }
      }
      s.getMethods().add(m);
    }
    return ServiceView.from(repo.save(s));
  }

  @Transactional(readOnly = true)
  public List<ServiceView> list() {
    return repo.findAll().stream().map(ServiceView::from).toList();
  }

  @Transactional(readOnly = true)
  public List<ServiceView> search(String q) {
    String text = q == null ? "" : q.trim().toLowerCase(Locale.ROOT);
    String pattern = "%" + text + "%";
    return repo.search(pattern).stream().map(ServiceView::from).toList();
  }

  @Transactional(readOnly = true)
  public ServiceView get(String name) {
    return ServiceView.from(find(name));
  }

  @Transactional(readOnly = true)
  public ServiceView.MethodView getPaidMethod(String serviceName, String methodName) {
    return find(serviceName).getMethods().stream()
        .filter(m -> m.isPaid() && m.getName().equalsIgnoreCase(methodName))
        .findFirst()
        .map(ServiceView::method)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Метод '" + methodName + "' не найден у сервиса '" + serviceName + "'"));
  }

  @Transactional
  public void delete(String name) {
    repo.delete(find(name));
  }

  private RegisteredService find(String name) {
    return repo.findByNameIgnoreCase(name)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Сервис '" + name + "' не найден"));
  }

  private static String stripTrailingSlash(String url) {
    return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
  }
}

package by.lab.registry.api;

import by.lab.registry.dto.RegisterServiceRequest;
import by.lab.registry.dto.ServiceView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RegistryController {

  private final RegistryService service;
  private final String adminKey;

  public RegistryController(RegistryService service, @Value("${registry.admin-key}") String adminKey) {
    this.service = service;
    this.adminKey = adminKey;
  }

  /** Регистрация прикладного сервиса (однократно). */
  @PostMapping("/services")
  public ResponseEntity<ServiceView> register(@Valid @RequestBody RegisterServiceRequest req) {
    ServiceView created = service.register(req);
    return ResponseEntity.created(URI.create("/api/services/" + created.name())).body(created);
  }

  /** Все сервисы (для клиентов: просмотр реестра). */
  @GetMapping("/services")
  public List<ServiceView> list() {
    return service.list();
  }

  /** Поиск по имени сервиса / методу / описанию. */
  @GetMapping("/services/search")
  public List<ServiceView> search(@RequestParam(defaultValue = "") String q) {
    return service.search(q);
  }

  @GetMapping("/services/{name}")
  public ServiceView get(@PathVariable String name) {
    return service.get(name);
  }

  /**
   * Для сервиса оплаты: проверка наличия сервиса и платного метода, возвращает
   * цену.
   */
  @GetMapping("/services/{name}/methods/{method}")
  public ServiceView.MethodView method(@PathVariable String name, @PathVariable String method) {
    return service.getPaidMethod(name, method);
  }

  /** Служебное: удаление сервиса. Требует заголовок X-Admin-Key. */
  @DeleteMapping("/services/{name}")
  public ResponseEntity<Void> delete(@PathVariable String name,
      @RequestHeader(value = "X-Admin-Key", required = false) String key) {
    if (key == null || !key.equals(adminKey)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Неверный X-Admin-Key");
    }
    service.delete(name);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/health")
  public String health() {
    return "ok";
  }
}

package by.lab.registry.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Таблица 2: методы сервиса. */
@Entity
@Table(name = "service_methods", uniqueConstraints = @UniqueConstraint(columnNames = { "service_id", "name" }))
public class ServiceMethod {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "service_id")
  private RegisteredService service;

  @Column(nullable = false, length = 100)
  private String name;

  /** GET / POST / PUT / PATCH / DELETE */
  @Column(nullable = false, length = 10)
  private String httpMethod;

  /** Путь относительно baseUrl, например /api/books */
  @Column(nullable = false, length = 300)
  private String path;

  @Column(length = 1000)
  private String description;

  /**
   * true - платный публичный метод; false - скрытый служебный (в просмотре не
   * показывается).
   */
  @Column(nullable = false)
  private boolean paid = true;

  /** Цена (для сервиса оплаты). */
  @Column(precision = 12, scale = 2)
  private BigDecimal price;

  @OneToMany(mappedBy = "method", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MethodParameter> parameters = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public RegisteredService getService() {
    return service;
  }

  public void setService(RegisteredService service) {
    this.service = service;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public void setHttpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isPaid() {
    return paid;
  }

  public void setPaid(boolean paid) {
    this.paid = paid;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public List<MethodParameter> getParameters() {
    return parameters;
  }
}

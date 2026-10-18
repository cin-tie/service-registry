package by.lab.registry.model;

import jakarta.persistence.*;

/** Таблица 3: аргументы методов. */
@Entity
@Table(name = "method_parameters")
public class MethodParameter {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "method_id")
  private ServiceMethod method;

  @Column(nullable = false, length = 100)
  private String name;

  /** string, int, decimal, bool, date, json ... */
  @Column(nullable = false, length = 50)
  private String type;

  /** query / path / body / header */
  @Column(nullable = false, length = 20)
  private String location = "query";

  @Column(nullable = false)
  private boolean required = true;

  public Long getId() {
    return id;
  }

  public ServiceMethod getMethod() {
    return method;
  }

  public void setMethod(ServiceMethod method) {
    this.method = method;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public boolean isRequired() {
    return required;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }
}

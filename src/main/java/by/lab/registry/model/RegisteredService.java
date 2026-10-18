package by.lab.registry.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/** Таблица 1: сервис (уникальное имя + адрес). */
@Entity
@Table(name = "services")
public class RegisteredService {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 100)
  private String name;

  @Column(nullable = false, length = 500)
  private String baseUrl;

  @Column(length = 1000)
  private String description;

  @Column(nullable = false)
  private Instant registeredAt = Instant.now();

  @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ServiceMethod> methods = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Instant getRegisteredAt() {
    return registeredAt;
  }

  public List<ServiceMethod> getMethods() {
    return methods;
  }
}

package by.lab.registry.repo;

import by.lab.registry.model.RegisteredService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<RegisteredService, Long> {

  Optional<RegisteredService> findByNameIgnoreCase(String name);

  boolean existsByNameIgnoreCase(String name);

  @Query("""
      select distinct s from RegisteredService s left join s.methods m
      where lower(s.name) like :pattern
         or lower(coalesce(s.description, '')) like :pattern
         or lower(m.name) like :pattern
         or lower(coalesce(m.description, '')) like :pattern
      order by s.name
      """)
  List<RegisteredService> search(@Param("pattern") String pattern);
}

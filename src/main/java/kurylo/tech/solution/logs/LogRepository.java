package kurylo.tech.solution.logs;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface LogRepository extends JpaRepository<LogAggregate, Long> {
    Optional<LogAggregate> findByBusinessId(String businessId);
    boolean existsByBusinessId(String businessId);
}

package com.wiethr.app.repository.jpaRepos;

import com.wiethr.app.model.BonusBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.Optional;

@Repository
public interface BonusBudgetRepository extends JpaRepository<BonusBudget, Long> {
    Optional<BonusBudget> getBonusBudgetByYear(Year year);
}

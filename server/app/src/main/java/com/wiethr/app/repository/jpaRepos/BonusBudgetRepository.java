package com.wiethr.app.repository.jpaRepos;

import com.wiethr.app.model.BonusBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonusBudgetRepository extends JpaRepository<BonusBudget, Long> {
}

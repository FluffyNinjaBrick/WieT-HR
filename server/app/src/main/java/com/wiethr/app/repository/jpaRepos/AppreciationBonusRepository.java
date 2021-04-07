package com.wiethr.app.repository.jpaRepos;

import com.wiethr.app.model.AppreciationBonus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppreciationBonusRepository extends JpaRepository <AppreciationBonus, Long> {

}

package com.wiethr.app.repository.jpaRepos;

import com.wiethr.app.model.DelegationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DelegationRequestRepository extends JpaRepository<DelegationRequest, Long> {
}

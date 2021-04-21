package com.wiethr.app.repository.jpaRepos;

import com.wiethr.app.model.DaysOffRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DaysOffRequestRepository extends JpaRepository<DaysOffRequest,Long> {
    List<DaysOffRequest> findAllByEmployeeIdAndDateFromAfter(long id, LocalDate localDate);
}

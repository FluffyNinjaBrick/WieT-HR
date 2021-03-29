package com.wiethr.app.repository.jpaRepos;

import com.wiethr.app.model.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionsRepository extends JpaRepository<Permissions, Long> {
}

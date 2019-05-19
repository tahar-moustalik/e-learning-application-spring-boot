package com.springdev.mepdev.persistance;

import com.springdev.mepdev.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("roleRepository")
public interface RoleRepository  extends JpaRepository<Role,Long> {

    Role findByRoleName(String role);

}

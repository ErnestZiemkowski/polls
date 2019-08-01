package com.twitter.polls.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.twitter.polls.model.Role;
import com.twitter.polls.model.RoleName;

public interface RoleDao extends JpaRepository<Role, Long> {
	Optional<Role> findByName(RoleName roleName);
}

package com.user.user_registration.repositories;

import com.user.user_registration.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long > {}

package xyz.krakenkat.restapi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.krakenkat.restapi.domain.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}

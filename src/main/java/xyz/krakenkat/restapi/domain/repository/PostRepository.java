package xyz.krakenkat.restapi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.krakenkat.restapi.domain.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
}

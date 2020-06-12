package sw2.lab6.teletok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sw2.lab6.teletok.entity.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByOrderByCreationDateDesc();

    List<Post> findByDescriptionContainingOrUserUsernameContainingOrderByCreationDateDesc(String description, String username);
}

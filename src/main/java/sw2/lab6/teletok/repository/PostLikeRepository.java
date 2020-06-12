package sw2.lab6.teletok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sw2.lab6.teletok.entity.PostLike;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {
}

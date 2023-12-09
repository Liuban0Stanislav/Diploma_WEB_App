package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.CommentEntity;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    @Query(value = "select * from comments where ad_id = :ad_id", nativeQuery = true)
    List<CommentEntity> findByAdId(@Param("ad_id") Integer adId);

    CommentEntity findFirstByText(String text);
}

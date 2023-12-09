package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.PhotoEntity;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Integer> {
//    Optional<PhotoEntity> findByAd(AdEntity ad);

//    Optional<PhotoEntity> findByAdId(Integer id);

    Optional<PhotoEntity> findById(Integer id);
}

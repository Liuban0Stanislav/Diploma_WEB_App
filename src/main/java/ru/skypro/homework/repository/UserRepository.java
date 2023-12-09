package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.UserEntity;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    /**
     * Пиоск пользователя по логину
     * @param userName логин
     * @return объект userEntity
     */
    UserEntity findUserEntityByUserName(String userName);
}

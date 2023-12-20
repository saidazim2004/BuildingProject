package com.example.buildingproject.repository;

import com.example.buildingproject.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.net.ContentHandler;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Query("select u from users u where u.email =:email and not u.deleted")
    Optional<UserEntity> findByEmail(String email);

    @Query("from users u where not u.deleted")
    Page<UserEntity> findAllUsers(Pageable pageable);



    @Query("from users u where not u.deleted and u.id = :userId")
    Optional<UserEntity> getUserByID(@Param("userId") UUID id);
    @Query("from users u where not u.deleted and u.passportNo = :passportNo")
    Optional<UserEntity> getUserEntityByPassportNo(@Param("passportNo") String passportNo);

    boolean existsUserByEmail(String email);

    UserEntity getUserEntityById(UUID id);

}

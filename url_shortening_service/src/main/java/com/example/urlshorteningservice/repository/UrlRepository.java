package com.example.urlshorteningservice.repository;

import com.example.urlshorteningservice.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, String> {

    @Modifying
    @Query("update Url u set u.usageCounter = u.usageCounter + 1 where u.id = :id")
    void increaseCounter(@Param(value = "id") String id);

    Optional<Url> findByIdAndIsActiveTrue(String id);

    List<Url> findAllByUserIdAndIsActiveTrue(String userId);

    @Modifying
    @Query("UPDATE Url u SET u.isActive = false WHERE u.id = :id AND u.userId = :userId")
    void disableUrl(String id, String userId);
}

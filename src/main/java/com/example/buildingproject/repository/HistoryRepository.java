package com.example.buildingproject.repository;

import com.example.buildingproject.entity.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HistoryRepository extends JpaRepository<HistoryEntity, UUID> {

    @Query("FROM HistoryEntity h WHERE h.user.id = :user_id AND h.house.id = :house_id AND h.isPaid = false")
    ArrayList<HistoryEntity> getHistoryEntitiesByIdAndOwnerId(
            @Param("user_id") UUID user_id,
            @Param("house_id") UUID house_id
    );


    @Query("FROM HistoryEntity h WHERE h.isPaid = false AND YEAR(h.monthlyPeriod) = :targetYear AND MONTH(h.monthlyPeriod) = :targetMonth")
    List<HistoryEntity> getUnpaidHistoryEntitiesYearAndForMonth(
            @Param("targetMonth") int targetMonth, @Param("targetYear") int targetYear
    );
    @Query("FROM HistoryEntity h WHERE h.house.id = :house_id and h.user.id = :user_id")
    ArrayList<HistoryEntity> getAllHistories(@Param("user_id") UUID userId, @Param("house_id") UUID houseId);
}

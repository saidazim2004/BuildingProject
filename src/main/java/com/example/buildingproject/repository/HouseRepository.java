package com.example.buildingproject.repository;

import com.example.buildingproject.entity.HouseEntity;
import org.modelmapper.internal.bytebuddy.dynamic.DynamicType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public interface HouseRepository extends JpaRepository<HouseEntity , UUID> {
    Optional<HouseEntity> findHouseEntityByHomeAddress(String homeAddress);
    @Query("from HouseEntity h where  h.ownerHouse.id = :owner_house_id")
    ArrayList<HouseEntity> findHouseEntitiesByOwnerId(@Param("owner_house_id") UUID owner_house_id);
//    @Query("from HouseEntity h where  h.id = :id")
//    Optional<HouseEntity> getHouseEntityById(@Param("id") UUID id );
}

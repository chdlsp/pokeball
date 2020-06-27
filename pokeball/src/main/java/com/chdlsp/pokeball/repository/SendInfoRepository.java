package com.chdlsp.pokeball.repository;

import com.chdlsp.pokeball.model.entity.SendInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SendInfoRepository extends JpaRepository<SendInfo, Long> {

    boolean existsByToken(String code);

    Optional<SendInfo> findByxUserIdAndTokenAndCreatedAtAfter(String xUserId, String token, LocalDateTime createdAt);

    Optional<SendInfo> findByxRoomIdAndToken(String xRoomId, String token);

    Optional<SendInfo> findByTokenAndCreatedAtAfter(String token, LocalDateTime createdAt);

}

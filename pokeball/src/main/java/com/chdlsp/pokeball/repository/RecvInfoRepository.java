package com.chdlsp.pokeball.repository;

import com.chdlsp.pokeball.model.entity.RecvInfo;
import com.chdlsp.pokeball.model.enumClass.RecvYnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecvInfoRepository extends JpaRepository<RecvInfo, Long> {

    List<RecvInfo> findByxUserIdAndTokenAndRecvYn(String xUserId, String token, RecvYnStatus recvYnStatus);

    Optional<RecvInfo> findByxRoomIdAndTokenAndRecvUserIdAndRecvYn(String xRoomId, String token, String recvUserId, RecvYnStatus recvYnStatus);

    Optional<List<RecvInfo>> findByxRoomIdAndTokenAndRecvYn(String xRoomId, String token, RecvYnStatus recvYnStatus);

}

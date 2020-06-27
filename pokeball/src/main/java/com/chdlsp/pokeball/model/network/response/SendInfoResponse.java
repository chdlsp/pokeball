package com.chdlsp.pokeball.model.network.response;

import com.chdlsp.pokeball.model.vo.RecvUserInfoVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendInfoResponse {

    private LocalDateTime createdAt;
    private BigDecimal SendTotAmt;
    private BigDecimal recvAmt;
    private List<RecvUserInfoVO> recvUserInfoVO;

}

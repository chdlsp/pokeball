package com.chdlsp.pokeball.model.enumClass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RecvYnStatus {
    RECEIVED(0, "Y", "뿌리기 수령 완료 상태"),
    NOT_RECEIVED(1, "N", "뿌리기 수령 전 상태");

    private Integer id;
    private String value;
    private String description;
}

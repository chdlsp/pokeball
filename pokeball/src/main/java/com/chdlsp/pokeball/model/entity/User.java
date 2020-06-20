package com.chdlsp.pokeball.model.entity;

import com.chdlsp.pokeball.model.enumClass.UserStatus;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

// LAZY = 지연로딩, EAGER = 즉시로딩
//
// => Many 걸려있는 쿼리문에 EAGER 모드를 사용 시 쿼리 성능 지연 발생 가능 함
// => EAGER 은 1:1 관계에 사용
// -----------------------------------------------------------------------
// (LAZY)
// SELECT * FROM item WHERE id = ?
// -----------------------------------------------------------------------
// (EAGER)
// SELECT *
//   FROM item, user, order_detail
//  WHERE 1=1
//    AND item_id = order_detail_id
//    AND user_id = order_detail_id
//    AND item.id = ?

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"orderGroupList"}) // orderGroup 변수에 대해 toString 제외
@Entity // == table
@EntityListeners(AuditingEntityListener.class) // JPA Audit 처리
@Accessors(chain = true)
// @Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String account;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private String email;
    private String phoneNumber;
    private LocalDateTime registeredAt;
    private LocalDateTime unregisteredAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;

//    // User 1 : N OderGroup
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
//    private List<OrderGroup> orderGroupList;
}

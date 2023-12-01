package com.jme.spatch.backend.model.complain.entity;

import com.jme.spatch.backend.model.order.entity.Order;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Complain {
    @Id
    @SequenceGenerator(
            name = "complain_seq",
            sequenceName = "complain_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "complain_seq")
    private long id;
    private String Subject;
    private String content;
    private List<String> attachment;
    @ManyToOne
    private UserEntity user;
    @ManyToOne
    private Order order;
}

package com.example.countryapi.models;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.relational.core.sql.In;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "token_id")
    private Integer id;
    private String name;
    private Date expireDate;
    private String token;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="User_info_id")
    private UserInfo userInfo;
}
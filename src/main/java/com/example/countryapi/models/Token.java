package com.example.countryapi.models;


import jakarta.persistence.*;
import lombok.*;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="User_info_id")
    private UserInfo userInfo;
}
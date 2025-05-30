package org.likelion.couplediray.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "couples")
@Getter
@Setter
@NoArgsConstructor
public class Couple {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String inviteCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;
    @OneToMany(mappedBy = "couple", cascade = CascadeType.ALL)
    private List<CoupleUser> coupleUsers = new ArrayList<>();
}
package io.homo_efficio.kpgaza.mvc.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class ChatRoom extends BaseEntity {

    @Id
//    @Column(columnDefinition = "BINARY(16)")  // for H2
    private UUID id;

    @NotNull
    @Column(length = 30)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private KUser owner;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatUser> chatUsers = new HashSet<>();


    public ChatRoom(UUID id, String name, KUser owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

    public boolean containsUser(KUser kUser) {
        return chatUsers.stream().anyMatch(cu -> cu.getChatter().equals(kUser));
    }
}

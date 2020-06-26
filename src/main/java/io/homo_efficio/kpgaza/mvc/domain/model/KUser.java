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
@Table(name = "k_user")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class KUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 20)
    private String name;

    @OneToMany(mappedBy = "chatter", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatUser> chatUsers = new HashSet<>();


    public KUser(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ChatRoom createChatRoom(String roomName) {
        return new ChatRoom(UUID.randomUUID(), roomName, this);
    }

    public ChatUser enterChatRoom(ChatRoom chatRoom) {
        return new ChatUser(null, chatRoom, this);
    }
}

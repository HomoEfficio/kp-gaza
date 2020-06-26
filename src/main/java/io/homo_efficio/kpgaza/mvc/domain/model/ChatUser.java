package io.homo_efficio.kpgaza.mvc.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@Entity
@Table(indexes = {
        @Index(name = "IDX_CHAT_USER__CHAT_ROOM", columnList = "chat_room_id"),
        @Index(name = "IDX_CHAT_USER__KUSER", columnList = "chatter_id"),
})
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class ChatUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "chatter_id")
    private KUser chatter;


    public ChatUser(Long id, ChatRoom chatRoom, KUser chatter) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.chatter = chatter;
    }
}

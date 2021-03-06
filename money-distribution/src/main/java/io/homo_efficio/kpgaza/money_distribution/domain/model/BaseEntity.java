package io.homo_efficio.kpgaza.money_distribution.domain.model;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Access(AccessType.FIELD)
@Getter
public abstract class BaseEntity implements Serializable {

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdDateTime;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    protected LocalDateTime lastModifiedDateTime;
}


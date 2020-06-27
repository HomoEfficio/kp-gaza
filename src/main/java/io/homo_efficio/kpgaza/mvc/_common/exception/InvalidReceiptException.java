package io.homo_efficio.kpgaza.mvc._common.exception;


import io.homo_efficio.kpgaza.mvc.domain.model.BaseEntity;
import io.homo_efficio.kpgaza.mvc.domain.model.Receipt;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-27
 */

public class InvalidReceiptException extends RuntimeException {

    private final Class<? extends BaseEntity> entityClazz = Receipt.class;

    private Long entityId;

    public InvalidReceiptException(String message) {
        super(message);
    }

    public Long getEntityId() {
        return entityId;
    }

    public Class<? extends BaseEntity> getEntityClazz() {
        return entityClazz;
    }

    public String getEntityClassSimpleName() {
        return entityClazz.getSimpleName();
    }
}

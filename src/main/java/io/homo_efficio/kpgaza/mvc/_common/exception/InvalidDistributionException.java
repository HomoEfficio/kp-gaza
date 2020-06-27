package io.homo_efficio.kpgaza.mvc._common.exception;


import io.homo_efficio.kpgaza.mvc.domain.model.BaseEntity;
import io.homo_efficio.kpgaza.mvc.domain.model.Distribution;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-27
 */

public class InvalidDistributionException extends RuntimeException {

    private final Class<? extends BaseEntity> entityClazz = Distribution.class;

    private Long entityId;

    public InvalidDistributionException(String message) {
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

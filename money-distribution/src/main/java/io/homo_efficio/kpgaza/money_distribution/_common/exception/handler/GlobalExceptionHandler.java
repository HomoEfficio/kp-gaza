package io.homo_efficio.kpgaza.money_distribution._common.exception.handler;

import io.homo_efficio.kpgaza.money_distribution._common.exception.EntityNotFoundException;
import io.homo_efficio.kpgaza.money_distribution._common.exception.InvalidDistributionException;
import io.homo_efficio.kpgaza.money_distribution._common.exception.InvalidReceiptException;
import io.homo_efficio.kpgaza.money_distribution._config.MessageConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-05-30
 */
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageConfig.MessageResolver messageResolver;

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("handleEntityNotFoundException: {}", e.getMessage());
        ErrorResponse errorResponse;
        if (e.getEntityId() == null) {
            errorResponse = ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND_WITH_NON_ID,
                    messageResolver.getMessage(ErrorCode.ENTITY_NOT_FOUND_WITH_NON_ID.getErrorCode(), e.getMessage()),
                    ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString());
        } else {
            errorResponse = ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND,
                    messageResolver.getMessage(ErrorCode.ENTITY_NOT_FOUND.getErrorCode(),
                            e.getEntityClassSimpleName(), e.getEntityId()),
                    ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString());
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDistributionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleInvalidDistributionException(InvalidDistributionException e) {
        log.error("handleInvalidDistributionException: {}", e.getMessage());
        ErrorResponse errorResponse;
        errorResponse = ErrorResponse.of(ErrorCode.INVALID_DISTRIBUTION,
                messageResolver.getMessage(ErrorCode.INVALID_DISTRIBUTION.getErrorCode(), e.getMessage()),
                ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidReceiptException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleInvalidReceiptException(InvalidReceiptException e) {
        log.error("handleInvalidReceiptException: {}", e.getMessage());
        ErrorResponse errorResponse;
        errorResponse = ErrorResponse.of(ErrorCode.INVALID_RECEIPT,
                messageResolver.getMessage(ErrorCode.INVALID_RECEIPT.getErrorCode(), e.getMessage()),
                ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

package br.com.vandersonsampaio.wishlist.exception;

import br.com.vandersonsampaio.wishlist.model.exceptions.ItemNotFoundException;
import br.com.vandersonsampaio.wishlist.model.exceptions.ProductExistsWishlistException;
import br.com.vandersonsampaio.wishlist.model.exceptions.ProductLimitException;
import br.com.vandersonsampaio.wishlist.model.error.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception ex) {
        logError(ex);
        return buildResponseEntity(new Error(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    @ExceptionHandler({ ProductLimitException.class, ItemNotFoundException.class, ProductExistsWishlistException.class })
    protected ResponseEntity<Object> handle(Exception ex) {
        logError(ex);
        return buildResponseEntity(new Error(HttpStatus.BAD_REQUEST, ex));
    }

    private void logError(Exception ex) {
        final StackTraceElement element = Arrays.stream(ex.getStackTrace()).findFirst().orElse(null);
        assert element != null;

        String nameClass  = element.getClassName();

        Logger logger = LoggerFactory.getLogger(nameClass);
        logger.error(ex.getMessage(), ex);
    }

    private ResponseEntity<Object> buildResponseEntity(Error apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}

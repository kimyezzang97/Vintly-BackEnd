package kr.vintly.common;

import kr.vintly.common.model.Message;
import kr.vintly.common.model.StatusEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice   // 전역 설정을 위한 annotaion
@RestController
public class ExceptionAdvisor {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity processValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
//            builder.append(" 입력된 값: [");
//            builder.append(fieldError.getRejectedValue());
//            builder.append("]");
        }

        return new ResponseEntity<>(
                Message.builder()
                        .status(StatusEnum.BAD_REQUEST)
                        .message(builder.toString())
                        .data("")
                        .build()
                ,HttpStatus.OK);
    }
}

package kr.vintly.common.exception;

import kr.vintly.common.exception.member.JoinConflictException;
import kr.vintly.common.exception.member.MemberNotExistException;
import kr.vintly.common.model.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice   // RestController 전체 적용, ControllerAdvice 도 존재함
public class ExceptionAdvisor {

    /**
    @Valid 또는 @Validated로 binding error 발생시 발생하는 예외
     */
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

        return ResponseEntity.ok(Message.builder()
                        .status(StatusEnum.BAD_REQUEST)
                        .message(builder.toString())
                        .data("")
                        .build());
    }

    /**
     * [Member]
     */
    // JOIN - ID, email, nickname 중 1개 중복
    @ExceptionHandler(JoinConflictException.class)
    protected ResponseEntity<Message> joinConflict(JoinConflictException exception) {
        return ResponseEntity.ok(Message.builder()
                .status(exception.getStatus())
                .message(exception.getStatus().getMessage())
                .data("")
                .build());
    }

    // 로그인, 메일인증 - ID 존재 안함
    @ExceptionHandler(MemberNotExistException.class)
    protected ResponseEntity<Message> memberNotExist(MemberNotExistException exception) {
        return ResponseEntity.ok(Message.builder()
                .status(exception.getStatus())
                .message(exception.getStatus().getMessage())
                .data("")
                .build());
    }

}

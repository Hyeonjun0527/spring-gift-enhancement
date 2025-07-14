
package gift.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("이미 등록된 이메일입니다.");
    }
}

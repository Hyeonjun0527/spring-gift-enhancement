package gift.exception;

public class UnAuthorizedException extends RuntimeException{
    public UnAuthorizedException(){
        super("로그인 정보가 없습니다.");
    }
}

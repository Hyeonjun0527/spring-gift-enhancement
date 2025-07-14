package gift.entity;

public class Member {

    private Long id;
    private String email;
    private String password;

    public Member() {
    }

    public Member(Long id, String email, String password) {

        this.id = id;
        this.email = email;
        this.password = password;
    }



    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // getter & setter
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

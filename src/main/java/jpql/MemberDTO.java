package jpql;

import lombok.Data;

@Data
public class MemberDTO {

    private int age;
    private String username;

    public MemberDTO(String username, int age) {
        this.age = age;
        this.username = username;
    }
}

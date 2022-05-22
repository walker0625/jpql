package jpql;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

// xml에도 공통으로 설정하여 관리 가능(우선권) > DATA JPA에서는 @Query로 사용 가능
@NamedQuery(name = "Member.findByUsername",
            query = "SELECT m FROM Member m WHERE m.username = :username")
@Entity
@Data
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}

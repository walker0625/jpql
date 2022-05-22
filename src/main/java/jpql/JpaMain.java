package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa"); // <persistence-unit name="jpa">
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Team team = new Team();
            team.setName("A");
            em.persist(team);

            Team team2 = new Team();
            team2.setName("B");
            em.persist(team2);

            Member member = new Member();
            member.setUsername("minwoo");
            member.setType(MemberType.ADMIN);
            member.setTeam(team);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("minwoo2");
            member2.setType(MemberType.ADMIN);
            member2.setTeam(team);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("minwoo3");
            member3.setType(MemberType.ADMIN);
            member3.setTeam(team2);
            em.persist(member3);

            em.flush();
            em.clear();

            // 벌크 연산 - FLUSH(1. 벌크 연산을 먼저 2. 벌크 연상 후 영속성 컨텍스트 초기화(em.clear()))
            em.createQuery("UPDATE Member m SET m.age = 20").executeUpdate();
            System.out.println("member3.getAge() = " + member3.getAge()); // 영속성 컨텍스트의 내용은 반영이 안되어 0
           
            //List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class).setParameter("username", "minwoo").getResultList();

            /*fetch join(한방 조인) > N+1 문제에 대한 해결책(LAZY로 가져오는 값을 미리 한번에 가져오는 것)
            String query = "SELECT t FROM Team t JOIN FETCH t.members"; // JOIN ROW 수 때문에 DISTINCT 필요
            String query = "SELECT t FROM Team t";
            List<Team> teamList = em.createQuery(query, Team.class).setFirstResult(0).setMaxResults(2).getResultList();

            for (Team team0 : teamList) {
                System.out.println("TeamName : " + team0.getName() + ", size : " + team0.getMembers().size());
            }*/
            
            /* 묵시적 조인은 비권장(join이 무분별하게 나갈 수 있음) > 명시적으로 join 키워드를 적어서 표현
            String query = "SELECT m.username FROM Team t " +
                           "JOIN t.members m"; // 명시적 join
            List<String> nameList = em.createQuery(query, String.class).getResultList();

            for (String s : nameList) {
                System.out.println("s = " + s);
            }*/

            // List<Member> resultList = em.createQuery("SELECT m FROM Member m LEFT JOIN m.team t ON t.name = 'teamA'", Member.class).getResultList();
            // List<Member> resultList = em.createQuery("SELECT m FROM Member m INNER JOIN m.team t", Member.class).getResultList();

            // List<MemberDTO> resultList = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
            //                                .setFirstResult(0).setMaxResults(3).getResultList(); // 구체적인 쿼리는 dbms에 맞게 생성되어 나감

            // Object singleObject = em.createQuery("select m from Member m").getSingleResult(); > 없거나 2개 이상이면 Exception 발생

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}

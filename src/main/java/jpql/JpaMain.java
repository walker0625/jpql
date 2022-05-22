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
            Member member = new Member();
            member.setUsername("minwoo");
            member.setType(MemberType.ADMIN);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("minwoo2");
            member2.setType(MemberType.ADMIN);
            em.persist(member2);

            em.flush();
            em.clear();

            String query = "SELECT group_concat(m.username) FROM Member m";
            List<String> nameList = em.createQuery(query, String.class).getResultList();

            for (String s : nameList) {
                System.out.println("s = " + s);
            }

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

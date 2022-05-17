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

            em.persist(member);

            List<Member> memberList = em.createQuery("select m from Member m WHERE m.username = :username", Member.class)
                                        .setParameter("username", "minwoo")
                                        .getResultList(); // 없으면 빈 list 반환

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

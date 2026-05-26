package jpabook.jpashop.member.repository;

import jakarta.persistence.NoResultException;
import jpabook.jpashop.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor // lombok 을 이용한 주입
public class MemberRepositoryImpl implements MemberRepository {
	
	// @PersistenceContext 이 어노테이션으로도 주입 가능
	// @Autowired 생성자 주입 -> lombok 으로 생성자 구현
	private final EntityManager em;
	
	@Override
	public Member save(Member member) {
		em.persist(member);
		return member;
	}
	
	@Override
	public Member findOneByMemberId(Long id) {
		return em.find(Member.class, id);
	}
	
	@Override
	public Member findOneByMemberName(String name) {
		List<Member> list = em.createQuery("select m from Member m where m.name = :name", Member.class)
		                      .setParameter("name", name)
		                      .getResultList();
		if (list.isEmpty())
		{
			return null;
		}
		else
		{
			return list.get(0);
		}
	}
	
	@Override
	public List<Member> findAll() {
		return em.createQuery("select m from Member m", Member.class)
		         .getResultList();
	}
	
	@Override
	public List<Member> findAllByName(String name) {
		return em.createQuery("select m from Member m where m.name = :name", Member.class)
		         .setParameter("name", name)
		         .getResultList();
	}
	
	@Override
	public void removeAllMember() {
		em.createQuery("delete from Member m");
	}
}
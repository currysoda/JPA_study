package jpabook.jpashop.member.service;

import java.util.List;
import jpabook.jpashop.member.entity.Member;

public interface MemberService {
	
	Long join(Member member);
	
	List<Member> getAllMembers();
	
	Member getMemberByMemberId(Long memberId);
	
	Member getMemberByMemberName(String memberName);
	
	void removeAll();
}

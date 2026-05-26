package jpabook.jpashop.member.service;

import jpabook.jpashop.member.entity.Member;
import jpabook.jpashop.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final MemberRepository memberRepository;
	
	/**
	 * 회원가입
	 */
	@Override
	@Transactional // 변경
	public Long join(Member member) {
		
		if (validateDuplicateMember(member))
		{
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
		else
		{
			memberRepository.save(member);
			return member.getId();
		}
	}
	
	// 중복 멤버 검증(이름으로)
	private boolean validateDuplicateMember(Member member) {
		
		Member resultOne = memberRepository.findOneByMemberName(member.getName());
		
		return resultOne != null;
	}
	
	/**
	 * 전체 회원 조회
	 */
	@Override
	public List<Member> getAllMembers() {
		return memberRepository.findAll();
	}
	
	// 회원 한명 조회
	@Override
	public Member getMemberByMemberId(Long memberId) {
		return memberRepository.findOneByMemberId(memberId);
	}
	
	// 회원 이름으로 조회
	@Override
	public Member getMemberByMemberName(String memberName) {
		return memberRepository.findOneByMemberName(memberName);
	}
	
	@Override
	public void removeAll() {
		memberRepository.removeAllMember();
	}
}
package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional  // test 시 트렌젝션은 기본 rollback
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    @Rollback(value = false)    // value false로 해야 insert문이 실행
    // test 시에는 em.persist 하고 commit flush를 하지 않음
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);

        //then
        // em.flush(); -> flush하여 db 저장 후 트렌젝션에 의해 데이터 롤백
        assertEquals(member, memberRepository.findOne(savedId));

    }

    @Test(expected = IllegalStateException.class)   // 에러가 터져서 나가는걸 캐치
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        /*
            memberService.join(member1);
            try {
                memberService.join(member2);    // 예외가 발생해야 한다.
            } catch (IllegalStateException e) {
                return;
            }
        */

        memberService.join(member1);
        memberService.join(member2);

        //then
        // fail 사용은 when에서 로직이 끝나야 하는데
        // fail 까지 넘어오면 안됌
        fail("예외가 발생해야 한다.");

    }

}
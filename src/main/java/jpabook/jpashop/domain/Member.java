package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded   // 내장 타입?
    private Address address;

    /***
     * 컬렉션은 필드에서 초기화 하자
     * 컬렉션은 필드에서 바로 초기화 하는 것이 안전하다.
     * null 문제에서 안전하다
     * 하이버네이트는 엔티티를 영속화 할 때, 컬렉션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경한다.
     * 만약 getOrders()처럼 임의의 메서드에서 컬렉션을 잘못 생성하면 하이버네이트 내부 메커니즘에 문제가 발생할 수 있다.
     * 따라서 필드레벨에서 생성하는 것이 가장 안전하고, 코드도 간결하다.
     *
     * ex)
     * Member member = new Member();
     * System.out.println(member.getOrders().getClass());
     *
     * em.persist(member);
     *
     * System.out.println(member.getOrders().getClass());
     *
     * //출력 결과
     * class java.util.ArrayList
     * class org.hibernate.collection.internal.PersistentBag    -> 하이버네이트에서 기존 컬렉션을 감싸서 바꿔버림
     *
     * 컬렉션을 가급적이면 변경하면 안 됌..
     * 처음 객체 생성하고 끝 / 있는걸 그대로 사용하는게 안전하다.
     */
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}

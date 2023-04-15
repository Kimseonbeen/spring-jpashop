package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    /***
     * @XToOne(OneToOne, ManyToOne) 관계는 기본이 즉시로딩이므로 직접 지연로딩으로 설정해야
     * 한다.
     */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /***
     * CascadeType.ALL
     * 원래 로직은 orderItems도 저장 그 뒤 추가 로직으로 order도 저장하지만,
     * order를 저장하면 자동으로 orderItems에도 저장이 된다.
     *
     * as-is)
     * persist(orderItemA)
     * persist(orderItemB)
     * persist(orderItemC)
     * persist(order)
     *
     * to-be)
     * persist(order)
     *
     *
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    /***
     * delivery로 마찬가지로
     * order 저장 시 delivery도 저장이 된다.
     */
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;    // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    //== 연관관계 메서드==//

    // member <--> order 양방향 연관관계
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    /*** ex)
     *  public static void main(String[] args) {
     *      Member m = new Member();
     *      Order o = new Order();
     *
     *      m.getOrders().add(order);
     *      o.setMember(member);
     *
     *      연관관계 메서드 사용 시
     *      setMember 메서드 사용 만으로
     *      member.getOrders에 Order값을 넣을 수 있다.
     *  }
     */

    // order <--> orderItem 양방향 연관관계
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    // order <--> delivery 양방향 연관관계
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }


}

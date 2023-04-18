package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        /**
         * 협업 시 누군가가  createOrderItem 메서드를 사용하지 않고
         * 따로 new orderItem() 사용하여 주문상품을 생성하게 되면
         * 추후에 유지보수하기가 힘듦
         * 그래서 orderItem 생성자를 막아야한다.
         * OrderItem orderItem1 = new OrderItem();
         * OrderItem 기본 생성자를 public이 아닌 protected로 만들어놓자
         * 롬복 어노테이션 중
         * @NoArgsConstructor(access = AccessLevel.PROTECTED) 사용하면 protected 생성자를 만들어준다
         */

        // 주문
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        /**
         * save 메소드를 order 한번만 날려도
         * domain에서 작성한 cascade 옵션으로 인해 
         * delivery, orderItem이 자동으로 save 됌
         */
        orderRepository.save(order);    

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
        /**
         * jpa를 활용할시 엔티티 데이터만 변경하면 jpa가 알아서
         * 바뀐 변경포인트들을 더티체킹(변경내역감지)하여 데이터베이스에 업데이트 쿼리가 자동으로 나감
         */
    }

    // 검색
/*
    public List<Order> findOrders(OrderSearch orderSearch) {

    }
*/


    // 검색
}

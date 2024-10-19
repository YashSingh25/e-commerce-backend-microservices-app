package com.yash.ecommerce.order;

import com.yash.ecommerce.order.customer.CustomerClient;
import com.yash.ecommerce.exception.BusinessException;
import com.yash.ecommerce.kafka.OrderConfirmation;
import com.yash.ecommerce.kafka.OrderProducer;
import com.yash.ecommerce.order.payment.PaymentClient;
import com.yash.ecommerce.order.payment.PaymentRequest;
import com.yash.ecommerce.orderline.OrderLineRequest;
import com.yash.ecommerce.orderline.OrderLineService;
import com.yash.ecommerce.order.product.ProductClient;
import com.yash.ecommerce.order.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public Integer createOrder(OrderRequest request) {
        // check the customer -> OpenFeign
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException(
                        "Cannot create order:: No Customer exists with the provided ID:: "+request.customerId()
                ));

        // purchase the products -> (Product M.S.) RestTemplate
        var purchasedProducts = this.productClient.purchaseProducts(request.products());

        // persist order
        var order = this.orderRepository.save(mapper.toOrder(request));

        for(PurchaseRequest purchaseRequest: request.products()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        // start payment process
        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        // send the order confirmation -> Notification (Apache Kafka)
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );

        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(mapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(mapper::fromOrder)
                .orElseThrow( () -> new EntityNotFoundException(String.format("No order found with the provided ID: %d" , orderId)));
    }
}

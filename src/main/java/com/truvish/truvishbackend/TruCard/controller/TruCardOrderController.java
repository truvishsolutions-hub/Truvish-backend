package com.truvish.truvishbackend.TruCard.controller;

import com.truvish.truvishbackend.TruCard.dto.TruCardOrderRequest;
import com.truvish.truvishbackend.TruCard.dto.TruCardOrderResponse;
import com.truvish.truvishbackend.TruCard.enums.TruCardOrderStatus;
import com.truvish.truvishbackend.TruCard.service.TruCardOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trucard/orders")
public class TruCardOrderController {

    private final TruCardOrderService orderService;

    public TruCardOrderController(
            TruCardOrderService orderService
    ) {
        this.orderService = orderService;
    }


    // =========================================================
    // CREATE TRUCARD ORDER
    // =========================================================

    @PostMapping
    public ResponseEntity<TruCardOrderResponse> createOrder(
            @RequestBody TruCardOrderRequest request
    ) {

        TruCardOrderResponse response =
                orderService.createOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // GET ALL ORDERS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<TruCardOrderResponse>> getAllOrders() {

        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }


    // =========================================================
    // GET ORDER BY ID
    // =========================================================

    @GetMapping("/{orderId}")
    public ResponseEntity<TruCardOrderResponse> getOrderById(
            @PathVariable Long orderId
    ) {

        return ResponseEntity.ok(
                orderService.getOrderById(orderId)
        );
    }


    // =========================================================
    // GET ORDERS BY CLIENT
    // =========================================================

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<TruCardOrderResponse>> getOrdersByClient(
            @PathVariable Long clientId
    ) {

        return ResponseEntity.ok(
                orderService.getOrdersByClient(clientId)
        );
    }


    // =========================================================
    // GET ORDERS BY STATUS
    // =========================================================

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TruCardOrderResponse>> getOrdersByStatus(
            @PathVariable TruCardOrderStatus status
    ) {

        List<TruCardOrderResponse> orders =
                orderService.getAllOrders()
                        .stream()
                        .filter(order ->
                                order.getStatus() == status
                        )
                        .toList();

        return ResponseEntity.ok(orders);
    }


    // =========================================================
    // GET ORDERS BY CLIENT + STATUS
    // =========================================================

    @GetMapping("/client/{clientId}/status/{status}")
    public ResponseEntity<List<TruCardOrderResponse>>
    getClientOrdersByStatus(
            @PathVariable Long clientId,
            @PathVariable TruCardOrderStatus status
    ) {

        return ResponseEntity.ok(
                orderService.getClientOrdersByStatus(
                        clientId,
                        status
                )
        );
    }


    // =========================================================
    // GET ORDERS BY CAMPAIGN
    // =========================================================

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<TruCardOrderResponse>>
    getCampaignOrders(
            @PathVariable Long campaignId
    ) {

        return ResponseEntity.ok(
                orderService.getCampaignOrders(
                        campaignId
                )
        );
    }


    // =========================================================
    // UPDATE ORDER STATUS
    // =========================================================

    @PutMapping("/{orderId}/status")
    public ResponseEntity<TruCardOrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam TruCardOrderStatus status
    ) {

        return ResponseEntity.ok(
                orderService.updateOrderStatus(
                        orderId,
                        status
                )
        );
    }


    // =========================================================
    // CANCEL ORDER
    // =========================================================

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<TruCardOrderResponse> cancelOrder(
            @PathVariable Long orderId
    ) {

        return ResponseEntity.ok(
                orderService.cancelOrder(orderId)
        );
    }
}
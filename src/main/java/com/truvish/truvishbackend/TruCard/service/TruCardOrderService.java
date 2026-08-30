package com.truvish.truvishbackend.TruCard.service;

import com.truvish.truvishbackend.TruCard.dto.TruCardOrderRequest;
import com.truvish.truvishbackend.TruCard.dto.TruCardOrderResponse;
import com.truvish.truvishbackend.TruCard.entity.TruCardCampaign;
import com.truvish.truvishbackend.TruCard.entity.TruCardCode;
import com.truvish.truvishbackend.TruCard.entity.TruCardOrder;
import com.truvish.truvishbackend.TruCard.enums.TruCardCodeStatus;
import com.truvish.truvishbackend.TruCard.enums.TruCardOrderStatus;
import com.truvish.truvishbackend.TruCard.repository.TruCardCampaignRepository;
import com.truvish.truvishbackend.TruCard.repository.TruCardCodeRepository;
import com.truvish.truvishbackend.TruCard.repository.TruCardOrderRepository;
import com.truvish.truvishbackend.client.Client;
import com.truvish.truvishbackend.client.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TruCardOrderService {

    private final TruCardOrderRepository orderRepository;
    private final TruCardCodeRepository codeRepository;
    private final TruCardCampaignRepository campaignRepository;
    private final ClientRepository clientRepository;

    public TruCardOrderService(
            TruCardOrderRepository orderRepository,
            TruCardCodeRepository codeRepository,
            TruCardCampaignRepository campaignRepository,
            ClientRepository clientRepository
    ) {
        this.orderRepository = orderRepository;
        this.codeRepository = codeRepository;
        this.campaignRepository = campaignRepository;
        this.clientRepository = clientRepository;
    }


    // =========================================================
    // CREATE ORDER
    // =========================================================

    @Transactional
    public TruCardOrderResponse createOrder(
            TruCardOrderRequest request
    ) {

        if (request == null) {
            throw new RuntimeException(
                    "TruCard order request is required"
            );
        }


        // =====================================================
        // 1. VALIDATE CLIENT
        // =====================================================

        if (request.getClientId() == null) {

            throw new RuntimeException(
                    "Client ID is required"
            );
        }

        Client client = clientRepository
                .findById(request.getClientId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Client not found with id: "
                                        + request.getClientId()
                        )
                );


        // =====================================================
        // 2. VALIDATE CAMPAIGN
        // =====================================================

        if (request.getCampaignId() == null) {

            throw new RuntimeException(
                    "Campaign ID is required"
            );
        }

        TruCardCampaign campaign =
                campaignRepository
                        .findById(
                                request.getCampaignId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "TruCard Campaign not found with id: "
                                                + request.getCampaignId()
                                )
                        );


        // =====================================================
        // 3. CAMPAIGN MUST BELONG TO CLIENT
        // =====================================================

        if (campaign.getClientId() != null &&
                !request.getClientId()
                        .equals(campaign.getClientId())) {

            throw new RuntimeException(
                    "Selected campaign does not belong to this client"
            );
        }


        // =====================================================
        // 4. CAMPAIGN MUST BE ACTIVE
        // =====================================================

        if (!Boolean.TRUE.equals(
                campaign.getActive()
        )) {

            throw new RuntimeException(
                    "Selected campaign is not active"
            );
        }


        // =====================================================
        // 5. VALIDATE QUANTITY
        // =====================================================

        if (request.getQuantity() == null ||
                request.getQuantity() <= 0) {

            throw new RuntimeException(
                    "Quantity must be greater than 0"
            );
        }


        // =====================================================
        // 6. VALIDATE DENOMINATION
        // =====================================================

        if (request.getDenomination() == null ||
                request.getDenomination()
                        .compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Denomination must be greater than 0"
            );
        }


        // =====================================================
        // 7. VALIDATE VALIDITY
        // =====================================================

        if (request.getValidityMonths() != null &&
                request.getValidityMonths() <= 0) {

            throw new RuntimeException(
                    "Validity months must be greater than 0"
            );
        }


        // =====================================================
        // 8. CALCULATE TOTAL AMOUNT
        // =====================================================

        BigDecimal totalAmount =
                request.getDenomination()
                        .multiply(
                                BigDecimal.valueOf(
                                        request.getQuantity()
                                )
                        );


        // =====================================================
        // 9. GET CLIENT WALLET BALANCE
        // =====================================================

        BigDecimal balanceBefore =
                client.getBalance();

        if (balanceBefore == null) {
            balanceBefore = BigDecimal.ZERO;
        }


        // =====================================================
        // 10. CHECK SUFFICIENT BALANCE
        // =====================================================

        if (balanceBefore.compareTo(totalAmount) < 0) {

            throw new RuntimeException(
                    "Insufficient wallet balance. " +
                            "Required: ₹" + totalAmount +
                            ", Available: ₹" + balanceBefore
            );
        }


        // =====================================================
        // 11. CALCULATE BALANCE AFTER
        // =====================================================

        BigDecimal balanceAfter =
                balanceBefore.subtract(
                        totalAmount
                );


        // =====================================================
        // 12. CREATE ORDER
        // =====================================================

        TruCardOrder order =
                new TruCardOrder();

        order.setClientId(
                request.getClientId()
        );

        order.setCampaignId(
                request.getCampaignId()
        );

        order.setDenomination(
                request.getDenomination()
        );

        order.setQuantity(
                request.getQuantity()
        );

        order.setTotalAmount(
                totalAmount
        );

        order.setBalanceBefore(
                balanceBefore
        );

        order.setBalanceAfter(
                balanceAfter
        );

        order.setStatus(
                TruCardOrderStatus.COMPLETED
        );


        // =====================================================
        // 13. SAVE ORDER
        // =====================================================

        TruCardOrder savedOrder =
                orderRepository.save(order);


        // =====================================================
        // 14. DEDUCT WALLET BALANCE
        // =====================================================

        client.setBalance(
                balanceAfter
        );

        clientRepository.save(client);


        // =====================================================
        // 15. GENERATE TRUCARD CODES
        // =====================================================
        //
        // Every generated physical card:
        //
        // Serial Number
        // Reference Number
        // Redeem Code
        //
        // Status = INACTIVE
        //
        // =====================================================

        for (int i = 0;
             i < request.getQuantity();
             i++) {

            TruCardCode code =
                    new TruCardCode();


            // -------------------------------------------------
            // CLIENT
            // -------------------------------------------------

            code.setClientId(
                    request.getClientId()
            );


            // -------------------------------------------------
            // ORDER
            // -------------------------------------------------

            code.setOrderId(
                    savedOrder.getId()
            );


            // -------------------------------------------------
            // CAMPAIGN
            // -------------------------------------------------

            code.setCampaignId(
                    request.getCampaignId()
            );


            // -------------------------------------------------
            // SERIAL NUMBER
            //
            // Example:
            //
            // 583214739001
            // 583214739002
            // ...
            // 583214739008
            //
            // Then:
            //
            // 583214739010
            //
            // 009 is skipped.
            // -------------------------------------------------

            code.setSerialNumber(
                    generateNextSerialNumber()
            );


            // -------------------------------------------------
            // REFERENCE NUMBER
            //
            // UUID based
            //
            // Example:
            //
            // DD13-8F85-FA3F
            //
            // -------------------------------------------------

            code.setReferenceNumber(
                    generateUniqueReferenceNumber()
            );


            // -------------------------------------------------
            // REDEEM CODE
            //
            // Example:
            //
            // A7F2-91BC-4D83
            //
            // -------------------------------------------------

            code.setCodeNumber(
                    generateUniqueRedeemCode()
            );


            // -------------------------------------------------
            // DENOMINATION
            // -------------------------------------------------

            code.setDenomination(
                    request.getDenomination()
            );


            // -------------------------------------------------
            // NEW CARD = INACTIVE
            // -------------------------------------------------

            code.setStatus(
                    TruCardCodeStatus.INACTIVE
            );


            // -------------------------------------------------
            // VALIDITY
            // -------------------------------------------------

            if (request.getValidityMonths() != null) {

                code.setValidityMonths(
                        request.getValidityMonths()
                );
            }


            // -------------------------------------------------
            // EXPIRY
            //
            // Do NOT calculate activation expiry here.
            // Activation service will calculate:
            //
            // activatedAt + validityMonths
            //
            // -------------------------------------------------

            if (request.getExpiryDate() != null) {

                code.setExpiryDate(
                        request.getExpiryDate()
                );
            }


            // -------------------------------------------------
            // SAVE CODE
            // -------------------------------------------------

            codeRepository.save(code);
        }


        // =====================================================
        // 16. RETURN ORDER
        // =====================================================

        return mapToResponse(
                savedOrder
        );
    }


    // =========================================================
    // GET ALL ORDERS
    // =========================================================

    public List<TruCardOrderResponse> getAllOrders() {

        return orderRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // =========================================================
    // GET ORDER BY ID
    // =========================================================

    public TruCardOrderResponse getOrderById(
            Long orderId
    ) {

        TruCardOrder order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "TruCard Order not found with id: "
                                                + orderId
                                )
                        );

        return mapToResponse(order);
    }


    // =========================================================
    // GET ORDERS BY CLIENT
    // =========================================================

    public List<TruCardOrderResponse> getOrdersByClient(
            Long clientId
    ) {

        return orderRepository
                .findByClientIdOrderByCreatedAtDesc(
                        clientId
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // =========================================================
    // GET CLIENT ORDERS BY STATUS
    // =========================================================

    public List<TruCardOrderResponse> getClientOrdersByStatus(
            Long clientId,
            TruCardOrderStatus status
    ) {

        return orderRepository
                .findByClientIdAndStatusOrderByCreatedAtDesc(
                        clientId,
                        status
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // =========================================================
    // GET CAMPAIGN ORDERS
    // =========================================================

    public List<TruCardOrderResponse> getCampaignOrders(
            Long campaignId
    ) {

        return orderRepository
                .findByCampaignIdOrderByCreatedAtDesc(
                        campaignId
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // =========================================================
    // UPDATE ORDER STATUS
    // =========================================================

    @Transactional
    public TruCardOrderResponse updateOrderStatus(
            Long orderId,
            TruCardOrderStatus status
    ) {

        if (status == null) {

            throw new RuntimeException(
                    "Order status is required"
            );
        }

        TruCardOrder order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "TruCard Order not found with id: "
                                                + orderId
                                )
                        );

        order.setStatus(status);

        TruCardOrder updatedOrder =
                orderRepository.save(order);

        return mapToResponse(
                updatedOrder
        );
    }


    // =========================================================
    // CANCEL ORDER
    // =========================================================

    @Transactional
    public TruCardOrderResponse cancelOrder(
            Long orderId
    ) {

        TruCardOrder order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "TruCard Order not found with id: "
                                                + orderId
                                )
                        );


        if (order.getStatus()
                != TruCardOrderStatus.COMPLETED) {

            throw new RuntimeException(
                    "Only completed orders can be cancelled"
            );
        }


        // =====================================================
        // FIND CLIENT
        // =====================================================

        Client client =
                clientRepository
                        .findById(
                                order.getClientId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Client not found"
                                )
                        );


        // =====================================================
        // REFUND WALLET
        // =====================================================

        BigDecimal currentBalance =
                client.getBalance();

        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
        }

        BigDecimal refundedBalance =
                currentBalance.add(
                        order.getTotalAmount()
                );

        client.setBalance(
                refundedBalance
        );

        clientRepository.save(client);


        // =====================================================
        // UPDATE ORDER STATUS
        // =====================================================

        order.setStatus(
                TruCardOrderStatus.CANCELLED
        );

        TruCardOrder updatedOrder =
                orderRepository.save(order);


        // =====================================================
        // DELETE GENERATED CODES
        // =====================================================

        List<TruCardCode> codes =
                codeRepository.findByOrderId(
                        orderId
                );

        if (!codes.isEmpty()) {

            codeRepository.deleteAll(codes);
        }


        return mapToResponse(
                updatedOrder
        );
    }


    // =========================================================
    // GENERATE UNIQUE REFERENCE NUMBER
    // =========================================================
    //
    // UUID based.
    //
    // Example:
    //
    // DD13-8F85-FA3F
    //
    // =========================================================

    private String generateUniqueReferenceNumber() {

        String referenceNumber;

        do {

            String uuid =
                    UUID.randomUUID()
                            .toString()
                            .replace("-", "")
                            .toUpperCase();

            referenceNumber =
                    uuid.substring(0, 4)
                            + "-"
                            + uuid.substring(4, 8)
                            + "-"
                            + uuid.substring(8, 12);

        } while (
                codeRepository
                        .existsByReferenceNumber(
                                referenceNumber
                        )
        );

        return referenceNumber;
    }


    // =========================================================
    // GENERATE UNIQUE REDEEM CODE
    // =========================================================
    //
    // Customer ko redeem karne ke liye ye code milega.
    //
    // Example:
    //
    // A7F2-91BC-4D83
    //
    // =========================================================

    private String generateUniqueRedeemCode() {

        String codeNumber;

        do {

            String uuid =
                    UUID.randomUUID()
                            .toString()
                            .replace("-", "")
                            .toUpperCase();

            codeNumber =
                    uuid.substring(0, 4)
                            + "-"
                            + uuid.substring(4, 8)
                            + "-"
                            + uuid.substring(8, 12);

        } while (
                codeRepository.existsByCodeNumber(
                        codeNumber
                )
        );

        return codeNumber;
    }


    // =========================================================
    // GENERATE NEXT SERIAL NUMBER
    // =========================================================
    //
    // 12 digit serial.
    //
    // Example:
    //
    // 583214739001
    // 583214739002
    // 583214739003
    //
    // ...
    //
    // 583214739008
    //
    // next:
    //
    // 583214739010
    //
    // 009 SKIP
    //
    // Then:
    //
    // 011
    // 012
    //
    // ...
    //
    // 018
    //
    // next:
    //
    // 020
    //
    // =========================================================

    @Transactional
    private synchronized String generateNextSerialNumber() {

        final String SERIAL_PREFIX =
                "583214739";

        TruCardCode latestCode =
                codeRepository
                        .findTopByOrderBySerialNumberDesc()
                        .orElse(null);


        // -----------------------------------------------------
        // FIRST SERIAL
        // -----------------------------------------------------

        if (latestCode == null ||
                latestCode.getSerialNumber() == null ||
                latestCode.getSerialNumber().isBlank()) {

            return SERIAL_PREFIX + "001";
        }


        // -----------------------------------------------------
        // READ LAST 3 DIGITS
        // -----------------------------------------------------

        String latestSerial =
                latestCode.getSerialNumber();

        String lastThree =
                latestSerial.substring(
                        latestSerial.length() - 3
                );

        int currentSequence;

        try {

            currentSequence =
                    Integer.parseInt(lastThree);

        } catch (NumberFormatException e) {

            throw new RuntimeException(
                    "Invalid TruCard serial number: "
                            + latestSerial
            );
        }


        // -----------------------------------------------------
        // NEXT SEQUENCE
        // -----------------------------------------------------

        int nextSequence =
                currentSequence + 1;


        // -----------------------------------------------------
        // SKIP 009, 019, 029, 039...
        // -----------------------------------------------------

        if (nextSequence % 10 == 9) {
            nextSequence++;
        }


        // -----------------------------------------------------
        // MAXIMUM
        // -----------------------------------------------------

        if (nextSequence > 999) {

            throw new RuntimeException(
                    "TruCard serial number sequence limit reached"
            );
        }


        // -----------------------------------------------------
        // CREATE SERIAL
        // -----------------------------------------------------

        String nextSerial =
                SERIAL_PREFIX +
                        String.format(
                                "%03d",
                                nextSequence
                        );


        // -----------------------------------------------------
        // EXTRA UNIQUE CHECK
        // -----------------------------------------------------

        while (
                codeRepository
                        .existsBySerialNumber(
                                nextSerial
                        )
        ) {

            nextSequence++;

            if (nextSequence % 10 == 9) {
                nextSequence++;
            }

            if (nextSequence > 999) {

                throw new RuntimeException(
                        "TruCard serial number sequence limit reached"
                );
            }

            nextSerial =
                    SERIAL_PREFIX +
                            String.format(
                                    "%03d",
                                    nextSequence
                            );
        }


        return nextSerial;
    }


    // =========================================================
    // ENTITY → RESPONSE
    // =========================================================

    private TruCardOrderResponse mapToResponse(
            TruCardOrder order
    ) {

        TruCardOrderResponse response =
                new TruCardOrderResponse();

        response.setId(
                order.getId()
        );

        response.setClientId(
                order.getClientId()
        );

        response.setCampaignId(
                order.getCampaignId()
        );

        response.setDenomination(
                order.getDenomination()
        );

        response.setQuantity(
                order.getQuantity()
        );

        response.setTotalAmount(
                order.getTotalAmount()
        );

        response.setBalanceBefore(
                order.getBalanceBefore()
        );

        response.setBalanceAfter(
                order.getBalanceAfter()
        );

        response.setStatus(
                order.getStatus()
        );

        response.setCreatedAt(
                order.getCreatedAt()
        );

        response.setUpdatedAt(
                order.getUpdatedAt()
        );

        return response;
    }
}
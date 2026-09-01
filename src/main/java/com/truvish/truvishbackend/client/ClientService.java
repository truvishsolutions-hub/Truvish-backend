package com.truvish.truvishbackend.client;

import com.truvish.truvishbackend.TruOpeAdmin.TruvishCode;
import com.truvish.truvishbackend.TruOpeAdmin.TruvishCodeRepository;
import com.truvish.truvishbackend.common.FileStorageService;
import com.truvish.truvishbackend.redemption.UserRedemption;
import com.truvish.truvishbackend.redemption.UserRedemptionRepository;
import com.truvish.truvishbackend.wallet.TxnStatus;
import com.truvish.truvishbackend.wallet.TxnType;
import com.truvish.truvishbackend.wallet.WalletTransaction;
import com.truvish.truvishbackend.wallet.WalletTransactionRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository repo;

    private final FileStorageService storage;

    private final WalletTransactionRepository walletRepo;

    private final UserRedemptionRepository userRedemptionRepo;

    private final TruvishCodeRepository truvishCodeRepo;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ClientService(
            ClientRepository repo,
            FileStorageService storage,
            WalletTransactionRepository walletRepo,
            UserRedemptionRepository userRedemptionRepo,
            TruvishCodeRepository truvishCodeRepo
    ) {

        this.repo = repo;
        this.storage = storage;
        this.walletRepo = walletRepo;
        this.userRedemptionRepo = userRedemptionRepo;
        this.truvishCodeRepo = truvishCodeRepo;
    }


    // =========================================================
    // EXISTS BY MOBILE
    // =========================================================

    public boolean existsByMobile(
            String mobileNumber
    ) {

        return repo.existsByMobileNumber(
                mobileNumber
        );
    }


    // =========================================================
    // GET BY MOBILE
    // =========================================================

    public Client getByMobile(
            String mobileNumber
    ) {

        return repo.findByMobileNumber(
                mobileNumber
        ).orElseThrow(
                () -> new RuntimeException(
                        "Client not found: "
                                + mobileNumber
                )
        );
    }


    // =========================================================
    // CREATE CLIENT
    // =========================================================

    public Client create(
            ClientRequest req,
            MultipartFile logo
    ) {

        try {

            Client client =
                    new Client();


            // -------------------------------------------------
            // BASIC INFORMATION
            // -------------------------------------------------

            client.setMobileNumber(
                    req.getMobileNumber()
            );

            client.setCompanyName(
                    req.getCompanyName()
            );

            client.setClientName(
                    req.getClientName()
            );

            client.setEmail(
                    req.getEmail()
            );


            // -------------------------------------------------
            // INITIAL BALANCE
            // -------------------------------------------------

            if (req.getBalance() != null) {

                client.setBalance(
                        req.getBalance()
                );

            } else {

                client.setBalance(
                        new BigDecimal("1000.00")
                );
            }


            // -------------------------------------------------
            // LOGO
            // -------------------------------------------------

            if (
                    logo != null
                            && !logo.isEmpty()
            ) {

                client.setLogoImg(
                        storage.storeFile(logo)
                );
            }


            // -------------------------------------------------
            // SAVE CLIENT
            // -------------------------------------------------

            Client saved =
                    repo.save(client);


            // -------------------------------------------------
            // WELCOME WALLET TRANSACTION
            // -------------------------------------------------

            WalletTransaction transaction =
                    new WalletTransaction();

            transaction.setClient(saved);

            transaction.setTxnDateTime(
                    LocalDateTime.now()
            );

            transaction.setAmount(
                    saved.getBalance()
            );

            transaction.setType(
                    TxnType.CREDIT
            );

            transaction.setDescription(
                    "Truvish Gifts"
            );

            transaction.setReferenceType(
                    "SYSTEM"
            );

            transaction.setReferenceId(
                    "WELCOME"
            );

            transaction.setStatus(
                    TxnStatus.SUCCESS
            );

            walletRepo.save(transaction);


            return saved;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to create client: "
                            + e.getMessage(),
                    e
            );
        }
    }


    // =========================================================
    // GET CLIENT BY ID
    // =========================================================

    public Client get(
            Long id
    ) {

        return repo.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Client not found: "
                                        + id
                        )
                );
    }


    // =========================================================
    // GET ALL CLIENTS
    // =========================================================

    public List<Client> list() {

        return repo.findAll();
    }


    // =========================================================
    // SEARCH CLIENTS
    // =========================================================

    public List<Client> search(
            String value
    ) {

        if (
                value == null
                        || value.trim().isEmpty()
        ) {

            return repo.findAll();
        }


        return repo.searchClients(
                value.trim()
        );
    }


    // =========================================================
    // SEARCH CLIENTS - PAGINATED
    // =========================================================

    public Page<Client> search(
            String value,
            Pageable pageable
    ) {

        if (
                value == null
                        || value.trim().isEmpty()
        ) {

            return repo.findAll(pageable);
        }


        return repo.searchClients(
                value.trim(),
                pageable
        );
    }


    // =========================================================
    // CLIENT OVERVIEW
    //
    // DIGITAL CODES ONLY
    // =========================================================

    public List<ClientOverviewResponse> listOverview() {

        List<UserRedemption> allRedemptions =
                userRedemptionRepo.findAll();

        List<TruvishCode> allCodes =
                truvishCodeRepo.findAll();


        return repo.findAll()
                .stream()
                .map(client -> {

                    Long clientId =
                            client.getId();


                    // -------------------------------------------------
                    // TOTAL LOAD
                    // -------------------------------------------------

                    BigDecimal totalLoad =
                            walletRepo.sumTotalLoadByClientId(
                                    clientId
                            );

                    if (totalLoad == null) {

                        totalLoad =
                                BigDecimal.ZERO;
                    }


                    // -------------------------------------------------
                    // DIGITAL CODES DISTRIBUTED
                    // -------------------------------------------------

                    Long codesDistributed =
                            truvishCodeRepo.countByClientId(
                                    clientId
                            );

                    if (codesDistributed == null) {

                        codesDistributed =
                                0L;
                    }


                    // -------------------------------------------------
                    // DIGITAL DISTRIBUTED VALUE
                    //
                    // Repository returns Long.
                    // -------------------------------------------------

                    Long distributedValue =
                            truvishCodeRepo
                                    .sumDistributedValueByClientId(
                                            clientId
                                    );

                    if (distributedValue == null) {

                        distributedValue =
                                0L;
                    }


                    // -------------------------------------------------
                    // DIGITAL CODE NUMBERS
                    // -------------------------------------------------

                    Set<String> clientCodes =
                            allCodes
                                    .stream()
                                    .filter(
                                            code ->
                                                    code.getClientId() != null
                                                            &&
                                                            code.getClientId()
                                                                    .equals(
                                                                            clientId
                                                                    )
                                    )
                                    .map(
                                            TruvishCode
                                                    ::getTruvishIdCodeNumber
                                    )
                                    .filter(
                                            code ->
                                                    code != null
                                                            &&
                                                            !code.isBlank()
                                    )
                                    .collect(
                                            Collectors.toSet()
                                    );


                    // -------------------------------------------------
                    // REDEEMED COUNT
                    // -------------------------------------------------

                    Long redeemedCount =
                            allRedemptions
                                    .stream()
                                    .filter(
                                            redemption ->
                                                    redemption
                                                            .getUserTruvishCode()
                                                            != null
                                                            &&
                                                            clientCodes.contains(
                                                                    redemption
                                                                            .getUserTruvishCode()
                                                            )
                                    )
                                    .count();


                    // -------------------------------------------------
                    // REDEEMED AMOUNT
                    // -------------------------------------------------

                    Long redeemedAmount =
                            allRedemptions
                                    .stream()
                                    .filter(
                                            redemption ->
                                                    redemption
                                                            .getUserTruvishCode()
                                                            != null
                                                            &&
                                                            clientCodes.contains(
                                                                    redemption
                                                                            .getUserTruvishCode()
                                                            )
                                    )
                                    .map(
                                            redemption -> {

                                                Long value =
                                                        redemption
                                                                .getUserBrandValue();

                                                return value == null
                                                        ? 0L
                                                        : value;
                                            }
                                    )
                                    .reduce(
                                            0L,
                                            Long::sum
                                    );


                    // -------------------------------------------------
                    // RESPONSE
                    // -------------------------------------------------

                    return new ClientOverviewResponse(

                            clientId,

                            client.getMobileNumber(),

                            client.getCompanyName(),

                            client.getClientName(),

                            client.getEmail(),

                            client.getLogoImg(),

                            client.getBalance(),

                            totalLoad,

                            codesDistributed,

                            distributedValue,

                            redeemedCount,

                            redeemedAmount,

                            client.getCreatedAt()
                    );

                })
                .toList();
    }


    // =========================================================
    // DASHBOARD SUMMARY
    //
    // DIGITAL DISTRIBUTION ONLY
    // =========================================================

    public DashboardSummaryResponse
    getDashboardSummary() {


        // ---------------------------------------------------------
        // TOTAL CLIENTS
        // ---------------------------------------------------------

        Long totalClients =
                repo.count();


        // ---------------------------------------------------------
        // ALL REDEMPTIONS
        // ---------------------------------------------------------

        List<UserRedemption> allRedemptions =
                userRedemptionRepo.findAll();


        // ---------------------------------------------------------
        // UNIQUE USERS
        // ---------------------------------------------------------

        Set<String> uniqueUsers =
                new HashSet<>();


        allRedemptions.forEach(
                redemption -> {

                    String phone =
                            redemption
                                    .getUserPhoneNumber();

                    if (
                            phone != null
                                    && !phone.isBlank()
                    ) {

                        uniqueUsers.add(
                                phone.trim()
                        );
                    }
                }
        );


        Long totalUsers =
                (long) uniqueUsers.size();


        // ---------------------------------------------------------
        // TOTAL CURRENT CLIENT BALANCE
        // ---------------------------------------------------------

        BigDecimal totalCurrentBalance =
                repo.findAll()
                        .stream()
                        .map(
                                client ->
                                        client.getBalance() == null
                                                ? BigDecimal.ZERO
                                                : client.getBalance()
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );


        // ---------------------------------------------------------
        // TOTAL LOAD VALUE
        // ---------------------------------------------------------

        BigDecimal totalLoadValue =
                walletRepo.sumAllLoadedValue();

        if (totalLoadValue == null) {

            totalLoadValue =
                    BigDecimal.ZERO;
        }


        // ---------------------------------------------------------
        // TOTAL DIGITAL CODES DISTRIBUTED
        // ---------------------------------------------------------

        Long totalCodesDistributed =
                truvishCodeRepo
                        .countAllDistributedCodes();

        if (totalCodesDistributed == null) {

            totalCodesDistributed =
                    0L;
        }


        // ---------------------------------------------------------
        // TOTAL DIGITAL DISTRIBUTED VALUE
        // ---------------------------------------------------------

        Long totalDistributedValue =
                truvishCodeRepo
                        .sumAllDistributedValue();

        if (totalDistributedValue == null) {

            totalDistributedValue =
                    0L;
        }


        // ---------------------------------------------------------
        // TOTAL REDEEMED COUNT
        // ---------------------------------------------------------

        Long totalRedeemedCount =
                (long) allRedemptions.size();


        // ---------------------------------------------------------
        // TOTAL REDEEMED AMOUNT
        // ---------------------------------------------------------

        Long totalRedeemedAmount =
                allRedemptions
                        .stream()
                        .map(
                                item -> {

                                    Long value =
                                            item.getUserBrandValue();

                                    return value == null
                                            ? 0L
                                            : value;
                                }
                        )
                        .reduce(
                                0L,
                                Long::sum
                        );


        // ---------------------------------------------------------
        // RESPONSE
        // ---------------------------------------------------------

        return new DashboardSummaryResponse(

                totalClients == null
                        ? 0L
                        : totalClients,

                totalUsers,

                totalCurrentBalance,

                totalLoadValue,

                totalCodesDistributed,

                totalDistributedValue,

                totalRedeemedCount,

                totalRedeemedAmount
        );
    }


    // =========================================================
    // UPDATE CLIENT
    // =========================================================

    public Client update(
            Long id,
            ClientRequest req,
            MultipartFile logo
    ) {

        try {

            Client client =
                    get(id);


            // -------------------------------------------------
            // BASIC INFORMATION
            // -------------------------------------------------

            client.setMobileNumber(
                    req.getMobileNumber()
            );

            client.setCompanyName(
                    req.getCompanyName()
            );

            client.setClientName(
                    req.getClientName()
            );

            client.setEmail(
                    req.getEmail()
            );


            // -------------------------------------------------
            // BALANCE
            // -------------------------------------------------

            if (req.getBalance() != null) {

                client.setBalance(
                        req.getBalance()
                );
            }


            // -------------------------------------------------
            // LOGO
            // -------------------------------------------------

            if (
                    logo != null
                            && !logo.isEmpty()
            ) {

                if (
                        client.getLogoImg() != null
                                && !client.getLogoImg().isBlank()
                ) {

                    storage.delete(
                            client.getLogoImg()
                    );
                }


                client.setLogoImg(
                        storage.storeFile(logo)
                );
            }


            // -------------------------------------------------
            // SAVE
            // -------------------------------------------------

            return repo.save(client);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to update client: "
                            + e.getMessage(),
                    e
            );
        }
    }


    // =========================================================
    // DELETE CLIENT
    // =========================================================

    public void delete(
            Long id
    ) {

        Client client =
                get(id);


        // -------------------------------------------------
        // DELETE LOGO
        // -------------------------------------------------

        if (
                client.getLogoImg() != null
                        && !client.getLogoImg().isBlank()
        ) {

            storage.delete(
                    client.getLogoImg()
            );
        }


        // -------------------------------------------------
        // DELETE CLIENT
        // -------------------------------------------------

        repo.deleteById(id);
    }
}
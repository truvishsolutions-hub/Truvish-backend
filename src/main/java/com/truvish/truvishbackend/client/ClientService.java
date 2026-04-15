package com.truvish.truvishbackend.client;

import com.truvish.truvishbackend.TruvishCode.TruvishCodeRepository;
import com.truvish.truvishbackend.common.FileStorageService;
import com.truvish.truvishbackend.redemption.UserRedemptionRepository;
import com.truvish.truvishbackend.wallet.TxnStatus;
import com.truvish.truvishbackend.wallet.TxnType;
import com.truvish.truvishbackend.wallet.WalletTransaction;
import com.truvish.truvishbackend.wallet.WalletTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepository repo;
    private final FileStorageService storage;
    private final WalletTransactionRepository walletRepo;
    private final UserRedemptionRepository userRedemptionRepo;
    private final TruvishCodeRepository truvishCodeRepo;

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

    public boolean existsByMobile(String mobileNumber) {
        return repo.existsByMobileNumber(mobileNumber);
    }

    public Client getByMobile(String mobileNumber) {
        return repo.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("Client not found: " + mobileNumber));
    }

    public Client create(ClientRequest req, MultipartFile logo) {
        try {
            Client c = new Client();

            c.setMobileNumber(req.getMobileNumber());
            c.setCompanyName(req.getCompanyName());
            c.setClientName(req.getClientName());
            c.setEmail(req.getEmail());

            if (req.getBalance() != null) {
                c.setBalance(req.getBalance());
            } else {
                c.setBalance(new BigDecimal("1000.00"));
            }

            if (logo != null && !logo.isEmpty()) {
                c.setLogoImg(storage.storeFile(logo));
            }

            Client saved = repo.save(c);

            WalletTransaction tx = new WalletTransaction();
            tx.setClient(saved);
            tx.setTxnDateTime(LocalDateTime.now());
            tx.setAmount(saved.getBalance());
            tx.setType(TxnType.CREDIT);
            tx.setDescription("Truvish Gifts");
            tx.setReferenceType("SYSTEM");
            tx.setReferenceId("WELCOME");
            tx.setStatus(TxnStatus.SUCCESS);

            walletRepo.save(tx);

            return saved;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create client: " + e.getMessage(), e);
        }
    }

    public Client get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found: " + id));
    }

    public List<Client> list() {
        return repo.findAll();
    }

    public List<ClientOverviewResponse> listOverview() {
        return repo.findAll().stream()
                .map(client -> {
                    BigDecimal totalLoad = walletRepo.sumTotalLoadByClientId(client.getId());
                    if (totalLoad == null) totalLoad = BigDecimal.ZERO;

                    Long codesDistributed = truvishCodeRepo.countByClientId(client.getId());
                    Long distributedValue = truvishCodeRepo.sumDistributedValueByClientId(client.getId());
                    if (distributedValue == null) distributedValue = 0L;

                    Long redeemedCount = userRedemptionRepo.countByClientId(client.getId());
                    Long redeemedAmount = userRedemptionRepo.sumRedeemedAmountByClientId(client.getId());
                    if (redeemedAmount == null) redeemedAmount = 0L;

                    return new ClientOverviewResponse(
                            client.getId(),
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

    public DashboardSummaryResponse getDashboardSummary() {
        Long totalClients = repo.count();
        Long totalUsers = userRedemptionRepo.countDistinctUsers();

        BigDecimal totalCurrentBalance = repo.findAll().stream()
                .map(client -> client.getBalance() == null ? BigDecimal.ZERO : client.getBalance())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalLoadValue = walletRepo.sumAllLoadedValue();
        if (totalLoadValue == null) totalLoadValue = BigDecimal.ZERO;

        Long totalCodesDistributed = truvishCodeRepo.countAllDistributedCodes();
        Long totalDistributedValue = truvishCodeRepo.sumAllDistributedValue();
        Long totalRedeemedCount = (long) userRedemptionRepo.findAll().size();
        Long totalRedeemedAmount = userRedemptionRepo.findAll().stream()
                .map(item -> item.getUserBrandValue() == null ? 0L : item.getUserBrandValue())
                .reduce(0L, Long::sum);

        return new DashboardSummaryResponse(
                totalClients == null ? 0L : totalClients,
                totalUsers == null ? 0L : totalUsers,
                totalCurrentBalance,
                totalLoadValue,
                totalCodesDistributed == null ? 0L : totalCodesDistributed,
                totalDistributedValue == null ? 0L : totalDistributedValue,
                totalRedeemedCount == null ? 0L : totalRedeemedCount,
                totalRedeemedAmount == null ? 0L : totalRedeemedAmount
        );
    }

    public Client update(Long id, ClientRequest req, MultipartFile logo) {
        try {
            Client c = get(id);

            c.setMobileNumber(req.getMobileNumber());
            c.setCompanyName(req.getCompanyName());
            c.setClientName(req.getClientName());
            c.setEmail(req.getEmail());

            if (req.getBalance() != null) {
                c.setBalance(req.getBalance());
            }

            if (logo != null && !logo.isEmpty()) {
                if (c.getLogoImg() != null && !c.getLogoImg().isBlank()) {
                    storage.delete(c.getLogoImg());
                }
                c.setLogoImg(storage.storeFile(logo));
            }

            return repo.save(c);

        } catch (Exception e) {
            throw new RuntimeException("Failed to update client: " + e.getMessage(), e);
        }
    }

    public void delete(Long id) {
        Client c = get(id);

        if (c.getLogoImg() != null && !c.getLogoImg().isBlank()) {
            storage.delete(c.getLogoImg());
        }

        repo.deleteById(id);
    }
}
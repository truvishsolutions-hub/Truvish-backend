package com.truvish.truvishbackend.inventory;


import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BrandInventoryService {

    private final BrandInventoryRepository repo;

    public BrandInventoryService(BrandInventoryRepository repo) {
        this.repo = repo;
    }

    public BrandInventory save(BrandInventory b) {
        return repo.save(b);
    }

    public List<BrandInventory> all() {
        return repo.findAll();
    }
}
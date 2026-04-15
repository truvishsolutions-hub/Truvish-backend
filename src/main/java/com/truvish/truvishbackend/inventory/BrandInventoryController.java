package com.truvish.truvishbackend.inventory;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class BrandInventoryController {

    private final BrandInventoryService service;

    public BrandInventoryController(BrandInventoryService service) {
        this.service = service;
    }

    @PostMapping
    public BrandInventory add(@RequestBody BrandInventory b) {
        return service.save(b);
    }

    @GetMapping
    public List<BrandInventory> all() {
        return service.all();
    }
}


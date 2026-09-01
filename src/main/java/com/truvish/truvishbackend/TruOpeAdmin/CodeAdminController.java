package com.truvish.truvishbackend.TruOpeAdmin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/client")
public class CodeAdminController {

    private final TruvishCodeService service;

    public CodeAdminController(TruvishCodeService service) {
        this.service = service;
    }

    @PostMapping("/update")
    public ResponseEntity<List<TruvishCode>> updateClient(
            @RequestBody CodeAssignmentDto dto
    ) {

        return ResponseEntity.ok(
                service.updateClient(dto)
        );
    }
}
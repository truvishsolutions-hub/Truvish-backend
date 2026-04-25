package com.truvish.truvishbackend.DemoRequest.service;

import com.truvish.truvishbackend.DemoRequest.dto.DemoRequestDto;
import com.truvish.truvishbackend.DemoRequest.entity.DemoRequest;
import com.truvish.truvishbackend.DemoRequest.repository.DemoRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoRequestService {

    private final DemoRequestRepository demoRequestRepository;

    public DemoRequestService(DemoRequestRepository demoRequestRepository) {
        this.demoRequestRepository = demoRequestRepository;
    }

    public DemoRequest saveDemoRequest(DemoRequestDto dto) {
        DemoRequest request = new DemoRequest();
        request.setName(dto.getName().trim());
        request.setEmail(dto.getEmail().trim());
        request.setPhone(dto.getPhone().trim());

        return demoRequestRepository.save(request);
    }

    public List<DemoRequest> getAllDemoRequests() {
        return demoRequestRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .toList();
    }
}
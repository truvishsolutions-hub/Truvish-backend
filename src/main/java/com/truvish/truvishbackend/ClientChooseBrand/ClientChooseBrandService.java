package com.truvish.truvishbackend.ClientChooseBrand;

import com.truvish.truvishbackend.ClientChooseBrand.DTO.ClientChooseBrandResponse;
import com.truvish.truvishbackend.common.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class ClientChooseBrandService {

    private static final long MAX_IMAGE_SIZE = 1024 * 1024;

    private final ClientChooseBrandRepository repository;
    private final FileStorageService fileStorageService;

    public ClientChooseBrandService(
            ClientChooseBrandRepository repository,
            FileStorageService fileStorageService
    ) {
        this.repository = repository;
        this.fileStorageService = fileStorageService;
    }

    // =====================================================
    // SAVE BRAND
    // =====================================================
    private void validateBrandImage(MultipartFile image) {
        if (image == null || image.isEmpty()) return;

        if (image.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Brand image size must be 1MB or less");
        }

        String contentType = image.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed for brand upload");
        }
    }

    public ClientChooseBrandResponse saveBrand(
            String brandName,
            String category,
            String termsAndConditions,
            String howToRedeem,
            MultipartFile image
    ) {
        String imgFileName = null;

        try {
            if (image != null && !image.isEmpty()) {
                validateBrandImage(image);
                imgFileName = fileStorageService.storeFile(image);
            }

            ClientChooseBrand brand = new ClientChooseBrand();
            brand.setBrandName(brandName != null ? brandName.trim() : "");
            brand.setCategory(category != null ? category.trim() : "");
            brand.setBrandImg(imgFileName);
            brand.setTermsAndConditions(
                    termsAndConditions != null && !termsAndConditions.trim().isEmpty()
                            ? termsAndConditions.trim()
                            : null
            );
            brand.setHowToRedeem(
                    howToRedeem != null && !howToRedeem.trim().isEmpty()
                            ? howToRedeem.trim()
                            : null
            );

            ClientChooseBrand saved = repository.save(brand);
            return toResponse(saved);

        } catch (Exception e) {
            throw new RuntimeException("Failed to save brand image: " + e.getMessage(), e);
        }
    }

    // =====================================================
    // GET ALL BRANDS
    // =====================================================
    public List<ClientChooseBrandResponse> getAllBrands() {
        return repository.findAllByOrderByBrandNameAsc()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // =====================================================
    // GET ALL CATEGORIES
    // =====================================================
    public List<String> getAllCategories() {
        return repository.findAllByOrderByBrandNameAsc()
                .stream()
                .map(ClientChooseBrand::getCategory)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    // =====================================================
    // GROUP BRANDS BY CATEGORY
    // =====================================================
    public Map<String, List<ClientChooseBrandResponse>> getBrandsGroupedByCategory() {
        List<ClientChooseBrand> all = repository.findAllByOrderByBrandNameAsc();

        Map<String, List<ClientChooseBrandResponse>> grouped = new LinkedHashMap<>();

        all.stream()
                .collect(Collectors.groupingBy(
                        b -> {
                            String cat = b.getCategory();
                            if (cat == null || cat.trim().isEmpty()) {
                                return "Other";
                            }
                            return cat.trim();
                        },
                        TreeMap::new,
                        Collectors.toList()
                ))
                .forEach((category, brands) -> grouped.put(
                        category,
                        brands.stream()
                                .map(this::toResponse)
                                .collect(Collectors.toList())
                ));

        return grouped;
    }

    // =====================================================
    // DELETE BRAND
    // =====================================================
    public void deleteBrand(Long id) {
        ClientChooseBrand brand = repository.findById(id).orElse(null);

        if (brand != null) {
            if (brand.getBrandImg() != null && !brand.getBrandImg().isBlank()) {
                fileStorageService.delete(brand.getBrandImg());
            }
            repository.deleteById(id);
        }
    }

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================
    private ClientChooseBrandResponse toResponse(ClientChooseBrand brand) {
        String imgUrl = null;

        if (brand.getBrandImg() != null && !brand.getBrandImg().isBlank()) {
            imgUrl = "/uploads/" + brand.getBrandImg();
        }

        return new ClientChooseBrandResponse(
                brand.getId(),
                brand.getBrandName(),
                brand.getCategory(),
                imgUrl,
                brand.getTermsAndConditions(),
                brand.getHowToRedeem()
        );
    }
}
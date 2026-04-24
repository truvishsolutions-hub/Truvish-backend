package com.truvish.truvishbackend.admin;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

@Service
public class AdminConfigService {

    private static final long MAX_IMAGE_SIZE = 1024 * 1024;
    private static final List<String> ALLOWED_IMAGE_TYPES = List.of("image/jpeg", "image/png", "image/webp", "image/jpg", "image/gif");

    private final AdminConfigRepository repo;

    public AdminConfigService(AdminConfigRepository repo) {
        this.repo = repo;
    }

    public AdminConfig getConfig() {
        return repo.findTopByOrderByIdAsc()
                .orElseGet(() -> repo.save(new AdminConfig()));
    }

    public AdminConfig save(AdminConfig incoming) {
        AdminConfig existing = getConfig();

        if (incoming.getAdminName() != null) existing.setAdminName(incoming.getAdminName());

        if (incoming.getBanner1() != null) existing.setBanner1(incoming.getBanner1());
        if (incoming.getBanner2() != null) existing.setBanner2(incoming.getBanner2());
        if (incoming.getBanner3() != null) existing.setBanner3(incoming.getBanner3());
        if (incoming.getBanner4() != null) existing.setBanner4(incoming.getBanner4());

        if (incoming.getThemeName1() != null) existing.setThemeName1(incoming.getThemeName1());
        if (incoming.getThemeImg1() != null) existing.setThemeImg1(incoming.getThemeImg1());
        if (incoming.getImg1() != null) existing.setImg1(incoming.getImg1());
        if (incoming.getImg1Name() != null) existing.setImg1Name(incoming.getImg1Name());
        if (incoming.getImg2() != null) existing.setImg2(incoming.getImg2());
        if (incoming.getImg2Name() != null) existing.setImg2Name(incoming.getImg2Name());
        if (incoming.getImg3() != null) existing.setImg3(incoming.getImg3());
        if (incoming.getImg3Name() != null) existing.setImg3Name(incoming.getImg3Name());
        if (incoming.getImg4() != null) existing.setImg4(incoming.getImg4());
        if (incoming.getImg4Name() != null) existing.setImg4Name(incoming.getImg4Name());

        if (incoming.getThemeName2() != null) existing.setThemeName2(incoming.getThemeName2());
        if (incoming.getThemeImg2() != null) existing.setThemeImg2(incoming.getThemeImg2());
        if (incoming.getImg6() != null) existing.setImg6(incoming.getImg6());
        if (incoming.getImg6Name() != null) existing.setImg6Name(incoming.getImg6Name());
        if (incoming.getImg7() != null) existing.setImg7(incoming.getImg7());
        if (incoming.getImg7Name() != null) existing.setImg7Name(incoming.getImg7Name());
        if (incoming.getImg8() != null) existing.setImg8(incoming.getImg8());
        if (incoming.getImg8Name() != null) existing.setImg8Name(incoming.getImg8Name());
        if (incoming.getImg9() != null) existing.setImg9(incoming.getImg9());
        if (incoming.getImg9Name() != null) existing.setImg9Name(incoming.getImg9Name());

        if (incoming.getThemeName3() != null) existing.setThemeName3(incoming.getThemeName3());
        if (incoming.getThemeImg3() != null) existing.setThemeImg3(incoming.getThemeImg3());
        if (incoming.getImg11() != null) existing.setImg11(incoming.getImg11());
        if (incoming.getImg11Name() != null) existing.setImg11Name(incoming.getImg11Name());
        if (incoming.getImg12() != null) existing.setImg12(incoming.getImg12());
        if (incoming.getImg12Name() != null) existing.setImg12Name(incoming.getImg12Name());
        if (incoming.getImg13() != null) existing.setImg13(incoming.getImg13());
        if (incoming.getImg13Name() != null) existing.setImg13Name(incoming.getImg13Name());
        if (incoming.getImg14() != null) existing.setImg14(incoming.getImg14());
        if (incoming.getImg14Name() != null) existing.setImg14Name(incoming.getImg14Name());

        if (incoming.getThemeName4() != null) existing.setThemeName4(incoming.getThemeName4());
        if (incoming.getThemeImg4() != null) existing.setThemeImg4(incoming.getThemeImg4());
        if (incoming.getImg16() != null) existing.setImg16(incoming.getImg16());
        if (incoming.getImg16Name() != null) existing.setImg16Name(incoming.getImg16Name());
        if (incoming.getImg17() != null) existing.setImg17(incoming.getImg17());
        if (incoming.getImg17Name() != null) existing.setImg17Name(incoming.getImg17Name());
        if (incoming.getImg18() != null) existing.setImg18(incoming.getImg18());
        if (incoming.getImg18Name() != null) existing.setImg18Name(incoming.getImg18Name());
        if (incoming.getImg19() != null) existing.setImg19(incoming.getImg19());
        if (incoming.getImg19Name() != null) existing.setImg19Name(incoming.getImg19Name());

        return repo.save(existing);
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) return;

        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Image size must be 1MB or less");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Only JPG, JPEG, PNG, WEBP or GIF images are allowed");
        }
    }

    public String saveFile(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) return null;

        validateImage(file);

        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        new File(uploadDir).mkdirs();

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        file.transferTo(new File(uploadDir + fileName));

        return "/uploads/" + fileName;
    }

    public void updateImage(Consumer<String> setImg, MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()) {
            setImg.accept(saveFile(file));
        }
    }

    public void updateImageWithName(
            Consumer<String> setImg,
            Consumer<String> setName,
            MultipartFile file,
            String name
    ) throws Exception {
        if (file != null && !file.isEmpty()) {
            setImg.accept(saveFile(file));
        }

        if (name != null) {
            setName.accept(name);
        }
    }
}
package com.truvish.truvishbackend.admin;

import jakarta.persistence.*;

@Entity
@Table(name = "admin_config")
public class AdminConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin_name")
    private String adminName;

    @Column(name = "banner1")
    private String banner1;

    @Column(name = "banner2")
    private String banner2;

    @Column(name = "banner3")
    private String banner3;

    @Column(name = "banner4")
    private String banner4;

    // ===== THEME 1 =====
    @Column(name = "theme_name1")
    private String themeName1;

    @Column(name = "theme_img1")
    private String themeImg1;

    @Column(name = "img1")
    private String img1;

    @Column(name = "img1_name")
    private String img1Name;

    @Column(name = "img2")
    private String img2;

    @Column(name = "img2_name")
    private String img2Name;

    @Column(name = "img3")
    private String img3;

    @Column(name = "img3_name")
    private String img3Name;

    @Column(name = "img4")
    private String img4;

    @Column(name = "img4_name")
    private String img4Name;

    // ===== THEME 2 =====
    @Column(name = "theme_name2")
    private String themeName2;

    @Column(name = "theme_img2")
    private String themeImg2;

    @Column(name = "img6")
    private String img6;

    @Column(name = "img6_name")
    private String img6Name;

    @Column(name = "img7")
    private String img7;

    @Column(name = "img7_name")
    private String img7Name;

    @Column(name = "img8")
    private String img8;

    @Column(name = "img8_name")
    private String img8Name;

    @Column(name = "img9")
    private String img9;

    @Column(name = "img9_name")
    private String img9Name;

    // ===== THEME 3 =====
    @Column(name = "theme_name3")
    private String themeName3;

    @Column(name = "theme_img3")
    private String themeImg3;

    @Column(name = "img11")
    private String img11;

    @Column(name = "img11_name")
    private String img11Name;

    @Column(name = "img12")
    private String img12;

    @Column(name = "img12_name")
    private String img12Name;

    @Column(name = "img13")
    private String img13;

    @Column(name = "img13_name")
    private String img13Name;

    @Column(name = "img14")
    private String img14;

    @Column(name = "img14_name")
    private String img14Name;

    // ===== THEME 4 =====
    @Column(name = "theme_name4")
    private String themeName4;

    @Column(name = "theme_img4")
    private String themeImg4;

    @Column(name = "img16")
    private String img16;

    @Column(name = "img16_name")
    private String img16Name;

    @Column(name = "img17")
    private String img17;

    @Column(name = "img17_name")
    private String img17Name;

    @Column(name = "img18")
    private String img18;

    @Column(name = "img18_name")
    private String img18Name;

    @Column(name = "img19")
    private String img19;

    @Column(name = "img19_name")
    private String img19Name;

    // =====================
    // GETTERS & SETTERS
    // =====================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAdminName() { return adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }

    public String getBanner1() { return banner1; }
    public void setBanner1(String banner1) { this.banner1 = banner1; }

    public String getBanner2() { return banner2; }
    public void setBanner2(String banner2) { this.banner2 = banner2; }

    public String getBanner3() { return banner3; }
    public void setBanner3(String banner3) { this.banner3 = banner3; }

    public String getBanner4() { return banner4; }
    public void setBanner4(String banner4) { this.banner4 = banner4; }

    // THEME 1
    public String getThemeName1() { return themeName1; }
    public void setThemeName1(String themeName1) { this.themeName1 = themeName1; }

    public String getThemeImg1() { return themeImg1; }
    public void setThemeImg1(String themeImg1) { this.themeImg1 = themeImg1; }

    public String getImg1() { return img1; }
    public void setImg1(String img1) { this.img1 = img1; }

    public String getImg1Name() { return img1Name; }
    public void setImg1Name(String img1Name) { this.img1Name = img1Name; }

    public String getImg2() { return img2; }
    public void setImg2(String img2) { this.img2 = img2; }

    public String getImg2Name() { return img2Name; }
    public void setImg2Name(String img2Name) { this.img2Name = img2Name; }

    public String getImg3() { return img3; }
    public void setImg3(String img3) { this.img3 = img3; }

    public String getImg3Name() { return img3Name; }
    public void setImg3Name(String img3Name) { this.img3Name = img3Name; }

    public String getImg4() { return img4; }
    public void setImg4(String img4) { this.img4 = img4; }

    public String getImg4Name() { return img4Name; }
    public void setImg4Name(String img4Name) { this.img4Name = img4Name; }

    // THEME 2
    public String getThemeName2() { return themeName2; }
    public void setThemeName2(String themeName2) { this.themeName2 = themeName2; }

    public String getThemeImg2() { return themeImg2; }
    public void setThemeImg2(String themeImg2) { this.themeImg2 = themeImg2; }

    public String getImg6() { return img6; }
    public void setImg6(String img6) { this.img6 = img6; }

    public String getImg6Name() { return img6Name; }
    public void setImg6Name(String img6Name) { this.img6Name = img6Name; }

    public String getImg7() { return img7; }
    public void setImg7(String img7) { this.img7 = img7; }

    public String getImg7Name() { return img7Name; }
    public void setImg7Name(String img7Name) { this.img7Name = img7Name; }

    public String getImg8() { return img8; }
    public void setImg8(String img8) { this.img8 = img8; }

    public String getImg8Name() { return img8Name; }
    public void setImg8Name(String img8Name) { this.img8Name = img8Name; }

    public String getImg9() { return img9; }
    public void setImg9(String img9) { this.img9 = img9; }

    public String getImg9Name() { return img9Name; }
    public void setImg9Name(String img9Name) { this.img9Name = img9Name; }

    // THEME 3
    public String getThemeName3() { return themeName3; }
    public void setThemeName3(String themeName3) { this.themeName3 = themeName3; }

    public String getThemeImg3() { return themeImg3; }
    public void setThemeImg3(String themeImg3) { this.themeImg3 = themeImg3; }

    public String getImg11() { return img11; }
    public void setImg11(String img11) { this.img11 = img11; }

    public String getImg11Name() { return img11Name; }
    public void setImg11Name(String img11Name) { this.img11Name = img11Name; }

    public String getImg12() { return img12; }
    public void setImg12(String img12) { this.img12 = img12; }

    public String getImg12Name() { return img12Name; }
    public void setImg12Name(String img12Name) { this.img12Name = img12Name; }

    public String getImg13() { return img13; }
    public void setImg13(String img13) { this.img13 = img13; }

    public String getImg13Name() { return img13Name; }
    public void setImg13Name(String img13Name) { this.img13Name = img13Name; }

    public String getImg14() { return img14; }
    public void setImg14(String img14) { this.img14 = img14; }

    public String getImg14Name() { return img14Name; }
    public void setImg14Name(String img14Name) { this.img14Name = img14Name; }

    // THEME 4
    public String getThemeName4() { return themeName4; }
    public void setThemeName4(String themeName4) { this.themeName4 = themeName4; }

    public String getThemeImg4() { return themeImg4; }
    public void setThemeImg4(String themeImg4) { this.themeImg4 = themeImg4; }

    public String getImg16() { return img16; }
    public void setImg16(String img16) { this.img16 = img16; }

    public String getImg16Name() { return img16Name; }
    public void setImg16Name(String img16Name) { this.img16Name = img16Name; }

    public String getImg17() { return img17; }
    public void setImg17(String img17) { this.img17 = img17; }

    public String getImg17Name() { return img17Name; }
    public void setImg17Name(String img17Name) { this.img17Name = img17Name; }

    public String getImg18() { return img18; }
    public void setImg18(String img18) { this.img18 = img18; }

    public String getImg18Name() { return img18Name; }
    public void setImg18Name(String img18Name) { this.img18Name = img18Name; }

    public String getImg19() { return img19; }
    public void setImg19(String img19) { this.img19 = img19; }

    public String getImg19Name() { return img19Name; }
    public void setImg19Name(String img19Name) { this.img19Name = img19Name; }
}
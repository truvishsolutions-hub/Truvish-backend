package com.truvish.truvishbackend.TruCard.enums;

public enum TruCardCodeStatus {

    // Code generate hua hai, lekin physical card abhi activate nahi hua
    INACTIVE,

    // Admin ne physical card ko activate kar diya
    ACTIVE,

    // Customer ne code redeem kar diya
    REDEEMED,

    // Validity khatam ho gayi aur amount wallet me wapas jana hai
    EXPIRED_BACK_TO_WALLET
}
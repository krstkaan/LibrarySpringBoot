package com.sufaka.libraryspringboot.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "65a1850a563bf90ca5da2022ebca8e42b300177635f0bbf0cf2b54cea8ecd6f210f110044ee2849f572e17d71ea2ea1aa4d2dc334405a60f337e8a2f3f1add62b15a5e45219a9b3c40d1a60a19897dc6638366a5fda03d62c9533d296b97ad4bd550967662b3e3c87802f17697bb03b3bb949234ed5c3bc4256927e9b930037b3dae01149ce43f85c0002d459505d22d4397180a66f35493fe4a492e354516770338813d71a66139ef13d86dba12960c162b57aeb59a3c90eced06fec393af7f98249608b1997ab794e0900cc824650b2e3ee3de48d74a0694aec6e588338e6402239b349cf0e726f6940651a220952d78e9e630298bfbd9cae9c3ae3bad0ca9"; // Güçlü bir anahtar belirleyin
    private static final long EXPIRATION_TIME = 86400000; // 1 gün (milisaniye cinsinden)

    public static String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

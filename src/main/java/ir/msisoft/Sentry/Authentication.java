package ir.msisoft.Sentry;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Authentication {
    private static final String KYE = "raja.ir";
    private static final String ISSUER = "raja.ir";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(KYE);

    public static String createToken(int userId) {
        try {
            Date exp = Date.from(LocalDateTime.now().plusHours(2).atZone(ZoneId.systemDefault()).toInstant());
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(new Date())
                    .withExpiresAt(exp)
                    .withClaim("userId", userId)
                    .sign(ALGORITHM);
        } catch (Exception exception){
            System.out.println("createToken: " + exception.getMessage());
        }
        return "";
    }

    public static boolean verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM)
                    .withIssuer(ISSUER)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception exception){
            System.out.println("verifyToken: " + exception.getMessage());
        }
        return false;
    }

    public static Integer getUserId(String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM)
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("userId").asInt();
        } catch (Exception exception){
            System.out.println("getUserId: " + exception.getMessage());
        }
        return 0;
    }
}

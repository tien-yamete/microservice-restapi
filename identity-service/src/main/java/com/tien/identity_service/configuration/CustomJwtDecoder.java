package com.tien.identity_service.configuration;

import java.text.ParseException;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.tien.identity_service.dto.request.IntrospectRequest;
import com.tien.identity_service.service.AuthenticationService;

// CustomJwtDecoder chịu trách nhiệm:
//         - Gọi AuthenticationService để introspect token (xác thực token với server).
//         - Nếu token hợp lệ, sử dụng NimbusJwtDecoder để parse và decode ra Jwt object.

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String signerKey;

    private final AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    public CustomJwtDecoder(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            // Gọi Auth service để kiểm tra token có hợp lệ không
            var response = authenticationService.introspect(
                    IntrospectRequest.builder().token(token).build());

            if (!response.isValid()) throw new JwtException("Token invalid");
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

        // Nếu decoder chưa được khởi tạo, thì khởi tạo mới
        if (Objects.isNull(nimbusJwtDecoder)) {
            // Tạo secret key từ signerKey (dùng HS512)
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        // Decode token và trả về Jwt object
        return nimbusJwtDecoder.decode(token);
    }
}

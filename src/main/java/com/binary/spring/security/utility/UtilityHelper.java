package com.binary.spring.security.utility;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Component;

import com.binary.spring.security.entity.AppUser;
import com.nimbusds.jose.jwk.source.ImmutableSecret;


@Component
public class UtilityHelper {
	
	
	@Value("${security.jwt.issuer}")
	private String jwtIssuer;
	

	@Value("${security.jwt.secret-key}")
	private String secretKey;
	
	public String createJwToken(AppUser user) {
		
		Instant now = Instant.now();
		
		JwtClaimsSet claims = JwtClaimsSet.builder().issuer(jwtIssuer)
				.issuedAt(now).expiresAt(now.plusSeconds(24*3600))
				.subject(user.getUserName())
				.claim("role", user.getRole()).build();
		
		NimbusJwtEncoder encoder = new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes()));
		
		JwtEncoderParameters params = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
		
		return encoder.encode(params).getTokenValue();
		
	}

}

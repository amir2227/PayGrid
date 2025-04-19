package com.paygrid.wallet.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paygrid.wallet.exception.UnAuthorizedException;
import org.springframework.data.web.JsonPath;

import java.util.Base64;
import java.util.UUID;

public class Utils {

    public static UUID extractUserIdFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new UnAuthorizedException("Invalid token");
        }
        String[] parts = token.substring(7).split("\\.");
        if (parts.length < 2) throw new UnAuthorizedException("Malformed token");
        String payload = new String(Base64.getDecoder().decode(parts[1]));
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(payload);
            return UUID.fromString(node.get("sub").asText());
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnAuthorizedException("Could not extract user ID from token");
        }
    }

}

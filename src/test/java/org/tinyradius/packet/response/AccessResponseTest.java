package org.tinyradius.packet.response;

import org.junit.jupiter.api.Test;
import org.tinyradius.attribute.type.RadiusAttribute;
import org.tinyradius.dictionary.DefaultDictionary;
import org.tinyradius.dictionary.Dictionary;
import org.tinyradius.util.RadiusPacketException;

import java.security.SecureRandom;
import java.util.Collections;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

class AccessResponseTest {

    private static final byte USER_PASSWORD = 2;

    private static final SecureRandom random = new SecureRandom();
    private static final Dictionary dictionary = DefaultDictionary.INSTANCE;

    @Test
    void encodeDecode() throws RadiusPacketException {
        final String sharedSecret = "sharedSecret1";
        final String username = "myUsername";
        final String password = "myPassword";

        final byte[] requestAuth = random.generateSeed(16);

        final RadiusResponse response = new AccessResponse.Accept(dictionary, (byte) 1, null, Collections.emptyList())
                .addAttribute(dictionary.createAttribute("User-Name", username))
                .addAttribute(dictionary.createAttribute(-1, USER_PASSWORD, password.getBytes(UTF_8)));

        final RadiusPacketException e = assertThrows(RadiusPacketException.class, () -> response.decodeResponse(sharedSecret, requestAuth));
        assertTrue(e.getMessage().contains("authenticator missing"));

        final RadiusResponse encodeResponse = response.encodeResponse(sharedSecret, requestAuth);
        final RadiusResponse decodeResponse = encodeResponse.decodeResponse(sharedSecret, requestAuth);

        final RadiusAttribute usernameAttribute = decodeResponse.getAttribute("User-Name").get();
        assertEquals(username, usernameAttribute.getValueString());

        final RadiusAttribute passwordAttribute = decodeResponse.getAttribute("User-Password").get();
        assertEquals(password, new String(passwordAttribute.getValue(), UTF_8));
    }
}
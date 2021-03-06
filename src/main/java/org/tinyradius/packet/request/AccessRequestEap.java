package org.tinyradius.packet.request;

import org.tinyradius.attribute.type.RadiusAttribute;
import org.tinyradius.dictionary.Dictionary;
import org.tinyradius.util.RadiusPacketException;

import java.util.List;

public class AccessRequestEap extends AccessRequest<AccessRequestEap> {

    public AccessRequestEap(Dictionary dictionary, byte identifier, byte[] authenticator, List<RadiusAttribute> attributes) {
        super(dictionary, identifier, authenticator, attributes);
    }

    @Override
    protected AccessRequestFactory<AccessRequestEap> factory() {
        return AccessRequestEap::new;
    }

    @Override
    public RadiusRequest encodeRequest(String sharedSecret) throws RadiusPacketException {
        return super.encodeRequest(sharedSecret);
    }

    @Override
    public RadiusRequest decodeRequest(String sharedSecret) throws RadiusPacketException {
        final List<RadiusAttribute> eapMessageAttr = filterAttributes(EAP_MESSAGE);
        if (eapMessageAttr.isEmpty())
            throw new RadiusPacketException("EAP-Message expected but not found");

        final List<RadiusAttribute> messageAuthAttr = filterAttributes(MESSAGE_AUTHENTICATOR);
        if (messageAuthAttr.size() != 1)
            throw new RadiusPacketException("AccessRequest (EAP) should have exactly one Message-Authenticator attribute, has " + messageAuthAttr.size());

        return super.decodeRequest(sharedSecret);
    }
}

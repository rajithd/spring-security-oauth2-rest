package org.ateam.common.service;

import org.ateam.common.repository.OauthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

@Component
public class ClientService implements ClientDetailsService {

    @Autowired
    private OauthRepository oauthRepository;

    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        BaseClientDetails clientDetails = oauthRepository.getByClientId(s);
        return clientDetails;
    }
}

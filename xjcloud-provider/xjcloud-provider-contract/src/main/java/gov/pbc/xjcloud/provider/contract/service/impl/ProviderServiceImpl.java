package gov.pbc.xjcloud.provider.contract.service.impl;

import gov.pbc.xjcloud.provider.contract.api.ProviderService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderServiceImpl implements ProviderService {
    @Override
    public String hello() {
        return "Hello " + System.currentTimeMillis();
    }
}

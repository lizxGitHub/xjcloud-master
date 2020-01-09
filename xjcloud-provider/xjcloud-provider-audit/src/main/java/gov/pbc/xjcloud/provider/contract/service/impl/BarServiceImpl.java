package gov.pbc.xjcloud.provider.contract.service.impl;

import gov.pbc.xjcloud.provider.contract.api.BarService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BarServiceImpl implements BarService {
    @Override
    public String bar() {
        return "Bar " + System.currentTimeMillis();
    }
}


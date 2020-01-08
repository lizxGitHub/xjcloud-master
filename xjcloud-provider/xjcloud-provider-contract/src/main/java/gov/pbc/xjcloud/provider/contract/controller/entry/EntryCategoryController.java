package gov.pbc.xjcloud.provider.contract.controller.entry;

import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/entry/category")
public class EntryCategoryController {

    @Autowired
    private EntryCategoryService entryCategoryService;



}

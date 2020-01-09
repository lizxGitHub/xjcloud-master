package gov.pbc.xjcloud.provider.contract.controller;

import gov.pbc.xjcloud.provider.contract.json.AjaxJson;
import gov.pbc.xjcloud.provider.contract.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author com.mhout.lizx
 * @version 1.0.0
 */
@RestController
@RequestMapping(value = "/test",headers="Accept=application/json", produces="application/json;charset=UTF-8")
public class TestController {

    @Resource
    TestService testService;

    @GetMapping("/getOne")
    public Map<String, Object> getTeacherOne (@RequestParam(name = "id", required = true)String id) {
        AjaxJson json = new AjaxJson();
        String[] ids = {"1021"};
        Map one = testService.selectById(id);
        return one;
    }

    @GetMapping("/getMany")
    public AjaxJson getTeacherMany (@RequestParam(name = "ids", required = false)String ids) {
        AjaxJson json = new AjaxJson();
        String[] tnoArray = ids.split(",");
        List<Map<String, Object>> TestList = testService.selectByIds(tnoArray);
        json.put(TestList);
        return json;
    }

}

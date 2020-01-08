package gov.pbc.xjcloud.provider.contract.controller.entry;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryInfo;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 词条管理
 *
 * @author paungmiao@163.com
 * @date 2020年1月6日21:53:24
 */
@RestController
@RequestMapping("/entry")
public class EntryController {

    @Autowired
    private EntryServiceImpl entryService;

    /**
     * 词条管理首页列表页面
     * @return
     */
    @GetMapping(value = {"index", ""})
    public IPage<List<EntryInfo>> index(EntryInfo query, IPage ipage) {
        QueryWrapper<EntryInfo> wrapper = new QueryWrapper();
//        wrapper.like("name", query.getName());
        IPage<List<EntryInfo>> page = entryService.page(ipage, wrapper);
        return page;
    }
}

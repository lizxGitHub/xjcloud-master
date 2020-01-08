package gov.pbc.xjcloud.provider.contract.controller.entry;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryInfo;
import gov.pbc.xjcloud.provider.contract.service.entry.EntryService;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 词条管理
 *
 * @author paungmiao@163.com
 * @date 2020年1月6日21:53:24
 */
@RestController
@RequestMapping("/entry")
public class EntryController {

    private EntryServiceImpl entryServieImpl;

    /**
     * 词条管理首页列表页面
     */
    @GetMapping(value = {"index", ""})
    public IPage<EntryInfo> index(EntryInfo query, IPage ipage) {
        QueryWrapper<EntryInfo> wrapper = new QueryWrapper();
        wrapper.like("name", query.getName());
        IPage page = entryServieImpl.page(ipage, wrapper);
        return page;
    }
}

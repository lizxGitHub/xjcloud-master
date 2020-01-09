package gov.pbc.xjcloud.provider.contract.controller.entry;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import gov.pbc.xjcloud.provider.contract.vo.entry.EntryInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 审计系统-词条管理
 *
 * @author paungmiao@163.com
 * @date 2020年1月6日21:53:24
 */
@RestController
@RequestMapping("/audit/entry")
public class EntryController {

    @Autowired
    private EntryServiceImpl entryService;

    /**
     * 词条管理首页列表页面
     *
     * @return
     */
    @GetMapping(value = {"index", ""})
    public R<Page<EntryInfoVO>> index(EntryInfoVO query, Page<EntryInfoVO> page) {
        PageUtil.initPage(page);
        try {
            page = entryService.selectEntryInfo(page, query);
        }
        catch (Exception e){
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
        return R.ok(page);
    }
}

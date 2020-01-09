package gov.pbc.xjcloud.provider.contract.controller.entry;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryCategory;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryCategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @date 2020年1月9日10:18:45
 * @author paungmiao@163.com
 */
@RestController
@RequestMapping("/audit/entry/category")
public class EntryCategoryController {

    @Autowired
    private EntryCategoryServiceImpl entryCategoryService;

    /**
     * 获取词条分类信息
     * @return
     */
    @RequestMapping("/list")
    public R<List<EntryCategory>> listR() {
        List<EntryCategory> list = entryCategoryService.list();
        return R.ok(list);
    }
}

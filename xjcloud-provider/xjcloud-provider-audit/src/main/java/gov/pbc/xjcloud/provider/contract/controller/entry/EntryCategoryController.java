package gov.pbc.xjcloud.provider.contract.controller.entry;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryCategory;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryCategoryServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @date 2020年1月9日10:18:45
 * @author paungmiao@163.com
 */
@RestController
@RequestMapping("/audit-api/entry/category")
@Api("词条分类")
public class EntryCategoryController {

    @Autowired
    private EntryCategoryServiceImpl entryCategoryService;

    /**
     * 获取词条分类信息
     * @return
     */
    @ApiOperation("获取词条类别")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public R<List<EntryCategory>> listR() {
        List<EntryCategory> list = entryCategoryService.list();
        return R.ok(list);
    }
}

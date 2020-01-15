package gov.pbc.xjcloud.provider.contract.controller.entry;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryCategory;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryInfo;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryCategoryServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import gov.pbc.xjcloud.provider.contract.vo.entry.EntryInfoVO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 审计系统-词条管理
 *
 * @author paungmiao@163.com
 * @date 2020年1月6日21:53:24
 */
@RestController
@RequestMapping("/audit-api/entry")
public class EntryController {

    @Autowired
    private EntryServiceImpl entryService;

    @Autowired
    private EntryCategoryServiceImpl categoryService;

    /**
     * 词条管理首页列表页面
     *
     * @return
     */
    @ApiOperation("词条页面信息")
    @GetMapping(value = {"page", ""})
    public R<Page<EntryInfoVO>> index(EntryInfoVO query, Page<EntryInfoVO> page) {
        PageUtil.initPage(page);
        try {
            page = entryService.selectEntryInfo(page, query);
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
        return R.ok(page);
    }

    /**
     * 根据id获取词条信息
     *
     * @param id
     * @return
     */
    @ApiOperation("获取词条信息")
    @GetMapping("/{id}")
    public R<EntryInfo> getEntryInfo(@PathVariable String id) {
        R<EntryInfo> r = new R<>();
        try {
            if (StringUtils.isBlank(id)) {
                return r.failed("参数错误，请检查");
            }
            EntryInfo byId = entryService.getById(id);
            r.setData(byId);
        } catch (Exception e) {
            r.failed(e.getMessage());
            e.printStackTrace();
        }
        return r;
    }

    /***
     *根据词条分类获取词条列表
     */
    @ApiOperation("根据分类获取词条列表")
    @GetMapping("/list_by_category/{cateKey}")
    public R<List<EntryInfo>> findEntryInfoByCateKey(@PathVariable String cateKey) {
        R<List<EntryInfo>> r = new R<>();
        QueryWrapper<EntryCategory> cateQuery = new QueryWrapper<>();
        QueryWrapper<EntryInfo> entryInfoQueryWrapper = new QueryWrapper<>();
        cateQuery.eq("def_key", cateKey);
        try {
            EntryCategory category = categoryService.getOne(cateQuery);
            entryInfoQueryWrapper.eq("category_fk", category.getId());
            List<EntryInfo> list = entryService.list(entryInfoQueryWrapper);
            r.setData(list);
        } catch (Exception e) {
            r.setMsg(e.getMessage());
            r.setCode(ApiErrorCode.FAILED.getCode());
            e.printStackTrace();
        }
        return r;
    }
}

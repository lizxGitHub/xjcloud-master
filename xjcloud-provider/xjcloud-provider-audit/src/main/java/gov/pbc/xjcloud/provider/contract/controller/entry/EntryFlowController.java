package gov.pbc.xjcloud.provider.contract.controller.entry;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryFlow;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryFlowServiceIml;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import gov.pbc.xjcloud.provider.contract.vo.entry.EntryInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 词条流程controller
 */
@Api("词条流程接口类")
@Controller
@RequestMapping("/audit-api/entry/flow")
public class EntryFlowController {

    @Resource
    private EntryFlowServiceIml entryFlowService;

    /**
     * 词条管理首页列表页面
     *
     * @return
     */
    @ApiOperation("词条页面信息")
    @GetMapping(value = {"page", ""})
    public R<Page<EntryFlow>> index(EntryFlow query, IPage<EntryFlow> page) {
        PageUtil.initPage(page);
        try {
            QueryWrapper<EntryFlow> queryWrapper = new QueryWrapper<>();
            if(null != query){
                if(StringUtils.isNotBlank(query.getName())){
                    queryWrapper.like("name",query.getName());
                }
                if(StringUtils.isNotBlank(query.getCategoryFk())){
                    queryWrapper.like("category_fk",query.getCategoryFk());
                }
            }
            page = entryFlowService.page(page, queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
        return R.ok((Page<EntryFlow>) page);
    }



}

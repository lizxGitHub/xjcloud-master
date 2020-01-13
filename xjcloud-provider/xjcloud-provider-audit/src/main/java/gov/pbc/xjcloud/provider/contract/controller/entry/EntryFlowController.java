package gov.pbc.xjcloud.provider.contract.controller.entry;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryCategory;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryFlow;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.AuditStatusEnum;
import gov.pbc.xjcloud.provider.contract.enumutils.OptEnum;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryCategoryServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryFlowServiceIml;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.IdGenUtil;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import gov.pbc.xjcloud.provider.contract.vo.entry.EntryFlowVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.Date;

/**
 * 词条流程controller
 */
@Api("词条流程接口类")
@RestController
@RequestMapping("/audit-api/entry/flow")
public class EntryFlowController {

    @Resource
    private EntryFlowServiceIml entryFlowService;

    @Resource
    private EntryServiceImpl entryService;

    @Resource
    private EntryCategoryServiceImpl categoryService;


    /**
     * 词条管理首页列表页面
     *
     * @return
     */
    @ApiOperation("词条页面信息")
    @GetMapping(value = {"page", ""})
    public R<IPage<EntryFlowVO>> index(EntryFlow query, IPage<EntryFlowVO> page) {
        PageUtil.initPage(page);
        try {
            QueryWrapper<EntryFlow> queryWrapper = new QueryWrapper<>();
            if (null != query) {
                if (StringUtils.isNotBlank(query.getName())) {
                    queryWrapper.like("name", query.getName());
                }
                if (StringUtils.isNotBlank(query.getCategoryFk())) {
                    queryWrapper.like("category_fk", query.getCategoryFk());
                }
                page = entryFlowService.selectEntryInfo(page, query);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
        return R.ok(page);
    }

    /**
     * 新建词条并提交审核
     *
     * @return
     */
    @ApiOperation("新建词条并提交审核")
    @PostMapping("/add")
    public R<Boolean> startAddFlow(EntryFlow entryFlow) {

        R<Boolean> r = new R<>();
        try {
            entryFlowService.validate(entryFlow, r);
            EntryCategory category = categoryService.getById(entryFlow.getCategoryFk());
            //乐观锁
            entryFlow.setRevision(1);
            //类型编码
            entryFlow.setTypeCode(category.getDefKey() + DateTime.now().toDate().getTime());
            // todo 此处需要能够访问用户服务 引入common-security包调用 SecurityUtils.getUsername() 方法;
            entryFlow.setCreatedBy("user01");
            entryFlow.setApplyUser("user01");
            entryFlow.setInstanceId(IdGenUtil.uuid());
            //新增
            entryFlow.setAuditStatus(AuditStatusEnum.ADD.getCode()+"");
            entryFlow.setUserOpt(OptEnum.ADD.getCode().toString());
            entryFlow.setCreatedTime(DateTime.now().toDate());
            entryFlow.setDelFlag(DelConstants.EXITED);
            r.setData(entryFlowService.save(entryFlow));
        } catch (Exception e) {
            e.printStackTrace();
            r.failed(e.getMessage());
        }
        return r;
    }

    /**
     * 创建此条信息
     *
     * @return
     */
    @ApiOperation("词条名称重复验证")
    @GetMapping("/checkName")
    public R<Boolean> checkName(EntryFlow entryFlow) {

        R<Boolean> r = new R<>();
        try {
            QueryWrapper<EntryFlow> wrapper = new QueryWrapper<>();
            QueryWrapper<EntryInfo> entryInfoQueryWrapper = new QueryWrapper<>();
            wrapper.like("name", entryFlow.getName());
            wrapper.eq("category_fk", entryFlow.getCategoryFk());

            entryInfoQueryWrapper.like("name", entryFlow.getName());
            entryInfoQueryWrapper.eq("category_fk", entryFlow.getCategoryFk());
            //若存在id表示是修改名称检验id重复
            if (StringUtils.isNotBlank(entryFlow.getId())) {
                wrapper.ne("id", entryFlow.getId());
                entryInfoQueryWrapper.ne("id", entryFlow.getEntryFk());
            }
            EntryFlow one = entryFlowService.getOne(wrapper);
            EntryInfo entryInfo = entryService.getOne(entryInfoQueryWrapper);
            r.setData(null != one || null != entryInfo);
        } catch (Exception e) {
            e.printStackTrace();
            r.failed(e.getMessage());
        }
        return r;
    }

    /**
     * 用户行为修改词条
     *
     * @param entryFlow
     * @return
     */
    @ApiOperation("用户删除词条")
    @PutMapping("/update")
    public R<Boolean> updateEntryFlow(EntryFlow entryFlow) {
        R<Boolean> r = new R<>();
        try {
            entryFlowService.validate(entryFlow, r);

            EntryFlow beforeFlow = entryFlowService.getById(entryFlow.getId());
            if(null == beforeFlow){
                r.setData(false);
                throw new NullPointerException("该数据已被删除");
            }
            if(entryFlow.getRevision()!=beforeFlow.getRevision()){
                r.setData(false);
                throw new NullPointerException("该数据已被更改，请刷新重试！");
            }
            UpdateWrapper<EntryFlow> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("name", entryFlow.getName());
            updateWrapper.set("revision", beforeFlow.getRevision()+1);
            updateWrapper.set("remarks", entryFlow.getRemarks());
            updateWrapper.set("category_fk", entryFlow.getCategoryFk());
            updateWrapper.set("updated_time", new Date());
            // todo 启动修改流程
            updateWrapper.set("updated_by", "user02");
            // 词条审核时使用该字段验证
            updateWrapper.set("instance_id", IdGenUtil.uuid());
            updateWrapper.eq("id", entryFlow.getId());
            entryFlowService.update(entryFlow, updateWrapper);
            r.setData(true);
            r.setCode(ApiErrorCode.SUCCESS.getCode());
        } catch (ConstraintViolationException e) {
            r.failed(e.getMessage());
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 根据id获取词条信息
     *
     * @param id
     * @return
     */
    @ApiOperation("获取词条流程信息")
    @GetMapping("/{id}")
    public R<EntryFlow> getEntryInfo(@PathVariable String id) {
        R<EntryFlow> r = new R<>();
        try {
            if (StringUtils.isBlank(id)) {
                return r.failed("参数错误，请检查");
            }
            EntryFlow byId = entryFlowService.getById(id);
            r.setData(byId);
        } catch (Exception e) {
            r.failed(e.getMessage());
            e.printStackTrace();
        }
        return r;
    }


}

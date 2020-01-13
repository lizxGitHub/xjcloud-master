package gov.pbc.xjcloud.provider.contract.controller.entry;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.constants.OptConstants;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.AuditStatusEnum;
import gov.pbc.xjcloud.provider.contract.enumutils.EntryOptEnum;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.IdGenUtil;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import gov.pbc.xjcloud.provider.contract.utils.TimeUtil;
import gov.pbc.xjcloud.provider.contract.vo.entry.EntryInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.Date;

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

    /**
     * 用户行为修改词条
     *
     * @param entryInfo
     * @return
     */
    @ApiOperation("用户删除词条")
    @PutMapping("/update")
    public R<Boolean> updateEntryInfo(EntryInfo entryInfo) {
        R<Boolean> r = new R<>();
        try {
            entryService.validate(entryInfo, r);
            UpdateWrapper<EntryInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("name", entryInfo.getName());
            updateWrapper.set("remarks", entryInfo.getRemarks());
            updateWrapper.set("category_fk", entryInfo.getCategoryFk());
            updateWrapper.set("updated_time", new Date());
            // todo 启动修改流程
            updateWrapper.set("updated_by", "admin");
            updateWrapper.set("instance_id", IdGenUtil.uuid());
            updateWrapper.eq("id", entryInfo.getId());
            entryService.update(entryInfo, updateWrapper);
            r.setData(true);
            r.setCode(ApiErrorCode.SUCCESS.getCode());
        } catch (ConstraintViolationException e) {
            r.failed(e.getMessage());
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 用户行为：删除词条
     *
     * @param id
     * @return
     */
    @ApiOperation("用户删除词条")
    @DeleteMapping("user_delete/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        R<Boolean> r = new R<>();
        Boolean b;
        if (StringUtils.isBlank(id)) {
            throw new NullArgumentException("请求参数不存在");
        } else {
            UpdateWrapper<EntryInfo> updateWrapper = new UpdateWrapper<>();
            EntryInfo entryInfo = new EntryInfo();
            entryInfo.setOptType(OptConstants.DEL);
            entryInfo.setInstanceId(IdGenUtil.uuid());
            entryInfo.setId(id);
            updateWrapper.set("opt_type", OptConstants.DEL);
            // todo 启动流程 获取流程实例ID
            updateWrapper.set("instance_id",entryInfo.getInstanceId());
            updateWrapper.eq("id",id);
            b = entryService.update(entryInfo,updateWrapper);
        }
        return r.setData(b);
    }


    /**
     * 创建此条信息
     *
     * @return
     */
    @PostMapping("/entries")
    public R<Boolean> entries(EntryInfo entryInfo) {

        R<Boolean> r = new R<>();
        try {
            entryService.validate(entryInfo, r);
            entryInfo.setTypeCode(Instant.now().toString());
            // todo 此处需要能够访问用户服务 引入common-security包调用 SecurityUtils.getUsername() 方法;
            entryInfo.setCreatedBy("admin");
            entryInfo.setAuditStatus(EntryOptEnum.ADD.getCode());
            entryInfo.setCreatedTime(DateTime.now().toDate());
            entryInfo.setAuditStatus(AuditStatusEnum.ADD.getCode());
            entryInfo.setDelFlag(DelConstants.EXITED);
            entryService.save(entryInfo);
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
    @GetMapping("/checkName")
    public R<Boolean> checkName(EntryInfo entryInfo) {

        R<Boolean> r = new R<>();
        try {
            QueryWrapper<EntryInfo> wrapper = new QueryWrapper<>();
            wrapper.like("name",entryInfo.getName());
            wrapper.like("category_fk",entryInfo.getCategoryFk());
            //若存在id表示是修改名称检验id重复
            if(StringUtils.isNotBlank(entryInfo.getId())){
                wrapper.ne("id",entryInfo.getId());
            }
            EntryInfo one = entryService.getOne(wrapper);
            r.setData(null!=one);
        } catch (Exception e) {
            e.printStackTrace();
            r.failed(e.getMessage());
        }
        return r;
    }
}

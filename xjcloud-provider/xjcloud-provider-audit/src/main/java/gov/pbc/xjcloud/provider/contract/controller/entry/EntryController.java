package gov.pbc.xjcloud.provider.contract.controller.entry;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.AuditStatusEnum;
import gov.pbc.xjcloud.provider.contract.enumutils.EntryOptEnum;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.IdGenUtil;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import gov.pbc.xjcloud.provider.contract.utils.TimeUtil;
import gov.pbc.xjcloud.provider.contract.vo.entry.EntryInfoVO;
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
     * 修改词条
     *
     * @param entryInfo
     * @return
     */
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
            updateWrapper.set("update_by", "admin");
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
     * 删除词条
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        R<Boolean> r = new R<>();
        Boolean b;
        if (StringUtils.isBlank(id)) {
            throw new NullArgumentException("请求参数不存在");
        } else {
            b = entryService.removeById(id);
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
}

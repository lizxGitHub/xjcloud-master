package gov.pbc.xjcloud.provider.contract.controller.entry;

import com.baomidou.mybatisplus.extension.api.R;
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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        }
        catch (Exception e){
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
        return R.ok(page);
    }

    /**
     * 创建此条信息
     * @return
     */
    @PostMapping("/entries")
    public R<Boolean> entries(EntryInfo entryInfo){

        R<Boolean> r = new  R<>();
        try {
            entryService.validate(entryInfo,r);
            entryInfo.setTypeCode(Instant.now().toString());
            // todo 此处需要能够访问用户服务 引入common-security包调用 SecurityUtils.getUsername() 方法;
            entryInfo.setCreatedBy("admin");
            entryInfo.setAuditStatus(EntryOptEnum.ADD.getCode());
            entryInfo.setCreatedTime(new Date());
            entryInfo.setAuditStatus(AuditStatusEnum.ADD.getCode());
            entryInfo.setDelFlag(DelConstants.EXITED);
            entryService.save(entryInfo);
        }catch (Exception e){
            e.printStackTrace();
            r.failed(e.getMessage());
        }
        return r;
    }
}

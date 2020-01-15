package gov.pbc.xjcloud.provider.contract.service.impl.entry;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryFlow;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.AuditStatusEnum;
import gov.pbc.xjcloud.provider.contract.enumutils.OptEnum;
import gov.pbc.xjcloud.provider.contract.mapper.entry.EntryFlowMapper;
import gov.pbc.xjcloud.provider.contract.mapper.entry.EntryMapper;
import gov.pbc.xjcloud.provider.contract.service.entry.EntryFlowService;
import gov.pbc.xjcloud.provider.contract.service.impl.IBaseServiceImpl;
import gov.pbc.xjcloud.provider.contract.vo.entry.EntryFlowVO;
import gov.pbc.xjcloud.provider.contract.vo.entry.EntryInfoVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class})
public class EntryFlowServiceIml extends IBaseServiceImpl<EntryFlowMapper, EntryFlow> implements EntryFlowService {

    @Resource
    EntryFlowMapper entryFlowMapper;

    @Resource
    EntryMapper entryMapper;

    /**
     * 词条流程信息获取
     *
     * @param page
     * @param query
     * @return
     */
    public IPage<EntryFlowVO> selectEntryInfo(IPage<EntryFlowVO> page, EntryFlow query) {
        List<EntryFlowVO> list = entryFlowMapper.selectEntryInfo(page, query);
        page.setRecords(list);
        return page;
    }

    /**
     * 流程通过
     *
     * @param instanceId
     */
    public void passEntryFlow(String instanceId) {

        //获取此条信息
        QueryWrapper<EntryFlow> flowQueryWrapper = new QueryWrapper<>();
        flowQueryWrapper.eq("instance_id", instanceId);
        EntryFlow entryFlow = entryFlowMapper.selectOne(flowQueryWrapper);
        EntryInfo entryInfo = entryFlow.getEntryInfo();
        String optCodeStr = entryFlow.getUserOpt();
        Integer optCode = null==optCodeStr?0:Integer.valueOf(optCodeStr);
        OptEnum optEnum = OptEnum.getOptByCode(optCode);
        switch (optEnum){
            case ADD:
                entryMapper.insert(entryInfo);
                break;
            case DEL:
                entryMapper.deleteById(entryInfo.getId());
                break;
            case UPDATE:
                entryMapper.updateBySql(entryInfo);
                break;
        }

        // 更新词条审核流程信息
        UpdateWrapper<EntryFlow> flowUpdateWrapper = new UpdateWrapper<>();
        flowUpdateWrapper.set("audit_status", AuditStatusEnum.PASS.getCode());
        flowUpdateWrapper.set("instance_id","");
        flowUpdateWrapper.eq("instance_id",instanceId);
        entryFlowMapper.update(entryFlow,flowUpdateWrapper);
    }

    /**
     * 接口拒绝
     * @param instanceId
     */
    public void rejectEntryFlow(String instanceId) {

        //获取此条信息
        QueryWrapper<EntryFlow> flowQueryWrapper = new QueryWrapper<>();
        flowQueryWrapper.eq("instance_id", instanceId);
        EntryFlow entryFlow = entryFlowMapper.selectOne(flowQueryWrapper);
        // 更新词条审核流程信息
        UpdateWrapper<EntryFlow> flowUpdateWrapper = new UpdateWrapper<>();
        flowUpdateWrapper.set("audit_status", AuditStatusEnum.REJECT.getCode());
        flowUpdateWrapper.set("instance_id","");
        flowUpdateWrapper.eq("instance_id",instanceId);
        entryFlowMapper.update(entryFlow,flowUpdateWrapper);
    }
}
package gov.pbc.xjcloud.provider.contract.utils;

import cn.hutool.json.JSONUtil;
import gov.pbc.xjcloud.provider.contract.feign.dept.RemoteDeptService;
import gov.pbc.xjcloud.provider.contract.vo.DeptVO;
import gov.pbc.xjcloud.provider.contract.vo.TreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class DeptUtil {

    @Autowired
    RemoteDeptService remoteDeptService;

    private Map<Integer, TreeVO> deptMap;

    private Lock lock = new ReentrantLock(false);

    public List<DeptVO> findChildBank(Integer deptId, String lastFilter)  {
        if (null == deptId) {
            return new ArrayList<>();
        }
        R r = remoteDeptService.children(deptId, false);
        ArrayList<Map<String, Object>> data = (ArrayList) r.getData();
        List<DeptVO> collect = data.stream().filter(Objects::nonNull).map(e -> {
            DeptVO deptVO = JSONUtil.toBean(JSONUtil.toJsonStr(e), DeptVO.class);
            return deptVO;
        }).filter(e -> e.getName().lastIndexOf(lastFilter) > -1).collect(Collectors.toList());
        return collect;
    }

    public Map<Integer, TreeVO> getDeptMap() {
        try {
            if(null== this.deptMap || this.deptMap.size()==0 ){
                this.deptMap = new HashMap<>();
                R<List<LinkedHashMap<String, Object>>> tree = remoteDeptService.tree();
                tree.getData().stream().filter(Objects::nonNull).forEach(e -> {
                    TreeVO treeVO = JSONUtil.toBean(JSONUtil.toJsonStr(e), TreeVO.class);
                    if (null != treeVO && !treeVO.getChildren().isEmpty()) {
                        treeVO.getChildren().forEach(c -> {
                            this.deptMap.put(c.getValue(), c);
                        });
                    }
                    TreeVO vo = TreeVONoChildren(treeVO);
                    this.deptMap.put(treeVO.getValue(), treeVO);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.deptMap;
    }

    private TreeVO TreeVONoChildren(TreeVO treeVO) {
        TreeVO vo = new TreeVO();
        vo.setValue(treeVO.getValue());
        vo.setLabel(treeVO.getLabel());
        return vo;
    }
    @Scheduled(fixedRate=1000*60)
    public void cleanDeptMap(){
        this.deptMap=new HashMap<>();
    }
}

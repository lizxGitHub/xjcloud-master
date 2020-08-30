//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package gov.pbc.xjcloud.provider.contract.feign.mission;

import gov.pbc.xjcloud.provider.contract.utils.R;
import gov.pbc.xjcloud.provider.contract.vo.MissionVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("xjcloud-provider-missioncenter")
public interface RemoteMissionService {
    @PutMapping({"/mission/mission/deploy"})
    R<MissionVO> deploy(@RequestBody MissionVO var1);
}

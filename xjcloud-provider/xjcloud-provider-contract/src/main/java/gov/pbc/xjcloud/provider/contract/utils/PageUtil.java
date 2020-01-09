package gov.pbc.xjcloud.provider.contract.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author paungmiao@163.com
 * @date 2020年1月9日11:53:02
 */
public class PageUtil {
    /**
     * 初始化分页对象
     *
     * @param iPage
     * @return
     */
    public static IPage initPage(IPage iPage) {
        if (null == iPage) {
            iPage = new Page<>(1, 10);
        }
        if (iPage.getCurrent() < 0l) {
            iPage.setCurrent(1l);
        }
        if (iPage.getSize() < 0l) {
            iPage.setSize(10l);
        }
        return iPage;
    }

}

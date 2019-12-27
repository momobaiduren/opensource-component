package com.opensource.component.dinging;

import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpensourceDingingApplicationTests {

    @Resource
    private DindingProvider dindingProvider;

    @Test
    void contextLoads() {
        dindingProvider.sendTextMessage("成本推送结算监控:我是张龙", null);
        dindingProvider.sendMarkdownMessage("监控警告","成本推送结算监控:zhanglong", null);
    }

}

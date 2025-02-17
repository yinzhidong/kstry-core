package cn.kstry.framework.test.flow;

import cn.kstry.framework.core.bus.InScopeData;
import cn.kstry.framework.core.engine.StoryEngine;
import cn.kstry.framework.core.engine.facade.ReqBuilder;
import cn.kstry.framework.core.engine.facade.StoryRequest;
import cn.kstry.framework.core.engine.facade.TaskResponse;
import cn.kstry.framework.core.enums.ScopeTypeEnum;
import cn.kstry.framework.core.enums.TrackingTypeEnum;
import cn.kstry.framework.core.exception.ExceptionEnum;
import cn.kstry.framework.test.flow.bo.MethodInvokeBo;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FlowCaseTestContextConfiguration.class)
public class FlowCase01Test {

    @Autowired
    private StoryEngine storyEngine;

    /**
     * 【正常】测试：正常单节点
     */
    @Test
    public void testFlow001() {
        MethodInvokeBo methodInvokeBo = new MethodInvokeBo();
        methodInvokeBo.setA(0);
        StoryRequest<Void> fireRequest = ReqBuilder.returnType(Void.class)
                .request(methodInvokeBo).trackingType(TrackingTypeEnum.SERVICE).startId("story-def-test-flow-001").build();
        TaskResponse<Void> fire = storyEngine.fire(fireRequest);
        Assert.assertTrue(fire.isSuccess());
        Assert.assertEquals(1, methodInvokeBo.getA());
    }

    /**
     * 【正常】测试：多分支带条件测试
     */
    @Test
    public void testFlow02() {
        MethodInvokeBo methodInvokeBo = new MethodInvokeBo();
        methodInvokeBo.setA(0);
        StoryRequest<Void> fireRequest = ReqBuilder.returnType(Void.class).request(methodInvokeBo)
                .trackingType(TrackingTypeEnum.SERVICE_DETAIL).startId("story-def-test-flow-002").build();
        TaskResponse<Void> fire = storyEngine.fire(fireRequest);
        Assert.assertTrue(fire.isSuccess());
        Assert.assertEquals(2, methodInvokeBo.getA());
    }

    /**
     * 【正常】测试：多分支测试
     */
    @Test
    public void testFlow0201() {
        MethodInvokeBo methodInvokeBo = new MethodInvokeBo();
        methodInvokeBo.setA(0);
        InScopeData varScopeData = new InScopeData(ScopeTypeEnum.VARIABLE);
        StoryRequest<Void> fireRequest = ReqBuilder.returnType(Void.class).request(methodInvokeBo).varScopeData(varScopeData)
                .trackingType(TrackingTypeEnum.SERVICE).requestId(UUID.randomUUID().toString()).startId("story-def-test-flow-00201").build();
        TaskResponse<Void> fire = storyEngine.fire(fireRequest);
        Assert.assertTrue(fire.isSuccess());
        Assert.assertEquals(3, methodInvokeBo.getA());
    }

    /**
     * 【正常】测试：排他网关测试
     */
    @Test
    public void testFlow03() {
        MethodInvokeBo methodInvokeBo = new MethodInvokeBo();
        methodInvokeBo.setA(0);
        StoryRequest<Void> fireRequest = ReqBuilder.returnType(Void.class).request(methodInvokeBo).startId("story-def-test-flow-003").build();
        TaskResponse<Void> fire = storyEngine.fire(fireRequest);
        Assert.assertTrue(fire.isSuccess());
        Assert.assertEquals(2, methodInvokeBo.getA());
    }

    /**
     * 【异常】测试：排他网关，无匹配分支测试
     */
    @Test
    public void testFlow0301() {
        StoryRequest<Void> fireRequest = ReqBuilder.returnType(Void.class).startId("story-def-test-flow-00301").build();
        TaskResponse<Void> fire = storyEngine.fire(fireRequest);
        System.out.println(JSON.toJSONString(fire));
        Assert.assertNotNull(fire.getResultException());
        Assert.assertEquals(fire.getResultCode(), ExceptionEnum.STORY_FLOW_ERROR.getExceptionCode());
    }

    /**
     * 【正常】测试：网关+子流程测试
     */
    @Test
    public void testFlow04() {
        MethodInvokeBo methodInvokeBo = new MethodInvokeBo();
        methodInvokeBo.setA(0);
        StoryRequest<Void> fireRequest = ReqBuilder.returnType(Void.class)
                .request(methodInvokeBo).trackingType(TrackingTypeEnum.SERVICE_DETAIL).timeout(4000).startId("story-def-test-flow-004").build();
        TaskResponse<Void> fire = storyEngine.fire(fireRequest);
        Assert.assertTrue(fire.isSuccess());
        Assert.assertEquals(2, methodInvokeBo.getA());
    }

    /**
     * 【异常】测试：测试 allow-absent 属性
     * allow-absent 属性测试
     * allow-absent=true 时，TaskService 未匹配到会直接跳过，默认为：true
     */
    @Test
    public void testFlow05() {
        StoryRequest<Void> fireRequest = ReqBuilder.returnType(Void.class).startId("story-def-test-flow-005").build();
        TaskResponse<Void> fire = storyEngine.fire(fireRequest);
        System.out.println(JSON.toJSONString(fire));
        Assert.assertNotNull(fire.getResultException());
        Assert.assertEquals(fire.getResultCode(), ExceptionEnum.TASK_SERVICE_MATCH_ERROR.getExceptionCode());
    }

    /**
     * 【正常】测试：并行网关测试
     */
    @Test
    public void testFlow06() {
        MethodInvokeBo methodInvokeBo = new MethodInvokeBo();
        methodInvokeBo.setA(0);
        StoryRequest<Void> fireRequest = ReqBuilder.returnType(Void.class).request(methodInvokeBo).startId("story-def-test-flow-006").build();
        TaskResponse<Void> fire = storyEngine.fire(fireRequest);
        Assert.assertTrue(fire.isSuccess());
        Assert.assertEquals(3, methodInvokeBo.getA());
    }

    /**
     * 【异常】测试：并行网关入度有无法触及分支时，出现异常测试
     */
    @Test
    public void testFlow0601() {
        StoryRequest<Void> fireRequest = ReqBuilder.returnType(Void.class).startId("story-def-test-flow-00601").build();
        TaskResponse<Void> fire = storyEngine.fire(fireRequest);
        System.out.println(JSON.toJSONString(fire));
        Assert.assertNotNull(fire.getResultException());
        Assert.assertEquals(fire.getResultCode(), ExceptionEnum.STORY_FLOW_ERROR.getExceptionCode());
    }

    /**
     * 【正常】测试：并行网关入度有无法触及分支时，设置 strict-mode=false 关闭并行网关的严格模式。流程继续执行测试
     */
    @Test
    public void testFlow0602() {
        MethodInvokeBo methodInvokeBo = new MethodInvokeBo();
        methodInvokeBo.setA(0);
        StoryRequest<Void> fireRequest = ReqBuilder.returnType(Void.class).request(methodInvokeBo).startId("story-def-test-flow-00602").build();
        TaskResponse<Void> fire = storyEngine.fire(fireRequest);
        Assert.assertTrue(fire.isSuccess());
        Assert.assertEquals(2, methodInvokeBo.getA());
    }

    /**
     * 【异常】测试：JSR303参数校验失败
     */
    @Test
    public void testFlow07() {
        StoryRequest<Void> fireRequest = ReqBuilder.returnType(Void.class).startId("story-def-test-flow-007").build();
        TaskResponse<Void> fire = storyEngine.fire(fireRequest);
        System.out.println(JSON.toJSONString(fire));
        Assert.assertFalse(fire.isSuccess());
        Assert.assertEquals(fire.getResultCode(), ExceptionEnum.PARAM_VERIFICATION_ERROR.getExceptionCode());
    }

    /**
     * 【异常】：测试出现流程被迫中断抛出异常
     */
    @Test
    public void testFlow08() {
        StoryRequest<Void> fireRequest = ReqBuilder.returnType(Void.class).startId("story-def-test-flow-008").build();
        TaskResponse<Void> fire = storyEngine.fire(fireRequest);
        System.out.println(JSON.toJSONString(fire));
        Assert.assertEquals(ExceptionEnum.STORY_FLOW_ERROR.getExceptionCode(), fire.getResultCode());
    }

    /**
     * 【正常】：false条件后面的包含网关异常情况测试
     */
    @Test
    public void testFlow09() {
        MethodInvokeBo methodInvokeBo = new MethodInvokeBo();
        methodInvokeBo.setA(0);
        StoryRequest<Void> fireRequest = ReqBuilder.returnType(Void.class).request(methodInvokeBo).startId("story-def-test-flow-009").build();
        TaskResponse<Void> fire = storyEngine.fire(fireRequest);
        Assert.assertTrue(fire.isSuccess());
        Assert.assertEquals(2, methodInvokeBo.getA());
    }

    /**
     * 【正常】：测试在排他网关上设置服务节点
     */
    @Test
    public void testFlow10() {
        MethodInvokeBo methodInvokeBo = new MethodInvokeBo();
        methodInvokeBo.setA(0);
        StoryRequest<Void> fireRequest = ReqBuilder.returnType(Void.class)
                .trackingType(TrackingTypeEnum.SERVICE_DETAIL).request(methodInvokeBo).startId("story-def-test-flow-010").build();
        TaskResponse<Void> fire = storyEngine.fire(fireRequest);
        Assert.assertTrue(fire.isSuccess());
        Assert.assertEquals(3, methodInvokeBo.getA());
    }

    /**
     * 【正常】：测试在包含网关上设置服务节点
     */
    @Test
    public void testFlow1001() {
        MethodInvokeBo methodInvokeBo = new MethodInvokeBo();
        methodInvokeBo.setA(0);
        StoryRequest<Void> fireRequest = ReqBuilder.returnType(Void.class)
                .trackingType(TrackingTypeEnum.SERVICE_DETAIL).request(methodInvokeBo).startId("story-def-test-flow-01001").build();
        TaskResponse<Void> fire = storyEngine.fire(fireRequest);
        Assert.assertTrue(fire.isSuccess());
        Assert.assertEquals(3, methodInvokeBo.getA());
    }

    /**
     * 【正常】：测试服务节点执行影响后面的条件判断
     */
    @Test
    public void testFlow11() {
        MethodInvokeBo methodInvokeBo = new MethodInvokeBo();
        methodInvokeBo.setA(0);
        StoryRequest<MethodInvokeBo> fireRequest = ReqBuilder.returnType(MethodInvokeBo.class)
                .trackingType(TrackingTypeEnum.SERVICE_DETAIL).request(methodInvokeBo).startId("story-def-test-flow-011").build();
        TaskResponse<MethodInvokeBo> fire = storyEngine.fire(fireRequest);
        Assert.assertTrue(fire.isSuccess());
        Assert.assertEquals(2, fire.getResult().getA());
    }
}

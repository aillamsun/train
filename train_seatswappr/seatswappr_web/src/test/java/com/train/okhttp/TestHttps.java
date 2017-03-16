package com.train.okhttp;

import com.mzlion.easyokhttp.HttpClient;
import org.junit.Test;
import org.junit.runners.model.TestClass;

/**
 * 测试HTTPS
 *
 * @author mzlion on 2016/12/10.
 */
public class TestHttps {

    /**
     * 请求受信任的HTTPS网站
     */
    @Test
    public void connectTrustHttps() {
        String baiduIndexContent = HttpClient.get("https://www.baidu.com")
                .execute().asString();
        System.out.println("baiduIndexContent = " + baiduIndexContent);
    }

    @Test
    public void connectUnTrustHttps1() {
        //直接信任所有网站,不推荐
        String mzlionIndexContent = HttpClient
                .get("https://kyfw.12306.cn/otn/")
                .https()
                .execute()
                .asString();
        //不受信任的原因：是因为12306自签名的SSL
        System.out.println("mzlionIndexContent = " + mzlionIndexContent);
    }

    @Test
    public void connectUnTrustHttps2() {
        //通过证书认证
        String mzlionIndexContent = HttpClient
                .get("https://kyfw.12306.cn/otn/")
                //导入自签名证书
                .https(TestClass.class.getClassLoader().getResourceAsStream("SRCA.cer"))
                .execute()
                .asString();
        System.out.println("mzlionIndexContent = " + mzlionIndexContent);
    }

    @Test
    public void connectUnTrustHttps3() {
        //通过证书认证
        String mzlionIndexContent = HttpClient
                .get("https://kyfw.12306.cn/otn/")
                //导入自签名证书,支持多种已知证书格式
                .https(TestClass.class.getClassLoader()
                        .getResourceAsStream("kyfw.12306.cn.crt"))
                .execute()
                .asString();
        System.out.println("mzlionIndexContent = " + mzlionIndexContent);
    }

}

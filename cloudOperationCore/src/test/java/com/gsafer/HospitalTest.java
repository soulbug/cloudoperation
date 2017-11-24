package com.gsafer;

import com.gsafer.smartoperation.util.OpenVpnFile;
import org.junit.Test;

import java.io.IOException;

/**
 * date 2017/11/3
 * author ethan
 * Copyright @ 互联极简网络科技成都有限公司
 * 类的作用
 */
public class HospitalTest {
    @Test
    public void testGenerateVpn(){
        try {
            OpenVpnFile.executeOpenVpnFile("aaaaaaaaaaaaaaaaaa","abaacaaaaaaaaa");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

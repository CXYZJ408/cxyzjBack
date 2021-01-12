package com.cxyzj.cxyzjback.Service.impl.Other;

import com.cxyzj.cxyzjback.Data.Other.TestData;
import com.cxyzj.cxyzjback.Service.Interface.Other.Test;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Utils.Response2;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import sun.nio.cs.ext.MacArabic;

import java.util.HashMap;

/**
 * @Author Yaser
 * @Date 4/25/2019 11:21 AM
 * @Description:
 */
@Service
public class TestImpl implements Test {
    @Override
    public String login(String userName, String password) {
        //逻辑代码
        Response2 response2 = new Response2();
        HashMap<String, String> map = new HashMap<>();
        map.put("AI", "123");
        map.put("IT", "123");
        map.put("C++", "123");
        if (userName.equals("admin") && password.equals("admin")) {
            response2.Insert("data", map);
            return response2.sendSuccess();
        } else {
            return response2.sendFailure();
        }

    }
}

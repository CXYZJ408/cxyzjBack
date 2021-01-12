package com.cxyzj.cxyzjback.Data.Other;

import lombok.Data;

/**
 * @Author Yaser
 * @Date 4/25/2019 11:56 AM
 * @Description:
 */
public class TestData {
    public TestData(String job, int age) {
        Job = job;
        this.age = age;
    }

    public String getJob() {
        return Job;
    }

    public void setJob(String job) {
        Job = job;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    String Job;
    int age;
}

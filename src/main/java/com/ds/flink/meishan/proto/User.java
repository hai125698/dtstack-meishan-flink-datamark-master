package com.ds.flink.meishan.proto;

import java.io.Serializable;

/**
 * @ClassName:
 * @Description:
 * @author: ds-longju
 * @Date: 2022-11-02 11:00
 * @Version 1.0
 **/
public class User implements Serializable {
    public User() {
    }

    public User(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    // 编号
    private Long id;
    // 姓名
    private String name;
    // 年龄
    private int age;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

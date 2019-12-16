package com.sherk.order.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    @Test
    void createOrder() {
//        orderDao.createOrder("1000","200000");
    }

}
package com.checkcode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.checkcode.dao.ICustomerDao;
import com.checkcode.entity.mpModel.CustomerModel;
import com.checkcode.service.ICustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl extends ServiceImpl<ICustomerDao, CustomerModel> implements ICustomerService {
}

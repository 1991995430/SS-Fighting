package com.ss.song.service.impl;

import com.ss.song.fegin.IPracticeClient;
import com.ss.song.mapper.UserMapper;
import com.ss.song.model.User;
import com.ss.song.service.SameService;
import com.ss.song.service.SsService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
public class SsServiceImpl implements SsService {

    private final IPracticeClient iPracticeClient;

    private final UserMapper userMapper;

    private final SameService sameService;

    private final DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    public SsServiceImpl(IPracticeClient iPracticeClient, UserMapper userMapper, SameService sameService, DataSourceTransactionManager dataSourceTransactionManager) {
        this.iPracticeClient = iPracticeClient;
        this.userMapper = userMapper;
        this.sameService = sameService;
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDiffService(Integer id) {

        // 手动开始事务
//        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
//        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        TransactionStatus transaction = dataSourceTransactionManager.getTransaction(defaultTransactionDefinition);

        User user = new User();
        user.setId(1);
        user.setName("服务1修改11111111");
        user.setAddress("北京市北京北京333333");
        userMapper.updateByPrimaryKey(user);
        iPracticeClient.updateByPrimaryKey(id);
        throw new RuntimeException("服务1失败");
//        dataSourceTransactionManager.commit(transaction);
//        try {
//            iPracticeClient.updateByPrimaryKey(id);
//        } catch (Exception ex) {
//            dataSourceTransactionManager.rollback(transaction);
//            throw new RuntimeException("服务2失败");
//        }
    }

    @Override
    public void updateSameService(Integer id) {

        User user = new User();
        user.setId(1);
        user.setName("第一次修改的名字88888");
        user.setAddress("北京市北京北京333333");
        userMapper.updateByPrimaryKey(user);

        sameService.updateByPrimKey(id);

        System.out.println("-----------------------");
    }
}

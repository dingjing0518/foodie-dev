package com.imooc.service.impl;

import com.imooc.mapper.StuMapper;
import com.imooc.pojo.Stu;
import com.imooc.service.StuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StuServiceImpl implements StuService {
    @Autowired
    private StuMapper stuMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Stu getStuInfo(int id) {
        return stuMapper.selectByPrimaryKey(id);
    }

    /**
     * 事务传播-Propagation
     *         REQUIRED：使用当前事务，如果当前没有事务，则自己新建一个事务，子方法是必须运行在一个事务中的；
     *                   如果当前存在事务，则加入这个事务，成为一个整体
     *         SUPPORTS：如果当前有事务，则使用事务；如果当前没有事务，则不使用事务
     *
     *         MANDATORY:该传播属性属于强制必须存在一个事务，如果不存在，则抛出一个异常
     *
     *         REQUIRES_NEW:如果当前有事务，则挂起该事务，并且自己创建一个新的事务给自己使用；
     *                      如果当前没有事务，则同REQUIRED
     *
     *         NOT_SUPPORTED：如果当前有事务，则把事务挂起，自己不使用事务去运行数据库操作
     *         NEVER：如果当前有事务存在，则抛出异常
     *         NESTED：如果当前有事务，则开启子事务（嵌套事务），嵌套事务是独立提交或者回滚；
     *                 如果当前没有事务，则同REQUIRED
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveStu() {
        Stu stu = new Stu();
        stu.setName("jack");
        stu.setAge(19);
        stuMapper.insert(stu);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateStu(int id) {
        Stu stu = new Stu();
        stu.setId(id);
        stu.setName("lucy");
        stu.setAge(20);
        stuMapper.updateByPrimaryKey(stu);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteStu(int id) {
        stuMapper.deleteByPrimaryKey(id);
    }
}

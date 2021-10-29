package com.weblog.dao;

import com.weblog.domain.OSPV;
import com.weblog.mapper.OSPVMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OSPVDao implements OSPVMapper{

    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return 0;
    }

    @Override
    public int insert(OSPV record) {
        return 0;
    }

    @Override
    public int insertSelective(OSPV record) {
        return 0;
    }

    @Override
    public OSPV selectByPrimaryKey(Integer id) {
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(OSPV record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(OSPV record) {
        return 0;
    }

    @Override
    public List<OSPV> getListByTime() {
        String sql="com.weblog.mapper.OSPVMapper.getListByTime";
        return this.sqlSessionFactory.openSession().selectList(sql);
    }


}

package com.weblog.dao;

import com.weblog.domain.RegionPV;
import com.weblog.mapper.RegionPVMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RegionDao implements RegionPVMapper{
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return 0;
    }

    @Override
    public int insert(RegionPV record) {
        return 0;
    }

    @Override
    public int insertSelective(RegionPV record) {
        return 0;
    }

    @Override
    public RegionPV selectByPrimaryKey(Integer id) {
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(RegionPV record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(RegionPV record) {
        return 0;
    }

    @Override
    public List<RegionPV> getList() {
        String sql="com.weblog.mapper.RegionPVMapper.getList";
        return this.sqlSessionFactory.openSession().selectList(sql);
    }
}

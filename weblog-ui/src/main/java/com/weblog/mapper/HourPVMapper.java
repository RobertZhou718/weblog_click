package com.weblog.mapper;

import com.weblog.domain.HourPV;

import java.util.List;

public interface HourPVMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HourPV record);

    int insertSelective(HourPV record);

    HourPV selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HourPV record);

    int updateByPrimaryKey(HourPV record);

    List<HourPV> selectList();
}
package com.weblog.mapper;

import com.weblog.domain.DayPV;

public interface DayPVMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DayPV record);

    int insertSelective(DayPV record);

    DayPV selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DayPV record);

    int updateByPrimaryKey(DayPV record);
}
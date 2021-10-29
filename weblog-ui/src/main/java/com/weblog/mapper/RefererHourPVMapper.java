package com.weblog.mapper;

import com.weblog.domain.RefererHourPV;

public interface RefererHourPVMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RefererHourPV record);

    int insertSelective(RefererHourPV record);

    RefererHourPV selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RefererHourPV record);

    int updateByPrimaryKey(RefererHourPV record);
}
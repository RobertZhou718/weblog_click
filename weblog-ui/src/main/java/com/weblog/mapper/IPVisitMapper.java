package com.weblog.mapper;

import com.weblog.domain.IPVisit;

public interface IPVisitMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IPVisit record);

    int insertSelective(IPVisit record);

    IPVisit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IPVisit record);

    int updateByPrimaryKey(IPVisit record);
}
package com.weblog.mapper;

import java.util.List;

import com.weblog.domain.RefererTop;

public interface RefererTopMapper {
	public List<RefererTop> selectTop10();
}

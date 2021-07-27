package com.market.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CalculateDAOImpl implements CalculateDAO {

	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public int getCountAll(int mem_num) {
		return this.sqlSession.selectOne("cal_count_all", mem_num);
	}

	@Override
	public List<CalculateDTO> getListAll(List<ClassDTO> list) {
		List<CalculateDTO> newList = new ArrayList<CalculateDTO>();

		for (int i = 0; i < list.size(); i++) {
			int target = list.get(i).getClass_num();
			newList.add((CalculateDTO) this.sqlSession.selectOne("cal_cont", target));
		}
		return newList;
	}

	@Override
	public int requestCal(int target) {
		return this.sqlSession.update("req_cal", target);
	}

}
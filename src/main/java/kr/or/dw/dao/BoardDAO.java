package kr.or.dw.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import kr.or.dw.command.SearchCriteria;
import kr.or.dw.vo.BoardVO;

@Mapper
public interface BoardDAO {


	int selectSearchBoardListCount(SearchCriteria cri) throws SQLException;

	List<BoardVO> selectSearchBoardList(SearchCriteria cri, RowBounds rowBounds) throws SQLException;
	
}

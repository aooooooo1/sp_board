package kr.or.dw.controller;

import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.or.dw.command.SearchCriteria;
import kr.or.dw.service.BoardService;
import kr.or.dw.vo.MemberVO;

@Controller
@RequestMapping("/board")
public class BoardController {
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired
	private BoardService boardService;
	
	@RequestMapping("/main")
	public String main() {
		String url = "/board/main.open";
		
		
		return url;
	};
	
	@RequestMapping("/list")
	public ModelAndView list(ModelAndView mnv, SearchCriteria cri) throws Exception {
		String url = "board/list.open";
		Map<String, Object> dataMap = boardService.selectBoardList(cri);
		
		mnv.addAllObjects(dataMap);
		mnv.setViewName(url);
		return mnv;
	}
	
	@RequestMapping("/registForm")
	public String detail(String bno, ModelAndView mnv) throws SQLException{
		String url = "/board/regist.open";
		
		return url;
		
	}
}

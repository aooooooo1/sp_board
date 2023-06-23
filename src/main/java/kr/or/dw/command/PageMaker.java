package kr.or.dw.command;

import lombok.Data;

@Data
public class PageMaker {
	private int totalCount; //전체 행의 갯수
	private int startPage = 1; //시작페이지번호
	private int endPage = 1; //마지막페이지 번호
	private int realEndPage; //끝 페이지 번호
	private boolean prev; //이전페이지버튼유무
	private boolean next; //이전페이지버튼유무
	
	private int displayPageNum = 10; //한페이지에 보여줄 페이지번호 개수
	private SearchCriteria cri; //현제 페이지 정보
	
	//startPage, endPage, prev , next 설정 (by totalCount)
	private void calcData() {
		endPage = (int)(Math.ceil(cri.getPage() / (double)displayPageNum) * displayPageNum); 
		startPage = (endPage - displayPageNum) + 1;
		realEndPage = (int)(Math.ceil(totalCount / (double)cri.getPerPageNum()));
		if(startPage < 0) {
			startPage = 1;
		}
		if(endPage > realEndPage ) {
			endPage = realEndPage;
		}
		
		prev = cri.getPage() == 1 ? false : true;
		next = cri.getPage() == realEndPage ? false : true;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		calcData();
	}
}

package kr.or.dw.vo;

import java.util.Date;

import lombok.Data;

@Data //룸북데이터를 붙히면 게터세터를 알아서 생성함
public class BoardVO {
	
	private int bno;
	private String title;
	private String writer;
	private String content;
	private Date regdate;
	private int viewcnt;
	private Date updatedate;
	
	
	public BoardVO() {};


	public BoardVO(int bno, String title, String writer, String content, Date regdate, int viewcnt,
			Date updatedate) {
		super();
		this.bno = bno;
		this.title = title;
		this.writer = writer;
		this.content = content;
		this.regdate = regdate;
		this.viewcnt = viewcnt;
		this.updatedate = updatedate;
	};
	
}

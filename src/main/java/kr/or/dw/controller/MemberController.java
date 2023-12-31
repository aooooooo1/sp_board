package kr.or.dw.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import kr.or.dw.command.MemberModifyCommand;
import kr.or.dw.command.MemberRegistCommand;
import kr.or.dw.command.SearchCriteria;
import kr.or.dw.service.MemberService;
import kr.or.dw.vo.MemberVO;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberService memberService;
	
	@RequestMapping("/main")
	public String main() {
		String url = "/member/main.open";
		return url;
	}
	
	
	/*????????????????????????????????????????????????????????????*/
	@RequestMapping("/list")
	public ModelAndView list(ModelAndView mnv, SearchCriteria cri) throws SQLException{
		String url = "/member/list.open";
		Map<String, Object> dataMap = memberService.selectSearchMemberList(cri);
		
		mnv.addAllObjects(dataMap);
		mnv.setViewName(url);
		
		return mnv;
	}
	
	// 회원등록 양식
	@RequestMapping("/registForm")
	public String registForm() {
		String url = "member/regist.open";
		return url;
	}
	
	// 회원등록
	@RequestMapping("/regist")
	   public void regist(MemberRegistCommand memberReq, HttpServletRequest req, HttpServletResponse res) throws SQLException, IOException {
	      MemberVO member = memberReq.toMemberVO();
	      memberService.regist(member);
	      
	      res.setContentType("text/html; charset=utf-8");
	      PrintWriter out = res.getWriter();
	      out.println("<script>");
	      out.println("alert('회원등록이 정상적으로 되었습니다.');");
	      out.println("window.opener.location.href='" + req.getContextPath() + "/member/list.do';");
	      out.println("window.close();");
	      out.println("</script>");
	   }
	
	// 아이디 중복확인
	@RequestMapping("/idCheck")
	public ResponseEntity<String> idCheck(String id, HttpServletRequest req){
		
		ResponseEntity<String> entity = null;
		
		try {
			MemberVO member = memberService.selectMemberById(id);
			entity = new ResponseEntity<String>(member == null ? id : "", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return entity;
	}
	
	@RequestMapping("/detail")
	public ModelAndView detail(String id, ModelAndView mnv) throws SQLException{
		String url = "/member/detail.open";
		
		MemberVO member = memberService.selectMemberById(id);
		mnv.addObject("member", member);
		mnv.setViewName(url);
		
		return mnv;
		
	}
	
	@RequestMapping("/modifyForm")
	public ModelAndView modifyForm(String id, ModelAndView mnv) throws SQLException{
		String url = "/member/modify.open";
		MemberVO member = memberService.selectMemberById(id);
		mnv.addObject("member", member);
		mnv.setViewName(url);
		return mnv;
	}
	
//	@RequestMapping("/deleteForm")
//	public void delete (String id, HttpServletResponse res, HttpServletRequest req) throws Exception{
//		  memberService.selectMemberDelete(id);
//		  res.setContentType("text/html; charset=utf-8");
//	      PrintWriter out = res.getWriter();
//	      String output = "" + "<script>" + "alert('삭제되었습니다.');" 
//					+ "window.opener.parent.location.reload();" +"window.close();" + "</script>";
//	      out.println("window.opener.parent.location.href='"+req.getContextPath() + "/member/list.do';");
//			out.println(output);
//			out.close();
//	}
	
	@RequestMapping("/deleteForm")
	public ModelAndView remove (String id, HttpSession session, ModelAndView mnv) throws SQLException{
		 String url = "/member/removeSuccess";
		 MemberVO member = null;
		 //이미지 파일 삭제해야한다 .
		 member = memberService.selectMemberById(id);
		 String savePath = this.picturePath;
		 File imageFile = new File(savePath, member.getPicture());
		 
		 if(imageFile.exists()) {
			 imageFile.delete();
		 }
		 memberService.selectMemberDelete(id);
		 
		//삭제되면 회원 로그아웃
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
		if(loginUser.getId().equals(member.getId())) {
			session.invalidate();
		};
		mnv.addObject("member", member);
		mnv.setViewName(url);
		return mnv;
	}
	
//	@RequestMapping("/stopForm")
//	public void stop (String id, HttpServletResponse res, HttpServletRequest req) throws Exception{
//		  memberService.selectMemberStop(id);
//		  res.setContentType("text/html; charset=utf-8");
//	      PrintWriter out = res.getWriter();
//	      String output = "" + "<script>" + "alert('그 사용자는 정지 되었습니다.');" 
//					+ "window.opener.parent.location.reload();" +"window.close();" + "</script>";
//	      out.println("window.opener.parent.location.href='"+req.getContextPath() + "/member/list.do';");
//			out.println(output);
//			out.close();
//	}
	
	@RequestMapping("/stop") //이 id 는 우리가 클릭한 유저의  id 이다.
	public ModelAndView stop (String id, HttpSession session, ModelAndView mnv) throws SQLException{
		String url = "/member/stopSuccess";
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
		if(id.equals(loginUser.getId())) {
			url = "/member/stopFail";
		}else {
			memberService.selectMemberStop(id);
		}
		mnv.addObject("id", id);
		mnv.addObject("msg","정지");
		mnv.setViewName(url);
		return mnv;
	}
	
	@RequestMapping("/rerole") //이 id 는 우리가 클릭한 유저의  id 이다.
	public ModelAndView rerole (String id, ModelAndView mnv) throws SQLException{
		String url = "/member/stopSuccess";
		
		memberService.selectMemberRerole(id);
		mnv.addObject("id", id);
		mnv.addObject("msg","해제");
		mnv.setViewName(url);
		return mnv;
		
	}
	
	@RequestMapping("/modify")
	public void modify(MemberModifyCommand modifyReq, HttpServletResponse res) throws Exception {
		MemberVO member = modifyReq.toParseMember();
		String fileName = savePicture(modifyReq.getPicture(), modifyReq.getOldPicture());
		member.setPicture(fileName);
		if(modifyReq.getPicture().isEmpty()) {
			member.setPicture(modifyReq.getOldPicture());
		};
		memberService.modify(member);
		 res.setContentType("text/html; charset=utf-8");
	      PrintWriter out = res.getWriter();
	      String output = "" + "<script>" + "alert('수정되었습니다.');" + "location.href='detail.do?id=" + member.getId() + "';"
					+ "window.opener.parent.location.reload();" + "</script>";
			out.println(output);
			out.close();
	}
	
	
	
	@RequestMapping("/picture")
	public ResponseEntity<String> picture(@RequestParam("pictureFile") MultipartFile multi, String oldPicture) throws Exception{
		ResponseEntity<String> entity = null;
		String result = "";
		HttpStatus status = null;
		if((result = savePicture(multi, oldPicture)) == null) {
			result = "업로드를 실패했습니다!!";
			status = HttpStatus.BAD_REQUEST;
		}else {
			status = HttpStatus.OK;
		}
		entity = new ResponseEntity<String>(result, status);
		return entity;
	}
	
	@Resource(name = "picturePath")
	private String picturePath;
	
	/*
	 	@Resource는 @AutoWired 와 같이 property injection 을 위한 어노테이션이다.
	 	차이점은
	 	@Autowired는 객체 타입을 보고 맵핑 된다면
	 	@Resource 는 객체 name(id)를 보고 맵핑이 된다.
	 	@Resource() 괄호에 객체 name을 명시하지 않으면 @Autowired 처럼 해당 레퍼런스변수의 타입과 똑같은 객체 name을 찾는다.
	 	
	 */
	
	private String savePicture(MultipartFile multi, String oldPicture) throws Exception {
		
		String fileName = null;
		
		/* 파일 유무 확인 */
		if(!(multi == null || multi.isEmpty() || multi.getSize() > 1024 * 1024 * 1)){
			/* 파일 저장 폴더 설정 */
			String uploadPath = picturePath;
			fileName = UUID.randomUUID().toString().replace("-", "") + ".jpg";
			File storeFile = new File(uploadPath, fileName);
			
			storeFile.mkdirs();
			
			// local HDD에 저장.
			multi.transferTo(storeFile);
			
			if(!oldPicture.isEmpty()) {
				File oldFile = new File(uploadPath, oldPicture);
				if(oldFile.exists()) {
					oldFile.delete();
				};
			};
		};
		
		return fileName;
	}
	
	@RequestMapping("/getPicture")
	public ResponseEntity<byte[]> getPicture(String picture) throws Exception {
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;
		String imgPath = this.picturePath;
		
		try {
			in = new FileInputStream(new File(imgPath, picture));
			
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), HttpStatus.CREATED);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			in.close();
		}
		
		return entity;
	}
	
	
	
	
	
}

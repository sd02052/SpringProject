package com.market.controller;

import java.io.File;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.JsonObject;
import com.market.model.CategoryDAO;
import com.market.model.CategoryDTO;
import com.market.model.ClassDAO;
import com.market.model.ClassDTO;
import com.market.model.MemberDAO;
import com.market.model.MemberDTO;
import com.market.model.NoticeDAO;
import com.market.model.NoticeDTO;
import com.market.model.OptionDAO;
import com.market.model.OptionDTO;
import com.market.model.Upload;

import net.sf.json.JSONArray;
import com.market.model.PageDTO;
import com.market.model.QnaDAO;
import com.market.model.QnaDTO;

import net.sf.json.JSONObject;

@Controller
public class MarketController {

	@Autowired
	private MemberDAO memberDao;
	@Autowired
	private NoticeDAO noticeDao;
	@Autowired
	private CategoryDAO categoryDao;
	@Autowired
	private Upload upload;
	@Autowired
	private ClassDAO classDao;
	@Autowired
	private OptionDAO optionDao;
	@Autowired
	private QnaDAO qnaDao;

	@RequestMapping("main.do")
	public String main() {
		return "home";
	}

	@RequestMapping("join.do")
	public String join() {
		return "joinForm";
	}

	@RequestMapping(value = "/emailCheck", method = RequestMethod.POST)
	@ResponseBody
	public void emailCheck(HttpServletResponse response, @RequestParam("mem_email") String mem_email) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		String res = null;
		int state = 0;
		
		int result = this.memberDao.checkEmail(mem_email);
		if(result > 0) {
			res = "사용 불가능합니다.";
			state = 2;
		} else {
			res = "사용 가능합니다.";
			state = 1;
		}
		
		JSONObject obj = new JSONObject();
		obj.put("res", res);
		obj.put("state", state);

		response.getWriter().print(obj);
	}
	
	@RequestMapping(value = "/nickCheck", method = RequestMethod.POST)
	@ResponseBody
	public void nickCheck(HttpServletResponse response, @RequestParam("mem_nick") String mem_nick) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		String res = null;
		int state = 0;
		
		int result = this.memberDao.checkNick(mem_nick);
		if(result > 0) {
			res = "사용 불가능합니다.";
			state = 2;
		} else {
			res = "사용 가능합니다.";
			state = 1;
		}
		
		JSONObject obj = new JSONObject();
		obj.put("res", res);
		obj.put("state", state);

		response.getWriter().print(obj);
	}
	
	@RequestMapping("join_ok.do")
	public void joinOk(MemberDTO dto, HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();

		int insertCheck = this.memberDao.insertMember(dto);
		if (insertCheck > 0) {
			out.println("<script>");
			out.println("alert('회원가입이 완료되었습니다.')");
			out.println("location.href='login.do'");
			out.println("</script>");
		} else {
			out.println("<script>");
			out.println("alert('회원가입 실패.')");
			out.println("history.back()");
			out.println("</script>");
		}
	}

	@RequestMapping("pwd_search.do")
	public String pwdSearch() {
		return "pwdSearch";
	}

	@RequestMapping(value = "/pwd_search_ok.do", method = RequestMethod.POST)
	@ResponseBody
	public void pwdSearch(HttpServletResponse response, @RequestParam("mem_email") String mem_email,
			@RequestParam("mem_name") String mem_name) throws IOException {
		String pwd = "일치하는 계정이 없습니다.";
		int state = 0;
		int checkEmail = this.memberDao.checkEmail(mem_email);

		if (checkEmail > 0) { // 아이디가 존재할때
			MemberDTO dto = this.memberDao.getMember(mem_email);
			if (dto.getMem_name().equals(mem_name)) { // 이름이 일치할때
				pwd = dto.getMem_pwd();
				state = 1;
			}
		}
		JSONObject obj = new JSONObject();
		obj.put("find_pwd", pwd);
		obj.put("state", state);

		response.getWriter().print(obj);

	}

	@RequestMapping("login.do")
	public String login() {
		return "loginForm";
	}

	@RequestMapping("login_ok.do")
	public void loginOk(@RequestParam("mem_email") String mem_email, @RequestParam("mem_pwd") String mem_pwd,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();

		int result = this.memberDao.loginCheck(mem_email, mem_pwd);

		if (result == 1) { // 로그인 성공
			HttpSession session = request.getSession();

			MemberDTO dto = this.memberDao.getMember(mem_email);
			System.out.println("로그인한 계정 >>> " + dto.getMem_num());
			session.setAttribute("loginDto", dto);
			if (dto.getMem_num() == 9999) {
				session.setAttribute("loginType", "admin");
			} else {
				session.setAttribute("loginType", "member");
			}

			out.println("<script>");
			out.println("location.href='main.do'");
			out.println("</script>");
		} else if (result == 2) { // 비밀번호 틀림
			out.println("<script>");
			out.println("alert('비밀번호가 틀립니다. 다시 확인해 주세요.')");
			out.println("history.back()");
			out.println("</script>");
		} else if (result == -1) { // 존재하지 않는 아이디
			out.println("<script>");
			out.println("alert('존재하지 않는 아이디입니다.')");
			out.println("history.back()");
			out.println("</script>");
		}
	}

	@RequestMapping("logout.do")
	public String logOut(HttpServletRequest request) {
		HttpSession session = request.getSession();
		System.out.println("로그아웃한 계정 >>>" + session.getAttribute("loginDto"));
		session.invalidate();

		return "home";
	}

	@RequestMapping("qna_list.do")
	public String qnaList(HttpServletRequest request, Model model) {
		int totalRecord = 0;
		int rowsize = 5;
		int page = 0; // 현재 페이지 변수

		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		} else {
			page = 1; // 처음으로 "게시물 전체 목록" 태그를 클릭한 경우
		}

		// DB 상의 전체 게시물의 수를 확인하는 작업.
		totalRecord = this.qnaDao.getListCount(0);

		PageDTO dto = new PageDTO(page, rowsize, totalRecord);

		// 페이지에 해당하는 게시물을 가져오는 메서드 호출
		List<QnaDTO> pageList = this.qnaDao.getQnaList(dto);

		model.addAttribute("list", pageList);
		model.addAttribute("Paging", dto);

		return "qna_list";
	}

	@RequestMapping("notice_list.do")
	public String noticeList() {
		return "notice_list";
	}

	@RequestMapping("like_frip.do")
	public String likeFrip() {
		return "like_frip";
	}

	@RequestMapping("like_host.do")
	public String likeHost() {
		return "like_host";
	}

	@RequestMapping("host_info.do")
	public String hostInfo() {
		return "host_info";
	}

	@RequestMapping("category_list.do")
	public String cateList() {
		return "category_list";
	}

	@RequestMapping("option_select.do")
	public String optionSel() {
		return "option_select";
	}

	@RequestMapping("admin_frip_pass.do")
	public String fripPass() {
		return "admin_frip_pass";
	}

	@RequestMapping("admin_cal_pass.do")
	public String calPass() {
		return "admin_cal_pass";
	}

	@RequestMapping("payment.do")
	public String pay() {
		return "payment";
	}

	@RequestMapping("payment_ok.do")
	public String payOk() {
		return "payment_ok";
	}

	@RequestMapping("hostMain.do")
	public String hostMain(Model model) {

		List<NoticeDTO> NList = this.noticeDao.getNoticeList();

		model.addAttribute("NList", NList);

		return "host/hostMain";
	}

	@RequestMapping("hostMakeFrip.do")
	public String hostMakeFrip(Model model) {

		List<CategoryDTO> cateList = this.categoryDao.getCate_oneList();

		model.addAttribute("cateList", cateList);

		return "host/hostMakeFrip";
	}

	@RequestMapping(value = "/cate_two.do", method = RequestMethod.POST)
	@ResponseBody
	public void cate_two(HttpServletResponse response, @RequestParam("cate_one") String cate_one) throws IOException {

		List<CategoryDTO> cate_twoList = this.categoryDao.getCate_two(cate_one);

		JSONObject obj = new JSONObject();

		JSONArray ja = JSONArray.fromObject(cate_twoList);

		obj.put("clist", ja);

		response.getWriter().print(obj);

	}

	@RequestMapping(value = "/uploadSummernoteImageFile", produces = "application/json; charset=utf8")
	@ResponseBody
	public String uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile,
			HttpServletRequest request) {
		JsonObject jsonObject = new JsonObject();

		String fileRoot = "C:\\Users\\kmsol\\git\\SpringProject\\SpringProject\\src\\main\\webapp\\resources\\summernote\\FileUpload\\";

		String originalFileName = multipartFile.getOriginalFilename(); // 오리지날 파일명
		String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 파일 확장자
		String savedFileName = UUID.randomUUID() + extension; // 저장될 파일 명

		File targetFile = new File(fileRoot + savedFileName);
		try {
			InputStream fileStream = multipartFile.getInputStream();
			FileUtils.copyInputStreamToFile(fileStream, targetFile); // 파일 저장
			jsonObject.addProperty("url", "/controller/resources/summernote/FileUpload/" + savedFileName);
			jsonObject.addProperty("responseCode", "success");

		} catch (IOException e) {
			FileUtils.deleteQuietly(targetFile); // 저장된 파일 삭제
			jsonObject.addProperty("responseCode", "error");
			e.printStackTrace();
		}
		String a = jsonObject.toString();
		return a;
	}

	  @RequestMapping("insertFrip.do") 
	  public void insertFrip(ClassDTO dto, OptionDTO odto, MultipartHttpServletRequest mRequest,
			  HttpServletRequest request, HttpServletResponse response) throws Exception { // 파일 업로드 처리 
		 
		  dto.setClass_image(upload.fileUpload(mRequest));
		  System.out.println(dto);
		  
		  //주소입력 기본주소 + 상세주소
		  dto.setClass_endArea(request.getParameter("class_endArea") 
				  				+""+request.getParameter("endArea_detail"));
		  if(request.getParameter("startArea") == null) {
			  dto.setClass_startArea("null");
		  }
		  //전체 클래스의 수 + 1구하기
		  int count = this.classDao.countClass();
		  dto.setClass_num(count);
		  odto.setOption_classNum(count);
		  
		  int result = this.classDao.insertClass(dto); 
		 
		  //옵션
		  int result2 =0;
		  int Qtt = Integer.parseInt(request.getParameter("optionQtt"));
		  if(odto.getOption_endDate() == null) { //  끝나는 날이 없으면 공백값
			  odto.setOption_endDate("null");
		  }
		  
		  for(int i=1; i<=Qtt; i++) {
			  odto.setOption_name(request.getParameter("option_name"+i));
			  odto.setOption_price(Integer.parseInt(request.getParameter("option_price"+i))); 
	
			  result2 = this.optionDao.insertOption(odto); 
		
		  }
		 PrintWriter out = response.getWriter();
		 if(result == 1 && result2 == 1) {
			out.println("<script>");
			out.println("alert('등록 성공!')");
			out.println("location.href='hostMain.do'");
			out.println("</script>");
		 }else {
			out.println("<script>");
			out.println("alert('등록 실패!')");
			out.println("history.back()");
			out.println("</script>");
		 }
	 
	 
	 }
	 

	@RequestMapping("hostMyFrip.do")
	public String hostMyFrip() {
		return "host/hostMyFrip";
	}

	@RequestMapping("hostAttendance.do")
	public String hostAttendance() {
		return "host/hostAttendance";
	}

	@RequestMapping("hostAttendance_member.do")
	public String hostAttendance_member() {
		return "host/hostAttendance_member";
	}

	@RequestMapping("hostAsk.do")
	public String hostAsk() {
		return "host/hostAsk";
	}

	@RequestMapping("hostCalculateReq.do")
	public String hostCalReq() {
		return "host/hostCalculateReq";
	}

	@RequestMapping("hostNotice_list.do")
	public String hostNotice() {
		return "host/hostNotice_list";
	}

	@RequestMapping("hostQna_list.do")
	public String hostQna() {
		return "host/hostQna_list";
	}

	@RequestMapping("hostReview.do")
	public String hostReview() {
		return "host/hostReview";
	}

	@RequestMapping("mypage.do")
	public String myPage() {
		return "mypage";
	}

	@RequestMapping("mypage_edit.do")
	public String myPageEdit() {
		return "mypage_edit";
	}

	@RequestMapping("mypage_coupon.do")
	public String myPageCoupon() {
		return "mypage_coupon";
	}

	@RequestMapping("mypage_purchases.do")
	public String myPagePurchases() {
		return "mypage_purchases";
	}

	@RequestMapping("mypage_energy.do")
	public String myPageEnergy() {
		return "mypage_energy";
	}

	@RequestMapping("admin_notice.do")
	public String adminNotice() {
		return "admin_notice";
	}

	@RequestMapping("admin_member_list.do")
	public String adminMember() {
		return "admin_member_list";
	}

	@RequestMapping("hostSideBar.do")
	public String hostSideBar() {
		return "hostInclude/hostSideBar";
	}

	@RequestMapping("hostFooter.do")
	public String hostFooter() {
		return "hostInclude/hostFooter";
	}

	@RequestMapping("hostAsk_answer.do")
	public String hostAsk_answer() {
		return "host/hostAsk_answer";
	}

	@RequestMapping("hostUpdateFrip.do")
	public String hostUpdateFrip() {
		return "host/hostUpdateFrip";
	}

	@RequestMapping("frip_content.do")
	public String fripContent() {
		return "frip_content";
	}
}

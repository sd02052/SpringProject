<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>hostMyFrip</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"></script>
<link href="<%=request.getContextPath()%>/resources/css/hostMyFrip.css"
	rel="stylesheet" />
</head>
<body>
	<div class="mainFrame">
	<jsp:include page="../hostInclude/hostMenuBar.jsp"></jsp:include>
	
		<div class="Frame"> 
			<div class="Frame_Frame">
				<jsp:include page="../hostInclude/hostSideBar.jsp"></jsp:include>
	
				<div class="Main">
					<h1 class="Main_title">내 프립</h1>
					
					<div class="info_frame">
						<div class="state">
							<div class="btn-group" role="group" aria-label="Basic radio toggle button group">
							  <input type="radio" class="btn-check" name="btnradio" id="btnradio1" autocomplete="off" checked>
							  <label class="btn btn-outline-primary" for="btnradio1">판매중</label>
							
							  <input type="radio" class="btn-check" name="btnradio" id="btnradio2" autocomplete="off">
							  <label class="btn btn-outline-primary" for="btnradio2">대기중</label>
							
							  <input type="radio" class="btn-check" name="btnradio" id="btnradio3" autocomplete="off">
							  <label class="btn btn-outline-primary menuButton" for="btnradio3">종료</label>
							</div>
							
							<div class="class_group">
								<c:forEach begin="1" end="4" var="i">
									<div class="class_item">
										<div class="class_item_div">
											<div class="class_item_thumbnail">
												<div class="class_item_top">
													<span class="class_area">서울</span>
													<!-- 스크랩 누른상태 -->
													<i class="fas fa-bookmark fa-2x push" data-placement="left"></i>
													<!-- 스크랩 안누른 상태 -->
													<i class="far fa-bookmark fa-2x nopush" data-placement="left"></i>
												</div>
												<div class="class_item_image">
													<img class="class_img"
														src="https://res.cloudinary.com/frientrip/image/upload/ar_1:1,c_fill,dpr_2,f_auto,w_310/product_banner_1623911236019_872487">
												</div>
											</div>
											<div class="class_item_hash">추가수량 확보 | 나만의 감성 만들기</div>
											<div class="class_item_title"><p class="main-block-desc">[50%▼할인]공간을 그리다, 와이어아트 (예약 가능)</p></div>
											<div class="class_item_state">상태 <span class="state_text">판매 대기중</span></div>
											<div class="class_item_price">
												<span class="class_price_after">25,000원</span>
												<span class="class_price_before">50,000</span>
											</div>
											<div class="class_Button_div1">
												<a href="#">수정</a>
											</div>
										</div>
									</div>
								</c:forEach>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
	<jsp:include page="../hostInclude/hostFooter.jsp"></jsp:include>
	</div>
</body>
</html>
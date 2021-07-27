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
							  <input type="radio" class="btn-check" name="btnradio" id="btnradio1" autocomplete="off" onclick="location.href='hostMyFrip.do'">
							  <label class="btn btn-outline-primary" for="btnradio1">판매중</label>
							
							  <input type="radio" class="btn-check" name="btnradio" id="btnradio2" autocomplete="off" checked onclick="location.href='hostMyFripWait.do'">
							  <label class="btn btn-outline-primary" for="btnradio2">대기중</label>
							
							  <input type="radio" class="btn-check" name="btnradio" id="btnradio3" autocomplete="off" onclick="location.href='hostMyFripEnd.do'">
							  <label class="btn btn-outline-primary menuButton" for="btnradio3">종료</label>
							</div>
							
							<c:set var="list" value="${cList }"/>
								<c:forEach begin="0" end="1" varStatus="index">
									<div class="class_group">
										<c:if test="${!empty list }">
											<c:forEach var="dto" items="${list }" begin="${(index.count-1)*4 }" end="${(index.count-1)*4 +3}">
												<div class="class_item">
													<div class="class_item_div">
														<div class="class_item_thumbnail">
															<div class="class_item_top">
																<span class="class_area">${dto.getClass_endArea().substring(0,2) }</span>
								
															</div>
															<div class="class_item_image">
																<img class="class_img"
																	src="<%=request.getContextPath() %>/resources/upload/${dto.getClass_image()}" height="180px">
															</div>
														</div>
														<div class="class_item_hash">${dto.getClass_hash() }</div>
														<div class="class_item_title"><p class="main-block-desc">${dto.getClass_title() }</p></div>
															<div class="class_item_state">상태 <span class="state_text">승인 대기중</span></div>
														<div class="class_Button_div1">
														<!-- 프립 번호 같이 보내줘야함! -->
															<a href="<%=request.getContextPath()%>/hostUpdateFrip.do?class_num=${dto.getClass_num()}">수정</a>
														</div>
													</div>
												</div>
											</c:forEach>
										</c:if>
										
									</div>
								</c:forEach>
								<c:if test="${empty list }">
									대기중인 프립이 없습니다.
								</c:if>
						</div>
					</div>
					
					<!-- 페이징 처리 부분 -->
					<nav aria-label="Page navigation example" class="qna_list_footer">
						<ul class="pagination">
							<c:if test="${Paging.getPage() > Paging.getBlock() }">
								<li class="page-item">
									<a class="page_link" href="hostMyFripWait.do?page=1">
										<span aria-hidden="true">&laquo;</span>
									</a>
								</li>
							</c:if>
							<c:forEach begin="${Paging.getStartBlock() }" end="${Paging.getEndBlock() }" var="i">
								<c:if test="${i == Paging.getPage() }">
									<li class="page-item"><a class="page_link active">${i }</a></li>
								</c:if>
								<c:if test="${i != Paging.getPage() }">
									<li class="page-item"><a class="page_link" href="hostMyFripWait.do?page=${i }">${i }</a></li>
								</c:if>
							</c:forEach>
							<c:if test="${Paging.getEndBlock() < Paging.getAllPage() }">
								<li class="page-item">
									<a class="page-link" href="hostMyFripWait.do?page=${Paging.getAllPage() }" style="color: black;">
										<span aria-hidden="true">&raquo;</span>
									</a>
								</li>
							</c:if>
						</ul>
					</nav>
				</div>
			</div>
		</div>
		
	<jsp:include page="../hostInclude/hostFooter.jsp"></jsp:include>
	</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="cri" value="${pageMaker.cri}"/>

<div class="content-wrapper">
	<jsp:include page="../content_header.jsp">
		<jsp:param value="회원 목록" name="subject" />
		<jsp:param value="목록" name="item" />
	</jsp:include>
	<!-- Main content -->
	<section class="content">
		<div class="card">
			<div class="card-header with-border">
				<sec:authorize access="hasAnyRole('ROLE_ADMIN, ROLE_MANAGER')">
					<button type="button" class="btn btn-primary" onclick="OpenWindow('registForm.do', '회원등록', 650, 750);">회원등록</button>
				</sec:authorize>
				<div id="keyword" class="card-tools" style="width: 550px;">
					<div class="input-group row">
						<!-- sort num -->
						<select class="form-control col-md-3" name="perPageNum"
							id="perPageNum">
							<option value="10">정렬개수</option>
							<option value="2" ${ cri.perPageNum == 2 ? 'selected' : '' }>2개씩</option>
							<option value="3" ${ cri.perPageNum == 3 ? 'selected' : '' }>3개씩</option>
							<option value="5" ${ cri.perPageNum == 5 ? 'selected' : '' }>5개씩</option>
						</select>
						<!-- search bar -->
						<select class="form-control col-md-3" name="searchType"
							id="searchType">
							<option value="" ${ cri.searchType == '' ? 'selected' : '' }>검색구분</option>
							<option value="i" ${ cri.searchType == 'i' ? 'selected' : '' }>아이디</option>
							<option value="p" ${ cri.searchType == 'p' ? 'selected' : '' }>전화번호</option>
							<option value="e" ${ cri.searchType == 'e' ? 'selected' : '' }>이메일</option>
						</select> 
						<input class="form-control" type="text" name="keyword"
							placeholder="검색어를 입력하세요." value="${cri.keyword}"/> 
							
							<span class="input-group-append">
								<button class="btn btn-primary" type="button" id="searchBtn"
									data--card-widget="search" onclick="searchList_go(1);">
									<i class="fa fa-fw fa-search"></i>
								</button>
							</span>
						<!-- end : search bar -->

					</div>
				</div>
			</div>
			<div class="card-body" style="text-align: center;">
				<div class="row">
					<div class="col-sm-12">
						<table class="table table-bordered">
							<tr>
								<th>아이디</th>
								<th>패스워드</th>
								<th>사용여부</th>
								<th>이메일</th>
								<th>전화번호</th>
								<th>등록날짜</th>
								<!-- yyyy-MM-dd  -->
							</tr>
						<c:forEach items="${memberList }" var="member">
							<tr>
								<td><a href="javascript:OpenWindow('detail.do?id=${member.id }', '회원상세보기', 800, 700);">${member.id }</a></td>
								<td>${member.pwd }</td>
								<td>${member.enabled} </td>
								<td>${member.email }</td>
								<td><a href="tel:010-1234-5678">${member.phone }</a></td>
								<td>
									<fmt:formatDate value="${member.regdate }" pattern="yyyy-MM-dd"/>
								</td>
							</tr>
						</c:forEach>
						</table>
					</div>
					<!-- col-sm-12 -->
				</div>
				<!-- row -->
			</div>
			<!-- card-body -->
			<div class="card-footer">
				<%@ include file="/WEB-INF/views/common/pagination.jsp" %>
			
			</div>
			<!-- cardfooter  -->
		</div>
		<!-- card  -->
	</section>
</div>

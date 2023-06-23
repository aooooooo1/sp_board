<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script>
	function SmartEditer_summernote(target) {
		target.summernote({
			placeholder : '여기에 내용을 적으세요',
			height : 300,
			disableResizeEditor : true,
			callbacks : {
				onImageUpload : function (files, editor, welEditable) {
					for(let i = files.length -1; i > -1; i--){
						if(files[i].size > 1024 * 1024 * 5){
							alert("이미지는 5mb미만이어야 합니다.");
							return;
						};
					};
					//위에서 안걸리면 서버로 보넨다.
					for(let i = files.length -1; i >=0; i--){
						sendFile(files[i], this);
					}
				}
			}
		});
	};
	
	function sendFile(file, el) {
		let form_data = new FormData();
		form_data.append("file", file);
		$.ajax({
			url : '<%=request.getContextPath()%>/uploadImg.do',
			type : 'post',
			data : form_data,
			contentType : false,
			processData : false,
			success : function(img_url){
				alert(img_url);
			}
		});
	}
</script>
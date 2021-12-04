// Khi khởi động trang users_form
$(document).ready(function()
{
	// Ấn nút Hủy thì quay về trang Quản lý nhân viên
	$("#buttonCancel").on("click", function()
	{
		window.location = moduleURL;			
	});
	
	// Load file ảnh lên trang users_form / account_form
	$	("#fileImage").change(function()
	{
		fileSize = this.files[0].size;
		if(fileSize > 1048576)
		{
			this.setCustomValidity("Bạn phải chọn ảnh có dung lượng bé hơn 1mb!");
			this.reportValidity();
		} else
		{
			this.setCustomValidity("");
			showImageThumnail(this);	
		}
					
	});
});

// Hiện ảnh đại diện lên trang user_form / account_form
function showImageThumnail(fileInput)
{
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e)
	{
		$("#thumbnail").attr("src", e.target.result)
	};
	reader.readAsDataURL(file);
}
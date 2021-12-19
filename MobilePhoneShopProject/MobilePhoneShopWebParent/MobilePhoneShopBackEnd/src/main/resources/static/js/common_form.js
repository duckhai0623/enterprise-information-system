// Khi khởi động trang *_form
$(document).ready(function()
{
	// Ấn nút Hủy thì quay về trang có đường dẫn là moduleURL
	$("#buttonCancel").on("click", function()
	{
		window.location = moduleURL;			
	});
	
	// Load file ảnh lên trang *_form
	$("#fileImage").change(function()
	{
		if(!checkFileSize(this))
			return;
			
		showImageThumnail(this);
	});
		
});

// Hiện ảnh đại diện lên trang *_form
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

// Kiểm tra kích thước file ảnh
function checkFileSize(fileInput)
{
	fileSize = fileInput.files[0].size;
	if(fileSize > MAX_FILE_SIZE)
	{
		fileInput.setCustomValidity("Bạn phải chọn ảnh có dung lượng bé hơn" + MAX_FILE_SIZE + " bytes!");
		fileInput.reportValidity();
		return false;
	} else
	{
		fileInput.setCustomValidity("");
		return true;
	}	
}

// Hiện thông báo / message tương ứng
function showModalDialog(title, message)
{
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();			
}

function showErrorModal(message)
{
	showModalDialog("Lỗi", message);	
}

function showWarningModal(message)
{
	showModalDialog("Cảnh báo", message);	
}
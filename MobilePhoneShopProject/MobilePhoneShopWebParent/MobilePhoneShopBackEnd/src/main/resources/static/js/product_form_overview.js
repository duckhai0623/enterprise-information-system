dropdownBrands = $("#brand");
dropdownCategories = $("#category");

$(document).ready(function()
{
	// Tạo richText cho shortDescription và fullDescription
	$("#shortDescription").richText();
	$("#fullDescription").richText();
	
	// Hiện combobox Loại hàng sau khi chọn Hãng
	dropdownBrands.change(function()
	{
		dropdownCategories.empty();
		getCategories();				
	});
	
	getCategoriesForNewForm();
	
});

function getCategoriesForNewForm()
{
	categoryIdField = $("#categoryId");
	editMode = false;
	
	if(categoryIdField.length)
		editMode = true;
		
	if(!editMode)
		getCategories();
}

function getCategories()
{
	brandId = dropdownBrands.val(); 
	url = brandModuleURL + "/" + brandId + "/categories";
	$.get(url, function(responseJson)
	{
		$.each(responseJson,function(index, category)
		{
			$("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);	
		});				
	});
}

// Kiểm tra trùng sản phẩm
function checkUnique(form)
{
	productId = $("#id").val();
	productName = $("#name").val();
	csrfValue = $("input[name='_csrf']").val();
	params = {id: productId, name: productName, _csrf: csrfValue};
	$.post(checkUniqueURL, url, params, function(response)
	{
		if(response == "OK")
			form.submit();
		else if (response == "Duplicate")
			showWarningModal("Tên sản phẩm '"+ productName +"' đã tồn tại!")
			else
	 			showErrorModal("Không tìm thấy response của server");
	}).fail(function()
		{
			showErrorModal("Không thể kết nối tới server!")
		});
	return false;			
}
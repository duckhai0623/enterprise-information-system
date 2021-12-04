$(document).ready(function()
{
	// Ấn nút đăng xuất thì đăng xuất khỏi tài khoản
	$("#logoutLink").on("click", function(e)
	{
		e.preventDefault();
		document.logoutForm.submit();
	});
	
	customizeDropDownMenu();
});

function customizeDropDownMenu()
{
	$(".navbar .dropdown").hover(
		function()
		{
			$(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();		
		},
		function()
		{
			$(this).find('.dropdown-menu').first().stop(true, true).delay(100).slideUp();			
		});
	
	// Ấn vào tên đại diện thì chuyển đến trang thông tin cá nhân
	$(".dropdown > a").click(function()
	{
		location.href = this.href;
	});	
}
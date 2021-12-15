function clearFilter()
{
	window.location = moduleURL;
}

function showDeleteConfirmModal(link, entityName)
{
	entityId = link.attr("entityId");
	$("#yesButton").attr("href", link.attr("href"));
	$("#confirmText").text("Bạn có thực sự muốn xoá " + entityName + " có id = " + entityId + "?");
	$("#confirmModal").modal();
}
$().ready( function() {
	$.tip = function () {
		var $tipWindow = $("#tipWindow");
		var $icon = $("#tipWindow .icon");
		var $messageText = $("#tipWindow .messageText");
		var messageType;
		var messageText;
		if (arguments.length == 1) {
			messageType = "warn";
			messageText = arguments[0];
		} else {
			messageType = arguments[0];
			messageText = arguments[1];
		}
		if (messageType == "success") {
			$icon.removeClass("warn").removeClass("error").addClass("success");
		} else if (messageType == "error") {
			$icon.removeClass("warn").removeClass("success").addClass("error");
		} else {
			$icon.removeClass("success").removeClass("error").addClass("warn");
		}
		$messageText.html(messageText);
		$tipWindow.css({"margin-left": "-" + parseInt($tipWindow.width() / 2) + "px", "left": "50%"});
		setTimeout(function() {
			$tipWindow.animate({left: 0, opacity: "hide"}, "slow");
		}, 1000);
		$tipWindow.show();
	}
	// 表单验证
	$("form.validate").validate({
		errorClass: "validateError",
		ignore: ".ignoreValidate",
		errorPlacement: function(error, element) {
			var messagePosition = element.metadata().messagePosition;
			if("undefined" != typeof messagePosition && messagePosition != "") {
				var $messagePosition = $(messagePosition);
				if ($messagePosition.size() > 0) {
					error.insertAfter($messagePosition).fadeOut(300).fadeIn(300);
				} else {
					error.insertAfter(element).fadeOut(300).fadeIn(300);
				}
			} else {
				error.insertAfter(element).fadeOut(300).fadeIn(300);
			}
		},
		submitHandler: function(form) {
			form.submit();
		}
	});

});
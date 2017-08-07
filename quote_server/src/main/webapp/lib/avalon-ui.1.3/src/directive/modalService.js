/**
 * Created by Administrator on 2016/8/22.
 */

(function (angular) {
    'use strict';
    
    // 使用avalon.ui模块， controller，directive,service都注册到这里，注意避免命名重复
angular.module("avalon.ui").factory('modalService', ['$timeout', '$window',function($timeout,$window) {

	var modal = {};


	modal.dialog = function(options, callback,cancelCallback) {
		var btn = "";
		var footerBtn = options.footer ? options.footer : ["确定", "取消"];
		for(var i = 0, len = footerBtn.length; i < len; i++) {
			if(i == 0) {
				btn += '<button type="button" class="btn btn-default comfirm" data-dismiss="ss-modal">' + footerBtn[i] + '</button>'
			} else {
				btn += '<button type="button" class="btn btn-default cancel" data-dismiss="ss-modal">' + footerBtn[i] + '</button>'
			}
		}
		$(".ss-modal-content").css("width", options.size.width);
		$(".ss-modal-content").css("height", options.size.height);
		$(".ss-modal-title").text(options.head);
		$(".ss-modal-body-content").html(options.body);
		$(".ss-modal-footer").html(btn);
		$(".ss-modal").show();
		$('.cancel').off('click');
		$('.cancel').on('click', function() {
			modal.close();
			if(cancelCallback){
				cancelCallback();
			}
			return false;
		});

		$('.comfirm').off('click');
		$('.comfirm').on('click', function() {
			modal.close();
			if(callback)
				callback();
		});

	}
	modal.close = function() {
		$(".ss-modal").hide();
	}
	modal.info = function(message,callback) {
		var options = {
			head: "提示",
			body: '<i class="body-icon ss-icon ss-icon-dialog-caution"></i>' +
				'<p class="text">' + message + '</p>',
			size: {
				width: "400px",
				height: "217px"
			}
		}
		var btn = '<button type="button" class="btn btn-default comfirm" data-dismiss="ss-modal">确定</button>';
		$(".ss-modal-content").css("width", options.size.width);
		$(".ss-modal-content").css("height", options.size.height);
		$(".ss-modal-title").text(options.head);
		$(".ss-modal-body-content").html(options.body);
		$(".ss-modal-footer").html(btn);
		$(".ss-modal").show();
		$('.cancel').off('click');
		$('.cancel').on('click', function() {
			modal.close();
			
		});
		$('.comfirm').off('click');
		$('.comfirm').on('click', function() {
			modal.close();
			if(callback)
				callback();
		});
	}
	
		modal.err = function(message,callback) {
		var options = {
			head: "提示",
			body: '<i class="body-icon ss-icon ss-icon-dialog-error"></i>' +
				'<p class="text">' + message + '</p>',
			size: {
				width: "400px",
				height: "217px"
			}
		}
		var btn = '<button type="button" class="btn btn-default comfirm" data-dismiss="ss-modal">确定</button>';
		$(".ss-modal-content").css("width", options.size.width);
		$(".ss-modal-content").css("height", options.size.height);
		$(".ss-modal-title").text(options.head);
		$(".ss-modal-body-content").html(options.body);
		$(".ss-modal-footer").html(btn);
		$(".ss-modal").show();
		$('.cancel').off('click');
		$('.cancel').on('click', function() {
			modal.close();
			
		});
		$('.comfirm').off('click');
		$('.comfirm').on('click', function() {
			modal.close();
			if(callback)
				callback();
		});
	}
		
			modal.suc = function(message,callback) {
		var options = {
			head: "提示",
			body: '<i class="body-icon ss-icon ss-icon-dialog-success"></i>' +
				'<p class="text">' + message + '</p>',
			size: {
				width: "400px",
				height: "217px"
			}
		}
		var btn = '<button type="button" class="btn btn-default comfirm" data-dismiss="ss-modal">确定</button>';
		$(".ss-modal-content").css("width", options.size.width);
		$(".ss-modal-content").css("height", options.size.height);
		$(".ss-modal-title").text(options.head);
		$(".ss-modal-body-content").html(options.body);
		$(".ss-modal-footer").html(btn);
		$(".ss-modal").show();
		$('.cancel').off('click');
		$('.cancel').on('click', function() {
			modal.close();
		
		});
		$('.comfirm').off('click');
		$('.comfirm').on('click', function() {
			modal.close();
			if(callback)
				callback();
		});
	}

	modal.question = function(message, callback,cancelCallback) {
		var options = {
			head: "提示",
			body: '<i class="body-icon ss-icon ss-icon-dialog-confirm"></i>' +
				'<p class="text">' + message + '</p>',
			size: {
				width: "400px",
				height: "217px"
			}
		}
		var btn = '<button type="button" class=" btn btn-default comfirm" data-dismiss="ss-modal">确认</button>' +
			'<button type="button" class="btn btn-default cancel" data-dismiss="ss-modal">取消</button>';
		$(".ss-modal-content").css("width", options.size.width);
		$(".ss-modal-content").css("height", options.size.height);
		$(".ss-modal-title").text(options.head);
		$(".ss-modal-body-content").html(options.body);
		$(".ss-modal-footer").html(btn);
		$(".ss-modal").show();

		$('.cancel').off('click');
		$('.cancel').on('click', function() {
			modal.close();
			if(cancelCallback){
				cancelCallback();
			}
			return false;
		});

		$('.comfirm').off('click');
		$('.comfirm').on('click', function() {
			modal.close();
			if(callback)
				callback();
		});
	}

	modal.startDrag = function(bar, target, callback) {
		var params = {
			left: 0,
			top: 0,
			currentX: 0,
			currentY: 0,
			flag: false
		};
		var topdistance;
		var leftdistance;
		bar.onmousedown = function(event) {
			leftdistance = parseInt($(target).css("width")) / 2;
			topdistance = parseInt($(target).css("height")) / 2;
			params.left = target.offsetLeft;
			params.top = target.offsetTop;
			params.flag = true;
			if(!event) {
				event = window.event;

				bar.onselectstart = function() {
					return false;
				}
			}
			var e = event;
			params.currentX = e.clientX; //得到鼠标位置并赋值
			params.currentY = e.clientY;
		};
		document.onmouseup = function() {
			params.flag = false;
		};

		document.onmousemove = function(event) {
			var e = event ? event : window.event;
			e.preventDefault();
			if(params.flag) {
				var nowX = e.clientX,
					nowY = e.clientY;
				var disX = nowX - params.currentX, //获取当前鼠标和原鼠标位置的距离
					disY = nowY - params.currentY;
				var leftDistance = target.style.Width;
				var left = parseInt(params.left) + disX; //将原窗口距离加上鼠标移动距离
				var top = parseInt(params.top) + disY;
				target.style.left = left <= leftdistance ? (leftdistance + "px") : (left >= window.innerWidth - leftdistance ? window.innerWidth - leftdistance + "px" : left + "px");
				console.log(top);
				console.log(topdistance);
				target.style.top = top <= topdistance ? (topdistance + "px") : (top >= window.innerHeight - topdistance ? window.innerHeight - topdistance + "px" : top + "px");
			}

			if(typeof callback == "function") {
				callback(parseInt(params.left) + disX, parseInt(params.top) + disY);
			}
		}
	};

	return modal;

}])

})(window.angular);




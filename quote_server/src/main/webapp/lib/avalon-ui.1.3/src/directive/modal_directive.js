/**
 * Created by Administrator on 2016/8/22.
 */

(function (angular) {
    'use strict';
    
    // 使用avalon.ui模块， controller，directive都注册到这里，注意避免命名重复
    angular.module('avalon.ui').directive('ssModalDirective', ['$interval', '$parse', '$timeout', '$window', 'modalService', function ($interval, $parse, $timeout, $window, modalService) {

        return {
            restrict: 'C',
            replace: true,
            templateUrl: "template/modal.html",
            link: function(scope, ele, attr) {
            	$timeout(function() {
			
				var oBox = $(".ss-modal-dialog")[0];
			//调用此函数使其可以拖动，参数为你要拖动的对象
			modalService.startDrag(oBox, oBox);
			}, 0);}
        };

    }]);

})(window.angular);;
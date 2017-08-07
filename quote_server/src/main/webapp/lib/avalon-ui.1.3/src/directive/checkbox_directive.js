(function (angular) {
    'use strict';
    
    // 使用avalon.ui模块， controller，directive都注册到这里，注意避免命名重复
    angular.module('avalon.ui').directive("ssCheckboxDirective", [
        function () {
            
            var injectedCtrl = [
                '$scope', function ($scope) {
                     var updateModelArg = undefined;                    
                    
                    $scope.onChangeChecked = function () {
                        updateModelArg = {
                            oldValue: $scope.oldValue,
                            newValue: $scope.value
                        };
                        
                        $scope.updateModel({ $event: updateModelArg });
                        
                        $scope.oldValue = $scope.value;
                    };
                    
                    $scope.onClick = function (event) {
                        if (!$scope.isNoBindModel) {
                            $scope.value = !$scope.value;
                            
                            $scope.onChangeChecked();
                        }
                    };
                    
                    var initView = function () {
                        $scope.oldValue = $scope.value;
                    }();
                }
            ];
            
            return {
                restrict: 'C',
                replace: true,
                //template: '<div class="ss-input-control ss-checkbox" ng-click="onClick($event)">\
                //                <input type="checkbox" ng-model="value" ng-change="onChangeChecked()" ng-disabled="{{isDisabled}}">\
                //                <i class="check ss-icon ss-icon-primary ss-icon-checkbox-{{value?\'selected\':\'unselected\'}}" ng-disabled="{{isDisabled}}"></i>\
                //                <span class="caption" ng-bind="label"></span>\
                //        <div>',
                // templateUrl与config.json中的配置项export值需相同
                templateUrl: 'template/checkbox.html',
                controller: injectedCtrl,
                scope: {
                    value: "=ngModel",
                    label: "@ssLabel",
                    updateModel: "&ngChange",
                    type: "@type",
                    isDisabled: "=ngDisabled"
                },
                compile: function (el, attr) {
                    return {
                        pre: function preLink(scope, element, attributes) {
                            if (attributes.ngModel === undefined) scope.isNoBindModel = true;
                        },
                        post: function postLink(scope, element, attributes) {
                           
                        }
                    };
                }
            };

        }]);

})(window.angular);
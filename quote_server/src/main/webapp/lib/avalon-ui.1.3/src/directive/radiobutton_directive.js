(function (angular) {
    'use strict';
    
    // 使用avalon.ui模块， controller，directive都注册到这里，注意避免命名重复
    angular.module('avalon.ui').directive("ssRadiobuttonDirective", ['$parse', 'avalon.ui.utility',
        function ($parse, ssUtility) {
            
            var injectedCtrl = [
                '$scope', function ($scope) {
                    
                    var updateModelArg = undefined;
                    
                    $scope.onClick = function (event) {
                        if (!$scope.isNoBindModel) {
                            $scope.value = $scope.targetValue;
                            $scope.onChange();
                        }
                    };
                    
                    $scope.onChange = function () {
                        
                        updateModelArg = {
                            oldValue: $scope.oldValue,
                            newValue: $scope.value
                        };
                        
                        $scope.oldValue = $scope.value;
                        
                        if (updateModelArg.newValue === updateModelArg.oldValue) return;

                        if (updateModelArg.newValue !== $scope.targetValue) return;                        

                        $scope.updateModel({ $event: updateModelArg });
                    };
                    
                    var initView = function () {
                        $scope.oldValue = $scope.value;
                    }();
                }
            ];
            
            return {
                restrict: 'C',
                replace: true,
                // templateUrl与config.json中的配置项export值需相同
                templateUrl: 'template/radiobutton.html',
                controller: injectedCtrl,
                scope: {
                    value: "=ngModel",
                    targetValue: "@ssValue",
                    label: "@ssLabel",
                    updateModel: "&ngChange",
                    type: "@type",
                    isDisabled: "=ngDisabled",
                    labelPath: "@ssLabelPath",
                    ngRepeatInner: "@ngRepeat"
                },
                link: function (scope, element, attributes, ngModel) {
                    
                },
                compile: function (el, attr) {
                    return {
                        pre: function preLink(scope, element, attributes, ngModel) {
                            
                            var valueIdf = function () {
                                var expression = attributes.ngRepeat;
                                
                                if (!expression) return undefined;
                                
                                var match = expression.match(/^\s*([\s\S]+?)\s+in\s+([\s\S]+?)(?:\s+as\s+([\s\S]+?))?(?:\s+track\s+by\s+([\s\S]+?))?\s*$/);
                                
                                if (!match) return undefined;
                                
                                var lhs = match[1];
                                var rhs = match[2];
                                var aliasAs = match[3];
                                var trackByExp = match[4];
                                
                                match = lhs.match(/^(?:(\s*[\$\w]+)|\(\s*([\$\w]+)\s*,\s*([\$\w]+)\s*\))$/);
                                
                                if (!match) return undefined;
                                
                                var valueIdentifier = match[3] || match[1];
                                var keyIdentifier = match[2];
                                
                                if (aliasAs && (!/^[$a-zA-Z_][$a-zA-Z0-9_]*$/.test(aliasAs) || /^(null|undefined|this|\$index|\$first|\$middle|\$last|\$even|\$odd|\$parent|\$root|\$id)$/.test(aliasAs))) {
                                    return undefined;
                                }
                                
                                return valueIdentifier;
                            }();            

                            if (valueIdf) scope.targetValue = scope.$parent[valueIdf];
                            
                            //scope.$watch(function () {
                            //    return scope.$parent.$parent[attributes.ngModel];
                            //}, function (newValue, oldValue) {
                            //    if (newValue !== oldValue) {
                            //        // scope.value = newValue;
                            //    }
                            //});
                            
                            scope.$watch(function () {
                                return scope.value;
                            }, function (newValue, oldValue) {
                                if (newValue !== oldValue) {  
            
                                    // scope.$parent.$parent[attributes.ngModel] = newValue;
                                    
                                    scope.onChange();
                                }
                            });
                            
                            if (attributes.ngModel === undefined) scope.isNoBindModel = true;
                        },
                        post: function postLink(scope, element, attributes, ngModel) {
                            
                        }
                    };
                }
            };

        }]);

})(window.angular);
(function() {

    angular.module('avalon.ui').directive('ssFilterDirective', function() {
        return {
            restrict: "E",
            replace: false,
            scope: {
                filterConfig: '=',
                filterCondition: "="
            },
            // html的模板可维护性更好，而且这样也不用担心会产生get html文件所产生的性能问题，template的内容在打包的时候会混进js里面。
            templateUrl: "template/filter_template.html",
            link: function($scope, $element, $attrs) {
                // console.log('Filter init!');

                function init() {
                    var filterConfig = {
                        "identifier": $scope.filterConfig.identifier || '',
                        "multiSelect": $scope.filterConfig.multiSelect,
                        "enableAllBtn": $scope.filterConfig.enableAllBtn,
                        "saveStatus": $scope.filterConfig.saveStatus,
                        "storageFunction": $scope.filterConfig.storageFunction || sessionStorage,
                        "conditions": $scope.filterConfig.conditions || []
                    }
                    if (filterConfig.enableAllBtn) {
                        var btnAllData = [];
                        var btnAll = [{
                            "title": '全部',
                            "data": 'all',
                            "active": true
                        }];
                    } else {
                        var btnAll = [];
                    }
                    $scope.filterConfig = filterConfig;
                    $scope.conditions = btnAll.concat(filterConfig.conditions);
                    $scope.onMultiAct = 0;
                    $scope.filterConfig.saveStatus ? loadStatus() : null;
                }

                function loadStatus() {
                    var tempStr = $scope.filterConfig.storageFunction.getItem($scope.filterConfig.identifier);
                    var tempArr = JSON.parse(tempStr);
                    if (tempStr != '["all"]') {
                        tempArr.length > 1 ? $scope.onMultiAct = 1 : $scope.onMultiAct = 0;
                        tempArr.forEach(function(tempArrE, tempArrIndex) {
                            $scope.conditions.forEach(function(condE, condIndex) {
                                if (JSON.stringify(tempArrE) == JSON.stringify(condE.data)) {
                                    $scope.filterBtnClick({"ctrlKey":$scope.onMultiAct},condIndex);
                                }
                            });
                        });
                    }
                }

                $scope.filterBtnClick = function($event, $index) {
                    var ifMulti = $event.ctrlKey && $scope.filterConfig.multiSelect;
                    if (ifMulti) {
                        if ($scope.conditions[$index].active) {
                            $scope.conditions[$index].active = false;
                        } else {
                            $scope.conditions[$index].active = true;
                        }
                        $scope.onMultiAct = 1;
                    } else {
                        if ($scope.onMultiAct && $scope.conditions[$index].active) {
                            // console.log('x01');
                            for (var i = 0; i < $scope.conditions.length; i++) {
                                $scope.conditions[i].active = false;
                            }
                            $scope.conditions[$index].active = true;
                        } else if ($scope.conditions[$index].active) {
                            // console.log('x02');
                            for (var i = 0; i < $scope.conditions.length; i++) {
                                $scope.conditions[i].active = false;
                            }
                            $scope.conditions[$index].active = false;
                        } else {
                            // console.log('x03');
                            for (var i = 0; i < $scope.conditions.length; i++) {
                                $scope.conditions[i].active = false;
                            }
                            $scope.conditions[$index].active = true;
                        }
                        $scope.onMultiAct = 0;
                    }

                    //ifNull => btnAll.active
                    btnAllActive();
                    saveStatus();

                    $scope.filterCondition = $scope.conditions;
                }

                function btnAllActive() {
                    var btnActiveData = [];
                    for (var i = 0; i < $scope.conditions.length; i++) {
                        if ($scope.conditions[i].active) {
                            btnActiveData.push($scope.conditions[i].data);
                        }
                    }
                    if (!btnActiveData.length) {
                        btnActiveData.push('all');
                    }
                    if (btnActiveData.length == 1 && btnActiveData[0] == 'all') {
                        $scope.filterConfig.enableAllBtn ? $scope.conditions[0].active = true : null;
                    } else {
                        $scope.filterConfig.enableAllBtn ? $scope.conditions[0].active = false : null;
                    }
                }

                function saveStatus() {
                    if ($scope.filterConfig.saveStatus) {
                        var tempArr = [];
                        for (var i = 0; i < $scope.conditions.length; i++) {
                            if ($scope.conditions[i].active) {
                                tempArr.push($scope.conditions[i].data);
                            }
                        }
                        var tempStr = JSON.stringify(tempArr);
                        $scope.filterConfig.storageFunction.setItem($scope.filterConfig.identifier, tempStr);
                    }
                }

                init();
            }
        }
    });
})();

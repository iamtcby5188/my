(function (angular, react) {

    var depModules = [
            // 'ui.bootstrap', 
            // 'ngTouch', 

            'ui.grid',
            'ui.grid.autoResize',
            'ui.grid.selection',
            'ui.grid.treeView',
            'ui.grid.moveColumns',
            'ui.grid.pinning',
            'ui.grid.resizeColumns',
            'ui.grid.exporter',
            'ui.grid.pagination',
            'ui.grid.saveState'
        ];

    if (react) depModules.push('react');

    var testModule = undefined;

    try {
        testModule = angular.module('ui.bootstrap');
    } catch (e) {

    }

    if (testModule) depModules.push('ui.bootstrap');

    console.debug("testModule: " + testModule);

    // Define main module
    var mainModule = angular.module('avalon.ui', depModules).constant('avalonUiConfig', {
        baseUrl: ""
        , templateList: [{"key":"template/checkbox.html","value":"﻿<div class=\"ss-input-control ss-checkbox\" ng-click=\"onClick($event)\">\r\n    <input type=\"checkbox\" ng-model=\"value\" ng-change=\"onChangeChecked($event)\" ng-disabled=\"{{isDisabled}}\">\r\n    <i class=\"check ss-icon ss-icon-primary ss-icon-checkbox-{{value?'selected':'unselected'}}\" ng-disabled=\"{{isDisabled}}\"></i>\r\n    <span class=\"caption\" ng-class=\"{'ss-text-disabled':isDisabled}\" ng-bind=\"label\" ng-disabled=\"{{isDisabled}}\"></span>\r\n</div>\r\n"},{"key":"template/datepicker_template.html","value":"﻿<div class=\"qbdatepickerRect\">\r\n    <div class=\"globalCover\" ng-show=\"showDatepicker\" ng-click=\"datepickerDismiss()\"></div>\r\n    <div class=\"inputRect\">\r\n        <!-- ng-model-options=\"{ updateOn: 'blur' }\" 可以通过设置变更parsers的触发时机不需要注册 ng-blur=\"blur()\" -->\r\n        <input class=\"defaultDatePickerInput ss-input-text ss-input-text-sm {{pickerConfig.class}} {{showDatepicker&&!pickerConfig.readOnly?'inputable':''}}\"\r\n               ng-class=\"{isEditing:isEditing === true}\"\r\n               ng-focus=\"selectInput($event)\"\r\n               ng-model=\"pickerDateString\"\r\n               ng-model-options=\"{ updateOn: 'blur' }\"\r\n               ng-blur=\"blur()\"\r\n               ng-readonly=\"{{pickerConfig.readOnly}}\"\r\n               ng-keydown=\"keydown($event)\"\r\n               ng-click=\"initDatepicker()\" />\r\n        <div class=\"rangeTipRect\">\r\n            <div class=\"rangeTip\" style=\"opacity:{{upperRangeTip||lowerRangeTip?'1':'0'}};width:{{upperRangeTip||lowerRangeTip?'auto':'0'}};\">\r\n                <div class=\"angle\"></div>\r\n                <div class=\"tip{{upperRangeTip?'':' ng-hide'}}\">日期不能晚于{{upperRangeDate}}</div>\r\n                <div class=\"tip{{lowerRangeTip?'':' ng-hide'}}\">日期不能早于{{lowerRangeDate}}</div>\r\n            </div>\r\n        </div>\r\n    </div>\r\n    <div class=\"qbdatepicker\" ng-show=\"showDatepicker\">\r\n        <div class=\"pickerHeader1\">\r\n            <div class=\"headBox1 ml_5\">\r\n                <input ng-model=\"currentYear\" />\r\n                <dl><dd class=\"datepicker_arrow plus\" ng-click=\"currentYear=currentYear+1\"></dd><dd class=\"datepicker_arrow reduce\" ng-click=\"currentYear-1>pickerConfig.yearMin?currentYear=currentYear-1:currentYear=pickerConfig.yearMin\"></dd></dl>\r\n            </div><span class=\"headerLabel\">&#24180</span>\r\n            <div class=\"headBox1 ml_15\">\r\n                <input ng-model=\"currentMonth\" />\r\n                <dl><dd class=\"datepicker_arrow plus\" ng-click=\"currentMonth+1>12?currentMonth=1:currentMonth=currentMonth+1\"></dd><dd class=\"datepicker_arrow reduce\" ng-click=\"currentMonth-1<1?currentMonth=12:currentMonth=currentMonth-1\"></dd></dl>\r\n            </div><span class=\"headerLabel\">&#26376</span>\r\n        </div>\r\n        <div class=\"pickerHeader2\"><span class=\"days\">&#26085</span><span class=\"days\">&#19968</span><span class=\"days\">&#20108</span><span class=\"days\">&#19977</span><span class=\"days\">&#22235</span><span class=\"days\">&#20116</span><span class=\"days\">&#20845</span></div>\r\n        <div class=\"dateCellRect\">\r\n            <!-- selectedDateInt==cell.timeStr?' isSelected':'' -->\r\n            <div class=\"dateCell{{cell.disable?'':' dateCellOn'}}{{cell.date==-1?' ng-hide':''}}{{todayDateInt==cell.timeStr?' isToday':''}}\"\r\n                 ng-class=\"{'isSelected': cell.timeStr === selectedDateInt, 'isToday': cell.isToday}\"\r\n                 ng-hide=\"cell.date==-1\" ng-repeat=\"cell in dateCells track by $index\" ng-click=\"selectDate(cell.timeStr,cell.date,cell.disable)\">{{cell.date?cell.date:'&nbsp;'}}</div>\r\n        </div>\r\n    </div>\r\n</div>"},{"key":"template/filter_template.html","value":"<div class=\"filterRect\">\r\n    <ul>\r\n        <li ng-repeat=\"condition in conditions track by $index\"><button class=\"ss-btn {{condition.active?'ss-btn-warning':'ss-btn-gray'}}\" type=\"button\" ng-click=\"filterBtnClick($event,$index)\">{{condition.title}}</button></li>\r\n    </ul>\r\n</div>"},{"key":"template/menu.html","value":"<div class=\"ss-menu\" ng-show=\"menuShow\">\r\n    <ul class=\"ss-text-secondary\" ng-class=\"{'ss-menu-active':mindex==$index,'ss-menu-icon':d.icon,'ss-menu-children':d.children}\" ng-repeat=\"d in data\" ng-mouseleave=\"menuOut()\" ng-mouseover=\"menuOver(d.children,$index)\" ng-click=\"d.event?d.event($event,d):menuClickFn($event,d)\">\r\n        {{d.title}}\r\n        <li ng-if=\"d.line\" ng-class=\"{'ss-menu-line':d.line}\"></li>\r\n        <div class=\"ss-menu ss-menu-first-children\" ng-class=\"{'ss-menu-showLeft':menuShowLeft,'ss-menu-showRight':!menuShowLeft}\" ng-if=\"showchildrenindex==$index\">\r\n            <ul class=\"ss-text-secondary\" ng-mouseover=\"childrenMenuOver($index)\" ng-mouseleave=\"childrenMenuOut()\"\r\n                ng-class=\"{'ss-menu-active':childrenhoverindex==$index,'ss-menu-icon':dc.icon}\" ng-repeat=\"dc in d.children track by $index\" ng-click=\"dc.event?dc.event($event,dc):menuClickFn($event,dc)\">\r\n                {{dc.title}}\r\n                <li ng-if=\"dc.line\" ng-class=\"{'ss-menu-line':dc.line}\"></li>\r\n            </ul>\r\n        </div>\r\n    </ul>\r\n</div>"},{"key":"template/modal.html","value":"<div class=\"ss-modal\">\r\n   <div id=\"box\" class=\"ss-modal-dialog\">\r\n      <div  class=\"ss-modal-content\">\r\n         <div class=\"ss-modal-header\" id=\"bar\">\r\n            <button type=\"button\" class=\"close cancel\" \r\n               data-dismiss=\"ss-modal\" aria-hidden=\"true\">\r\n                  &times;\r\n            </button>\r\n            <h4  class=\"ss-modal-title\" id=\"myModalLabel\">\r\n          \r\n            </h4>\r\n         </div>\r\n         <div class=\"ss-modal-body\" align=\"center\" >\r\n         \t<!--<div class=\"icon\">\t\r\n         \t</div>               -->\r\n           <div class=\"ss-modal-body-content\">\r\n          \r\n           </div>\r\n         </div>\r\n        <div class=\"ss-modal-footer\">\r\n         \r\n         </div>\r\n      </div><!-- /.modal-content -->\r\n</div><!-- /.modal -->\r\n</div>"},{"key":"template/radiobutton.html","value":"﻿<div class=\"ss-input-control ss-radiobutton-group\">\r\n    <div class=\"ss-radiobutton\" ng-click=\"onClick($event)\">\r\n        <input type=\"radio\" ng-checked=\"targetValue && value === targetValue\" ng-disabled=\"{{isDisabled}}\">\r\n        <i class=\"check ss-icon ss-icon-primary ss-icon-radiobutton-{{(targetValue && value === targetValue)?'selected':'unselected'}}\" ng-disabled=\"{{isDisabled}}\"></i>\r\n        <span class=\"caption\" ng-class=\"{'ss-text-disabled':isDisabled}\" ng-bind=\"label\" ng-disabled=\"{{isDisabled}}\"></span>\r\n    </div>\r\n</div>"},{"key":"template/modal.html","value":"<div class=\"ss-modal\">\r\n    <div id=\"box\" class=\"ss-modal-dialog\">\r\n        <div class=\"ss-modal-content\">\r\n            <div class=\"ss-modal-header\" id=\"bar\">\r\n                <button type=\"button\" class=\"close cancel\"\r\n                        data-dismiss=\"ss-modal\" aria-hidden=\"true\">\r\n                    &times;\r\n                </button>\r\n                <h4 class=\"ss-modal-title\" id=\"myModalLabel\">\r\n                    表格设置\r\n                </h4>\r\n            </div>\r\n            <div ng-if=\"columsSettingNoGroup\" class=\"ss-modal-body\" align=\"left\" style=\"margin-left: 42px;\">\r\n                <!--<div ng-repeat=\"col in columns_setting | filter:{'group':'user'}\" class=\"column-setting-no-group\">-->\r\n                <div ng-repeat=\"col in rangeeee(0,10) track by $index\" class=\"column-setting-no-group\">\r\n                    <input type=\"checkbox\" class=\"ss-checkbox-directive\"\r\n                           ng-model=\"col.checked\" ng-disabled=\"col.unchecked\" ss-label={{col.field}}>\r\n                </div>\r\n\r\n                <p class=\"columns-setting-modal-line-no-group\"></p>\r\n\r\n                <div ng-repeat=\"col in rangeeee(10,20) track by $index\" class=\"column-setting-no-group\">\r\n                    <input type=\"checkbox\" class=\"ss-checkbox-directive\"\r\n                           ng-model=\"col.checked\" ng-disabled=\"col.unchecked\" ss-label={{col.field}}>\r\n                </div>\r\n                <p class=\"columns-setting-modal-line-no-group\"></p>\r\n\r\n                <div ng-repeat=\"col in rangeeee(20,100) track by $index\" class=\"column-setting-no-group\">\r\n                    <input type=\"checkbox\" class=\"ss-checkbox-directive\"\r\n                           ng-model=\"col.checked\" ng-disabled=\"col.unchecked\" ss-label={{col.field}}>\r\n                </div>\r\n                <p class=\"columns-setting-modal-line-no-group\"></p>\r\n            </div>\r\n\r\n\r\n            <!--grid-columns-group-->\r\n            <div ng-if=\"columsSettingByGroup\" class=\"ss-modal-body clearfix\" align=\"left\" style=\"margin-left: 42px;display: table\">\r\n                <p class=\"ss-text-extra columns-setting-group-style\">user</p>\r\n                <div ng-repeat=\"col in rangeeee(0,8)\" class=\"column-setting-group\">\r\n                    <input type=\"checkbox\" class=\"ss-checkbox-directive\" ng-model=\"col.checked\" ng-disabled=\"col.unchecked\" ss-label={{col.field}}>\r\n                </div>\r\n\r\n                <p class=\"ss-text-extra columns-setting-modal-line-group\"></p>\r\n                <p class=\"ss-text-extra columns-setting-group-style\">extra</p>\r\n                <div ng-repeat=\"col in rangeeee(8,16)\" class=\"column-setting-group\">\r\n                    <input type=\"checkbox\" class=\"ss-checkbox-directive\" ng-model=\"col.checked\" ng-disabled=\"col.unchecked\" ss-label={{col.field}}>\r\n                </div>\r\n                <p class=\"ss-text-extra columns-setting-modal-line-group\"></p>\r\n            </div>\r\n            <div class=\"columns-unchecked-tooltip\" ng-show=\"result\">请至少选择1项</div>\r\n            <div class=\"ss-modal-footer\" style=\"bottom: 20px\">\r\n            </div>\r\n        </div><!-- /.modal-content -->\r\n    </div><!-- /.modal -->\r\n</div>"}]
    });
    
    // Module Run
    mainModule.run(['$templateCache', '$http', 'avalonUiConfig', function ($templateCache, $http, config) {
            
            if (config.templateList && config.templateList.length > 0 && config.templateList instanceof Array) {
                config.templateList.forEach(function (item, index) {
                    $templateCache.put(item.key, item.value);
                });
            }
        }]);
    
    // Service utility
    mainModule.service('avalon.ui.utility', [function () {

        // getPropertyX
            function getPropertyXFunc(obj, prop) {
                if (!obj) return undefined;
                
                if (!prop || prop.indexOf(".") < 0) return obj[prop];
                
                var arr = prop.split(".");
                var firstProp = arr.shift();
                
                if (!obj.hasOwnProperty(firstProp)) return undefined;
                
                return getPropertyXFuncArr(obj[firstProp], arr);
            };
            
            function getPropertyXFuncArr(obj, propArr) {
                if (!propArr || !obj) return obj;
                
                var firstProp = propArr.shift();
                
                if (propArr.length === 0) return obj[firstProp];
                
                return getPropertyXFuncArr(obj[firstProp], propArr);
            };
            
            this.getProperty = getPropertyXFunc;
            
            // setProppertyX
            function setPropertyXFunc(obj, prop, value) {
                if (!obj) obj = {};
                
                if (prop.indexOf(".") < 0) return obj[prop] = value;
                
                var arr = prop.split(".");
                var firstProp = arr.shift();
                
                if (!obj[firstProp]) obj[firstProp] = {};
                
                setPropertyXFuncArr(obj[firstProp], arr, value);
                
                return obj;
            };
            
            function setPropertyXFuncArr(obj, propArr, value) {
                if (!propArr) return;
                
                var firstProp = propArr.shift();
                
                if (propArr.length === 0) obj[firstProp] = value;
                else {
                    if (!obj[firstProp]) obj[firstProp] = {};
                    setPropertyXFuncArr(obj[firstProp], propArr, value);
                }
            };
            
            this.setPropperty = setPropertyXFunc;
            
            this.safeApply = function (scope, fn) {
                var phase = scope.$root.$$phase;
                if (phase === '$apply' || phase === '$digest') {
                    if (fn && (typeof (fn) === 'function')) {
                        fn();
                    }
                } else {
                    try {
                        scope.$apply(fn);
                    } catch (e) {

                    }
                }
            };
        }]);

})(window.angular, window.React);
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
(function () {
    /*
     *  Quoteboard datepicker for Angular 1.x
     *  @author zhaokun.zhang@sumscope.com ,lai.wei@sumscope.com
     *  @version (2016-10-25)
     */

    angular.module('avalon.ui').directive('ssDatepickerDirective', function () {
        return {
            restrict: "C", //ECMA 使用class注册，在使用的时候便于装卸
            replace: true,
            require: 'ngModel', // 使用AngularJs的Model
            scope: {
                pickerConfig: '=?',

                format: '@',
                readOnly: '@',
                yearMax: '@',
                yearMin: '@',
                upperRange: '@',
                lowerRange: '@',
                nullValueString: '@',

                vmChanged: '&'
            },
            // template: datepickerTemplate,
            // 可以直接指定模板文件，路径应该是config里面buildAs对应的template项所设置的export路径
            // {"file": "datepicker_template02.html", "type": "html", "buildAs": "template", "export": "template/datepicker_template.html"}
            // html的模板可维护性更好，而且这样也不用担心会产生get html文件所产生的性能问题，template的内容在打包的时候会混进js里面。
            templateUrl: "template/datepicker_template.html",
            link: function ($scope, $element, $attrs) {

            },

            compile: function (cElem, cAttr) {

                return {
                    pre: function preLink($scope, element, attributes, ctrl) {

                        var currentYearTimer = 0;
                        var dateCells = [];
                        var day = 0;
                        for (var i = 0; i < 42; i++) {
                            dateCells.push({
                                "day": day - Math.floor(day / 7) * 7,
                                "date": 0,
                                "timeStr": 0
                            });
                            day = day + 1;
                        }

                        function getTimeByDays(days) {
                            if (!days) return undefined;
                            days = +days;
                            return (new Date()).getTime() + 86400000 * days;
                        };

                        $scope.pickerConfig = $scope.pickerConfig || {
                            "format": $scope.format || "yyyy.mm.dd", //yyyy年m月d日：日期选择器将会使用正则replace的方法根据format替换出可读的日期文本返回到input框（yyyy:年，m:不强制2位数的月份，mm：强制2位数的月份，d：不强制2位数的日，dd：强制2位数的日）缺省默认yyyy-mm-dd；
                            "readOnly": $scope.readOnly || false, //日期选择器的input框是否可读写，缺省默认可读可写；
                            "yearMax": $scope.yearMax || 9999, //日期选择器年份上限，缺省默认9999；
                            "yearMin": $scope.yearMin || 1900, //日期选择器年份下限，缺省默认1900；
                            "upperRange": getTimeByDays($scope.upperRange), //日期选择最大限制，缺省默认Int(99999999999999)，手动输入日期如果超限，将会有气泡提示；
                            "lowerRange": getTimeByDays($scope.lowerRange)  //日期选择最小限制，缺省默认Int(-62135596800000)，手动输入日期如果超限，将会有气泡提示；
                        };

                        //验证时间数据格式是否正确(数据正确返回日期对象，数据非法返回null)
                        function validateDateData(date) {
                            date = date || ($scope.nullValueString ? undefined : new Date());
                            if (!date) return date;

                            var dateDataType = date.constructor.name;
                            if (dateDataType == 'String') {
                                date.length < 3 ? date = 'error' : null;
                            }
                            if (dateDataType != 'Date') {
                                date = new Date(date);
                            }
                            return date;
                        };

                        //验证时间范围
                        function validateDateRange(dateObj) {
                            dateObj.getYear() + 1900 < $scope.pickerConfig.yearMin ? dateObj.setYear($scope.pickerConfig.yearMin) : null;
                            dateObj.getYear() + 1900 > $scope.pickerConfig.yearMax ? dateObj.setYear($scope.pickerConfig.yearMax) : null;
                            dateObj.getTime() > $scope.pickerConfig.upperRange ? $scope.upperRangeTip = true : $scope.upperRangeTip = false; //upperRangeDate
                            dateObj.getTime() < $scope.pickerConfig.lowerRange ? $scope.lowerRangeTip = true : $scope.lowerRangeTip = false; //lowerRangeDate
                            return dateObj;
                        };

                        /*
                         *  @description Set date to scope.current*
                         */
                        function setDateToScope(dateObj) {
                            $scope.currentYear = dateObj.getYear() + 1900;
                            $scope.currentMonth = dateObj.getMonth() + 1;
                            $scope.currentDate = dateObj.getDate();
                        };

                        $scope.$outterModelCtrl = ctrl;

                        // 将模块外输入的值转换为模块内ngModel数据的方法
                        ctrl.$formatters.unshift(function (value) {
                            var dateData = validateDateData(value);

                            var viewDate = dateData;

                            var formattedString = $scope.nullValueString ? $scope.nullValueString : '';

                            if (dateData && dateData.toString() !== 'Invalid Date') {

                                viewDate.setHours(0);
                                viewDate.setMinutes(0);
                                viewDate.setSeconds(0);
                                viewDate.setMilliseconds(0);

                                viewDate = validateDateRange(viewDate);
                                setDateToScope(viewDate);

                                $scope.selectedDateInt = viewDate.getTime();
                                viewDate.setTime(viewDate.getTime() - viewDate.getTimezoneOffset() * 60000); //时区归0，默认中国+800坑爹╮(╯_╰)╭

                                formattedString = $scope.genDateStr();
                            } else {
                                setDateToScope(new Date());
                            }

                            $scope.pickerDateString = formattedString;

                            return formattedString;
                        });

                        // 将ngModel数据转换为对外输出值的方法
                        ctrl.$parsers.unshift(function () {

                            if ($scope.isEditing && ctrl.$viewValue) {
                                return ctrl.$modelValue;
                            }

                            var dateData = validateDateData(ctrl.$viewValue);

                            var viewDate = dateData;
                            if (dateData && dateData.toString() !== 'Invalid Date') {
                                $scope.selectedDateInt = viewDate.getTime();

                                viewDate.setHours(0);
                                viewDate.setMinutes(0);
                                viewDate.setSeconds(0);
                                viewDate.setMilliseconds(0);
                                viewDate = validateDateRange(viewDate);
                                setDateToScope(viewDate);
                                viewDate.setTime(viewDate.getTime() - viewDate.getTimezoneOffset() * 60000); //时区归0，默认中国+800坑爹╮(╯_╰)╭
                                $scope.pickerDateString = $scope.genDateStr();
                            } else {
                                setDateToScope(new Date());
                                $scope.pickerDateString = $scope.nullValueString || '';
                            }

                            return viewDate;
                        });

                        /*
                         *  @description Generate dateCells
                         *  @globalParam {Number} currentYear #$scope.currentYear
                         *  @globalParam {Number} currentMonth #$scope.currentMonth
                         *  @return {Object} dateCells #$scope.dateCells
                         */
                        function genDateCells() {
                            var currentYearMonthDate = new Date($scope.currentYear, $scope.currentMonth - 1, 1);
                            var curMonthDays = new Date(currentYearMonthDate.getFullYear(), (currentYearMonthDate.getMonth() + 1), 0).getDate();
                            var curMonthDay = currentYearMonthDate.getDay();
                            var tempCells = dateCells;
                            var curDate = 0;
                            var dateCellLast = 0;

                            var now = new Date();

                            var nowMonthVal = now.getMonth(),
                                nowDayVal = now.getDate();

                            for (var i = 0; i < tempCells.length; i++) {
                                tempCells[i].date = 0;
                                tempCells[i].timeStr = 0;
                                tempCells[i].disable = 0;
                                if (i > curMonthDay - 1) {
                                    curDate = curDate + 1;
                                    tempCells[i].disable = 1;
                                    tempCells[i].timeStr = -1000000000;
                                }
                                if (curDate <= curMonthDays) {
                                    tempCells[i].date = curDate;
                                    dateCellLast = i;
                                    tempCells[i].timeStr = (new Date($scope.currentYear, $scope.currentMonth - 1, curDate)).getTime();
                                    tempCells[i].timeStr > $scope.pickerConfig.upperRange || tempCells[i].timeStr < $scope.pickerConfig.lowerRange || !curDate ? tempCells[i].disable = 1 : tempCells[i].disable = 0;
                                }
                                if (curDate > curMonthDays) {
                                    dateCellLast > 34 ? tempCells[i].date = 0 : i < 35 ? tempCells[i].date = 0 : tempCells[i].date = -1;
                                    tempCells[i].disable = 1;
                                    tempCells[i].timeStr = -1000000000;
                                }

                                if (tempCells[i].timeStr) {
                                    var date = new Date(tempCells[i].timeStr);
                                    tempCells[i].isToday = nowDayVal === date.getDate() && nowMonthVal === date.getMonth();
                                }
                            }
                            $scope.dateCells = tempCells;

                        }

                        $scope.$watch('currentYear', function (nv, ov) {
                            if (nv * 1 != nv * 1) {
                                $scope.currentYear = ov;
                                return;
                            } else if (nv > $scope.pickerConfig.yearMax) {
                                $scope.currentYear = ov;
                                return;
                            } else if (nv < 1000) {
                                clearTimeout(currentYearTimer);
                                (function (nv) {
                                    currentYearTimer = setTimeout(function () {
                                        if ($scope.currentYear == nv) {
                                            $scope.currentYear = (new Date()).getYear() + 1900;
                                            $scope.$apply();
                                        }
                                    }, 2000);
                                })(nv);
                                return;
                            } else if (nv > 0) {
                                $scope.currentYear = Math.floor($scope.currentYear);
                                genDateCells();
                            }
                            $scope.currentYear = $scope.currentYear * 1;
                            if ($scope.currentYear > 999) {
                                if ($scope.currentYear < $scope.pickerConfig.yearMin) {
                                    $scope.currentYear = (new Date()).getYear() + 1900;
                                } else if ($scope.currentYear > $scope.pickerConfig.yearMax) {
                                    $scope.currentYear = (new Date()).getYear() + 1900;
                                }
                            }
                        });

                        $scope.$watch('currentMonth', function (nv, ov) {
                            if (nv * 1 != nv * 1) {
                                $scope.currentMonth = ov;
                                return;
                            } else if (nv > 12) {
                                $scope.currentMonth = 12;
                                return;
                            } else if (nv < 1) {
                                setTimeout(function () {
                                    if ($scope.currentMonth < 1) {
                                        $scope.currentMonth = 1;
                                        $scope.$apply();
                                    }
                                }, 1000);
                                return;
                            } else if (nv > 0 & nv < 13) {
                                $scope.currentMonth = Math.floor($scope.currentMonth);
                                genDateCells();
                            }
                            $scope.currentMonth = $scope.currentMonth * 1;
                        });
                    },
                    post: function postLink(poScope, poElement, poAttributes, poNgModelCtrl) {
                        function setHMSto0(timeInt) {
                            var tempDate = new Date(timeInt);
                            tempDate.setHours(0);
                            tempDate.setMinutes(0);
                            tempDate.setSeconds(0);
                            tempDate.setMilliseconds(0);
                            return tempDate.getTime();
                        }

                        poScope.pickerConfig = function (config) {
                            config = config || {};

                            var pickerConfig = {
                                "class": config.class || '',
                                "format": config.format || "yyyy-mm-dd",
                                "readOnly": config.readOnly || false,
                                "yearMax": config.yearMax || 9999,
                                "yearMin": config.yearMin || 1900,
                                "upperRange": config.upperRange || 253402214400000,
                                "lowerRange": config.lowerRange || -62135596800000
                            };

                            pickerConfig.upperRange = setHMSto0(pickerConfig.upperRange);
                            pickerConfig.lowerRange = setHMSto0(pickerConfig.lowerRange);

                            return pickerConfig;

                        } (poScope.pickerConfig);

                        var upperRangeDate = new Date(poScope.pickerConfig.upperRange);
                        var lowerRangeDate = new Date(poScope.pickerConfig.lowerRange);
                        poScope.upperRangeDate = poScope.genDateStr(null, upperRangeDate.getYear() + 1900, upperRangeDate.getMonth() + 1, upperRangeDate.getDate());
                        poScope.lowerRangeDate = poScope.genDateStr(null, lowerRangeDate.getYear() + 1900, lowerRangeDate.getMonth() + 1, lowerRangeDate.getDate());

                        poScope.todayDateInt = new Date(setHMSto0((new Date()))).getTime();
                    }
                };

            },

            controller: ['$scope', function ($scope) {

                /*  @description Generate date word by timeString
                 *  @globalParam {Number} currentYear #$scope.currentYear
                 *  @globalParam {Number} currentMonth #$scope.currentMonth
                 *  @globalParam {Number} currentDate #$scope.currentDate
                 *  @param {String} formatStr #source format date string
                 *  @return {String} formatStr #Replaced formate date string
                 */
                $scope.genDateStr = function (formatStr, year, month, date) {
                    formatStr = formatStr || String($scope.pickerConfig.format);

                    if (!formatStr) return '';

                    var yyyy = year || String($scope.currentYear);
                    var mm = month || String($scope.currentMonth);
                    var dd = date || String($scope.currentDate);

                    var m = mm;
                    var d = dd;
                    mm < 10 ? mm = '0' + mm : null;
                    dd < 10 ? dd = '0' + dd : null;
                    formatStr = formatStr.replace(/yyyy/, yyyy);
                    formatStr = formatStr.replace(/mm/, mm);
                    formatStr = formatStr.replace(/dd/, dd);
                    formatStr = formatStr.replace(/m/, m);
                    formatStr = formatStr.replace(/d/, d);
                    return formatStr;
                };

                /*  press enter to confirm
                 *  @param {Event} $event #angular ng-click event
                 */
                $scope.keydown = function (event) {
                    if (event.keyCode === 13) {
                        event.target.blur();
                        $scope.showDatepicker = false;
                        $scope.isEditing = false;
                    } else {
                        $scope.isEditing = true;
                    }
                };

                $scope.blur = function () {

                    // deleted by WeiLai on 2017/03/22
                    // vmChanged 触发了两次

                    //if ($scope.pickerDateString) {
                    //    var dateString = $scope.pickerDateString.replace(/-/g, '.');

                    //    $scope.$outterModelCtrl.$viewValue = dateString;
                    //    $scope.$outterModelCtrl.$commitViewValue();

                    //    console.debug('$scope.vmChanged in $scope.blur');
                    //    if ($scope.vmChanged) $scope.vmChanged();
                    //}
                };

                /*  dateCell select event
                 *  @param {Number} timeStr #Trigger dateCell's timeStr
                 *  @param {Number} curDate #Trigger dateCell's date
                 */
                $scope.selectDate = function (timeStr, curDate, disable) {
                    if (curDate && $scope.currentYear >= $scope.pickerConfig.yearMin && !disable) {
                        $scope.currentDate = curDate;

                        $scope.$outterModelCtrl.$viewValue = timeStr;
                        $scope.$outterModelCtrl.$commitViewValue();

                        // console.debug('$scope.vmChanged in $scope.selectDate');
                        if ($scope.vmChanged) $scope.vmChanged();

                        $scope.showDatepicker = false;
                    }
                };

                /* datepickerDismiss
                 *  @decription datepicker dissmiss event
                 */
                $scope.datepickerDismiss = function () {
                    $scope.showDatepicker = false;
                    $scope.isEditing = false;

                    // deleted by WeiLai on 2017/03/22
                    // vmChanged 触发了两次
                    // $scope.blur();
                };

                // ng-focus event
                $scope.selectInput = function ($event) {
                    if (!$scope.pickerConfig.readOnly) {
                        setTimeout(function () {
                            $event.target.select();
                        }, 100);
                    }
                };

                // ng-click event
                /*  initDatepicker datepicker and show it
                 *  @description input click event initDatepicker
                 */
                $scope.initDatepicker = function () {
                    $scope.showDatepicker = true;
                };

                //$scope.updatePickerDateString = function () {

                //}

                // Controller 中的初始化function，如何实现无所谓，但须确保只执行一次
                var init = function () {
                    $scope.showDatepicker = false;
                    // debugger;
                } ();


            }]

        }
    });
})();

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

/*
 *  Quoteboard menu for Angular 1.x
 *  @author shuang.wang@sumscope.com
 *  @version (2016-09-19)
 */


angular.module('avalon.ui').directive('ssMenuDirective', [

    // Note：当前展开的Menu做单例控制
    // Service？
    // Class？ 

    function() {
        return {
            restrict: 'C',
            replace: true,
            scope: {
                //传入的菜单数据
                data: '=menuData',
                //点击菜单，将数据传入controller
                menuClickData: '=menuClick',
                //禁止滚动的元素
                menuForbidScrolldom: '=menuForbidScrolldom',
                //是否不应用边界检测，默认为false
                menuForced: '=menuForced'
                
                // Note： &onClickMenuItem
            },
            //template: '<div class="ss-menu" ng-show="menuShow">' +
            //    '<ul class="ss-text-secondary" ng-class="{\'ss-menu-active\':mindex==$index,\'ss-menu-icon\':d.icon,\'ss-menu-children\':d.children}" ng-repeat="d in data" ng-mouseleave="menuOut()" ng-mouseover="menuOver(d.children,$index)" ng-click="d.event?d.event($event,d):menuClickFn($event,d)">{{d.title}}' +
            //    '<li ng-if="d.line" ng-class="{\'ss-menu-line\':d.line}"></li>' +
            //    '<div class="ss-menu ss-menu-first-children" ng-class="{\'ss-menu-showLeft\':menuShowLeft,\'ss-menu-showRight\':!menuShowLeft}" ng-if="showchildrenindex==$index">' +
            //    '<ul class="ss-text-secondary" ng-mouseover="childrenMenuOver($index)" ng-mouseleave="childrenMenuOut()" ' +
            //    'ng-class="{\'ss-menu-active\':childrenhoverindex==$index,\'ss-menu-icon\':dc.icon}" ng-repeat="dc in d.children track by $index" ng-click="dc.event?dc.event($event,dc):menuClickFn($event,dc)">{{dc.title}}' +
            //    '<li ng-if="dc.line" ng-class="{\'ss-menu-line\':dc.line}"></li>' +
            //    '</ul>' +
            //    '</div>' +
            //    '</ul> ' +
            //    '</div>',
            // Note：可以通过templateUrl来指定模板，便于编辑
            templateUrl: 'template/menu.html',
            link: function(scope, ele, attr) {
                /*
                     * 第一层菜单的索引，主要处理鼠标悬浮
                     * scope.mindex = -1;
                     * 第一层菜单的显示子菜单索引，主要处理显示子菜单
                     * scope.showchildrenindex = -1;
                     * 第一层菜单的子菜单索引，主要用于处理子菜单悬浮
                     * scope.childrenhoverindex = -1;
                     * */
                //存储禁止的事件
                var forbidScrollEvents = [];
                // var menuContextmenuDom = document.getElementsByClassName('ss-menu-contextmenu')[0];
                var menuContextmenuDom = ele.parent('.ss-menu-contextmenu');
                // document.getElementsByClassName('ss-menu-contextmenu')[0];
                var menuContextmenu = angular.element(menuContextmenuDom);

                if (menuContextmenu)
                    menuContextmenu.on('contextmenu', function(e) {
                        scope.$apply(function() {
                            e.preventDefault();
                            //显示菜单
                            scope.menuShow = true;
                            scope.mindex = -1;
                            scope.showchildrenindex = -1;
                            scope.childrenhoverindex = -1;
                            scope.clientX = e.clientX;
                            ele.css({
                                'left': calcX(menuContextmenu, ele, e.clientX, 0).width + 'px',
                                'top': calcY(menuContextmenu, ele, e.clientY, $('.ss-menu ul').height() * scope.data.length).height + 'px'
                            });
                        });
                        if (scope.menuForbidScrolldom)
                            if (scope.menuForbidScrolldom[scope.menuForbidScrolldom.length - 1] != "true") {
                                $.each(scope.menuForbidScrolldom, function(k, e) {
                                    if ($(e))
                                        $(e).off('scroll').on('scroll', function() {
                                            $scope.$apply(function() {
                                                scope.menuShow = false;
                                            });
                                        });
                                });
                            } else {
                                $.each(scope.menuForbidScrolldom, function(k, e) {
                                    forbidScrollEvents.push($(e).scroll);
                                    $(e).off('scroll');
                                    var eScrollTop = $(e).scrollTop();
                                    $(e).on('scroll', function() {
                                        $(this).scrollTop(eScrollTop);
                                        console.log($(this).scrollTop());
                                    });
                                });
                            }
                    });

                $(document).on('click', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    // Note：没有双绑的情况下$apply也没问题，推荐使用safeApply
                    scope.$apply(function() {
                        if (scope.menuShow)
                            scope.menuShow = false;
                    });
                    //去掉禁止滚动事件
                    if (scope.menuForbidScrolldom && scope.menuForbidScrolldom.length > 0) {
                        $.each(scope.menuForbidScrolldom, function(k, e) {
                            if ($(e))
                                $(e).off('scroll');
                        });
                    }
                });

                //鼠标悬浮
                scope.menuOver = function(hasChildren, index) {
                    scope.mindex = index;
                    scope.showchildrenindex = hasChildren ? index : -1;

                    if (hasChildren) {
                        if (!calcX(menuContextmenu, ele, scope.clientX, 1).istrue) {
                            scope.menuShowLeft = false;
                        } else {
                            scope.menuShowLeft = true;
                        }
                    }
                };
                scope.childrenMenuOver = function(index) {
                    scope.childrenhoverindex = index;
                };
                scope.childrenMenuOut = function(index) {
                    scope.childrenhoverindex = -1;
                };

                //菜单点击事件
                scope.menuClickFn = function($event, obj) {
                    scope.menuClickData = obj;
                    $event.stopPropagation();
                };

                /*
                     * 计算菜单到右边的距离
                     * parentele 容器dom
                     * element 菜单dom
                     * x e.clientX
                     * times 0代表一级菜单,1代表二级菜单，2代表三级菜单，以此类推
                     * return obj
                     * */
                var calcX = function(parentele, element, x, times) {
                    var obj = {
                        width: 0,
                        istrue: true
                    };
                    //判断父级是否有横向滚动条，如果有，则改变可视区域为父级的宽
                    if ($(parentele).parent().width() < $(parentele).width() && ($(parentele).parent().css('overflow') == 'auto' || $(parentele).parent().css('overflow') == 'scroll')) {
                        parentele = $(parentele).parent();
                    }
                    if (!(scope.menuForced && scope.menuForced.x)) {
                        if ($(parentele).width() - (x - $(parentele).offset().left) - $(element).width() * times < $(element).width()) {
                            obj.width = x - $(element).width();
                            obj.istrue = false;
                        } else {
                            obj.width = x;
                        }
                    } else {
                        obj.width = x;
                    }
                    return obj;
                };

                /*
                     * 计算菜单到下边的距离
                     * parentele 容器dom
                     * element 菜单dom
                     * y e.clientY
                     * eleheight菜单的高度，指令中，用的单个菜单的高度*data.length+菜单.paddingTop+菜单.paddingBottom
                     * return obj
                     * */
                var calcY = function(parentele, element, y, eleheight) {
                    //判断父级是否有纵向滚动条，如果有，则改变可视区域为父级的高
                    if ($(parentele).parent().height() < $(parentele).height() && ($(parentele).parent().css('overflow') == 'auto' || $(parentele).parent().css('overflow') == 'scroll')) {
                        parentele = $(parentele).parent();
                    }
                    var obj = {
                        height: 0,
                        istrue: true
                    };
                    if (!(scope.menuForced && scope.menuForced.y)) {
                        if ($(parentele).height() - (y - ($(parentele).offset().top - $(document).scrollTop())) < (eleheight + parseInt($(element).css('paddingTop')) + parseInt($(element).css('paddingBottom')))) {
                            obj.height = y - eleheight;
                            obj.istrue = false;
                        } else
                            obj.height = y;
                    } else {
                        obj.height = y;
                    }
                    return obj;
                }

            }
        }
    }
]);
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
(function (angular, React) {
    'use strict';
    
    // React 为可选组件
    if (!React) {
        console.warn('AvalonUI: React and React-DOM not loaded.');
        return;
    }

    // 定义React控件
    var component = React.createClass({
        propTypes: {
            fname: React.PropTypes.string.isRequired,
            lname: React.PropTypes.string.isRequired
        },
        render: function () {
            return React.DOM.span(null, 'The name of Super Man is ' + this.props.fname + ' ' + this.props.lname);
        }
    });
    
    // 使用Directive注册
    angular.module('avalon.ui').directive("reactHelloComponentDirective", ['reactDirective', function (reactDirective) {
            return reactDirective(component);
        }]);
    
    // 使用Value注册
    angular.module('avalon.ui').value('reactHelloComponentValue', component);

    // 使用Service注册
    angular.module('avalon.ui').service('reactHelloComponentService', [function() {
        return component;
    }]);

})(window.angular, window.React);
'use strict';

(function (angular, React) {
    'use strict';

    // React 为可选组件

    if (!React) {
        console.warn('AvalonUI: React and React-DOM not loaded.');
        return;
    }

    // 定义JSX React控件
    var component = React.createClass({
        displayName: 'component',

        propTypes: {
            fname: React.PropTypes.string.isRequired,
            lname: React.PropTypes.string.isRequired
        },
        render: function render() {
            return React.createElement(
                'span',
                null,
                'The name of Super Man is ',
                this.props.fname,
                ' ',
                this.props.lname
            );
        }
    });

    // 使用Directive注册
    angular.module('avalon.ui').directive("reactHelloJsxComponentDirective", ['reactDirective', function (reactDirective) {
        return reactDirective(component);
    }]);
})(window.angular, window.React);
(function (amCharts) {
    
    // AmCharts Ϊ��ѡ���
    if (!amCharts) {
        console.warn('AvalonUI: Amcharts not loaded.');
        return;
    }
    
    amCharts.themes.ssChartStyle = {
        
        themeName: "ssChartStyle",
        
        AmChart: {
            color: "#e7e7e7",
            backgroundColor: "#161819",
            fontFamily: "Microsoft YaHei",
        },
        
        AmCoordinateChart: {
            colors: [
            // ��Ʒ����ṩ��10����ѡ��ɫ
                "#ffc96c", "#dc4444", "#4876cf", "#58b96d", "#b26ed0", "#dc6b35", "#7adc4e", "#25ba93", "#f04971", "#fbff1e",
                "#724887", "#7256bc"
            ]
        },
        
        AmStockChart: {
            colors: [
            // ��Ʒ����ṩ��8����ѡ��ɫ
                "#dc4444", "#dc6b35", "#fbff1e", "#7adc4e", "#25ba93", "#4876cf", "#b26ed0", "#f04971", 
                "#9d9888", "#916b8a", "#724887", "#7256bc"
            ]
        },
        
        AmSlicedChart: {
            outlineAlpha: 1,
            outlineThickness: 2,
            outlineColor: "#161819",
            labelTickColor: "#FFFFFF",
            labelTickAlpha: 0.3,
            colors: [
             // ��Ʒ����ṩ��8����ѡ��ɫ
                "#dc4444", "#dc6b35", "#fbff1e", "#7adc4e", "#25ba93", "#4876cf", "#b26ed0", "#f04971",
                "#9d9888", "#916b8a", "#724887", "#7256bc"
            ]
        },
        
        AmRectangularChart: {
            zoomOutButtonColor: "#FFFFFF",
            zoomOutButtonRollOverAlpha: 0.15,
            zoomOutButtonImage: "lensWhite"
        },
        
        AxisBase: {
            axisColor: "#FFFFFF",
            axisAlpha: 0.3,
            gridAlpha: 0.1,
            gridColor: "#FFFFFF",
            dashLength: 3
        },
        
        ChartScrollbar: {
            backgroundColor: "#000000",
            backgroundAlpha: 0.2,
            graphFillAlpha: 0.2,
            graphLineAlpha: 0,
            graphFillColor: "#FFFFFF",
            selectedGraphFillColor: "#FFFFFF",
            selectedGraphFillAlpha: 0.4,
            selectedGraphLineColor: "#FFFFFF",
            selectedBackgroundColor: "#FFFFFF",
            selectedBackgroundAlpha: 0.09,
            gridAlpha: 0.15
        },
        
        ChartCursor: {
            cursorColor: "#FFFFFF",
            color: "#000000",
            cursorAlpha: 0.5
        },
        
        AmLegend: {
            color: "#e7e7e7"
        },
        
        AmGraph: {
            lineAlpha: 0.9
        },
        
        GaugeArrow: {
            color: "#FFFFFF",
            alpha: 0.8,
            nailAlpha: 0,
            innerRadius: "40%",
            nailRadius: 15,
            startWidth: 15,
            borderAlpha: 0.8,
            nailBorderAlpha: 0
        },
        
        GaugeAxis: {
            tickColor: "#FFFFFF",
            tickAlpha: 1,
            tickLength: 15,
            minorTickLength: 8,
            axisThickness: 3,
            axisColor: "#FFFFFF",
            axisAlpha: 1,
            bandAlpha: 0.8
        },
        
        TrendLine: {
            lineColor: "#c03246",
            lineAlpha: 0.8
        },
        
        // ammap
        AreasSettings: {
            alpha: 0.8,
            color: "#666666",
            colorSolid: "#000000",
            unlistedAreasAlpha: 0.4,
            unlistedAreasColor: "#555555",
            outlineColor: "#000000",
            outlineAlpha: 0.5,
            outlineThickness: 0.5,
            rollOverBrightness: 30,
            slectedBrightness: 50,
            rollOverOutlineColor: "#000000",
            selectedOutlineColor: "#000000",
            unlistedAreasOutlineColor: "#000000",
            unlistedAreasOutlineAlpha: 0.5
        },
        
        LinesSettings: {
            color: "#555555",
            alpha: 0.8
        },
        
        ImagesSettings: {
            alpha: 0.8,
            labelColor: "#FFFFFF",
            color: "#FFFFFF",
            labelRollOverColor: "#3c5bdc"
        },
        
        ZoomControl: {
            buttonFillAlpha: 0.4
        },
        
        SmallMap: {
            mapColor: "#444444",
            rectangleColor: "#666666",
            backgroundColor: "#000000",
            backgroundAlpha: 0.5,
            borderColor: "#555555",
            borderThickness: 1,
            borderAlpha: 0.8
        },
        
        // the defaults below are set using CSS syntax, you can use any existing css property
        // if you don't use Stock chart, you can delete lines below
        PeriodSelector: {
            color: "#e7e7e7"
        },
        
        PeriodButton: {
            color: "#e7e7e7",
            background: "transparent",
            opacity: 0.7,
            border: "1px solid rgba(255, 255, 255, .15)",
            MozBorderRadius: "5px",
            borderRadius: "5px",
            margin: "1px",
            outline: "none",
            boxSizing: "border-box"
        },
        
        PeriodButtonSelected: {
            color: "#e7e7e7",
            backgroundColor: "rgba(255, 255, 255, 0.1)",
            border: "1px solid rgba(255, 255, 255, .3)",
            MozBorderRadius: "5px",
            borderRadius: "5px",
            margin: "1px",
            outline: "none",
            opacity: 1,
            boxSizing: "border-box"
        },
        
        PeriodInputField: {
            color: "#e7e7e7",
            background: "transparent",
            border: "1px solid rgba(255, 255, 255, .15)",
            outline: "none"
        },
        
        DataSetSelector: {
            color: "#e7e7e7",
            selectedBackgroundColor: "rgba(255, 255, 255, .25)",
            rollOverBackgroundColor: "rgba(255, 255, 255, .15)"
        },
        
        DataSetCompareList: {
            color: "#e7e7e7",
            lineHeight: "100%",
            boxSizing: "initial",
            webkitBoxSizing: "initial",
            border: "1px solid rgba(255, 255, 255, .15)"
        },
        
        DataSetSelect: {
            border: "1px solid rgba(255, 255, 255, .15)",
            outline: "none"
        }

    };
})(window.AmCharts);


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
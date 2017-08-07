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

/**
 * Created by jiannan.niu on 2016/12/13.
 */

const initView = Symbol('initView');

class gridTicketHallSSRCtrl {

    constructor ($scope, httpService, uiGridConstants, gridDataDefineService, qbService, gridColumnFactoryService, $interval, $timeout, $window, configConsts, websocketService, $q, babQuoteService, accountAssociateService, commonService, gridExcelImportService, userService) {
        this.$scope = $scope;
        $scope.$timeout = $timeout;
        $scope.qbService = qbService;
        $scope.commonService = commonService;
        this.commonService = commonService;
        this.gridExcelImportService = gridExcelImportService;
        this.configConsts = configConsts;
        this.httpService = httpService;
        this.gridDataDefineService = gridDataDefineService;
        this.gridColumnFactoryService = gridColumnFactoryService;
        this.websocketService = websocketService;
        this.userService = userService;
        $scope.gridDataDefineService = gridDataDefineService;
        this.$q = $q;
        this.babQuoteService = babQuoteService;
        this.accountAssociateService = accountAssociateService;
        $scope.showBill = gridColumnFactoryService.showBill;
        $scope.gridColumnFactoryService = gridColumnFactoryService;

        window.onresize = function () {
            gridColumnFactoryService.resizeGrid();
        };

        $scope.sortPeriod = function (e) {
            if (!e || !e.name || !e.enableSorting || !gridDataDefineService.sortField[e.name]) return;
            if (e.name !== '时间') {
                jQuery('.last-update-time i.ui-grid-icon-down-dir').removeClass('ui-grid-icon-down-dir').addClass('ui-grid-icon-blank');
            }
            $scope.filterParam.orderByPriceType = gridDataDefineService.sortField[e.name];
            $scope.filterParam.oderSeq = e.sort.direction.toUpperCase();
            $scope.filterParam.pageNumber = 0;
            $scope.gridApi.core.scrollTo($scope.gridOptions.data[0]);
            gridColumnFactoryService.gridInit($scope, configConsts.bab_search_SSR, $scope.filterParam);
        }

        $scope.getLastUpdateTime = gridColumnFactoryService.getLastUpdateTime;
        $scope.getRemainingDays = gridColumnFactoryService.getRemainingDays;
        $scope.copyQQ = gridColumnFactoryService.copyQQ;
        $scope.openQM = gridColumnFactoryService.openQM;
        $scope.getMobileTel = gridColumnFactoryService.getMobileTel;
        $scope.openMenu = function ($mdOpenMenu, ev, quote) {
            let tel = quote.containsAdditionalInfo ? quote.additionalInfo.contactTelephone : quote.contactDto.mobileTel;
            if (!tel) return;
            $mdOpenMenu(ev);
        };

        $scope.getDataDown = function () {
            $scope.filterParam.pageNumber++;
            gridColumnFactoryService.getDataDown($scope, configConsts.bab_search_SSR, $scope.filterParam);
        }

        $scope.saveState = (state) => {
            this.gridColumnFactoryService.saveState($scope, state);
        };

        $scope.restoreState = (state) => {
            this.gridColumnFactoryService.restoreState($scope, state);
        }

        $scope.data = [
            {
                title: '自定义列',
                icon: false,
                event: (event, obj) => {
                    this.gridExcelImportService.openGridColumnPanel(event, {
                        columnDefs: $scope.columnDefs,
                        scope: $scope
                    });
                }
            },
            {
                title: '恢复默认',
                icon: false,
                event: (event, obj) => {
                    $scope[$scope.type + 'BackState'] = JSON.parse(localStorage.getItem($scope.type + 'BackState'));
                    $scope.restoreState($scope.type + 'BackState');
                    localStorage.setItem($scope.type + 'State', JSON.stringify($scope[$scope.type + 'BackState']));
                    $scope.columnDefs = $scope[$scope.type + 'BackState'].columns;
                }
            }
        ];
        $scope.menuForced = {x: true, y: true};


        let columnDefs = gridColumnFactoryService.columnDefs;
        let templateDefine = gridColumnFactoryService.templateDefine;

        $scope.gridOptions = gridColumnFactoryService.buildGridOptions($scope, columnDefs, templateDefine);
        // $scope.gridOptions.columnDefs = $scope.gridOptions.columnDefs.findWhere(e => e.visible);
        // $scope.columnDefs = angular.copy($scope.gridOptions.columnDefs).findWhere(e => e.visible);

        this[initView]();
    };

    onClickAddPrice (event) {
        if (this.clickAddPrice) this.clickAddPrice({$event: event});
    };

    onClickImport (event) {
        this.gridExcelImportService.openImportDialog(undefined, {type: 'ssr'}, this.initData).then(e => {
            this.mdPanelRef = e;
        });
    }

    [initView] () {

        this.gridColumnFactoryService.resizeGrid();
        this.$scope.networkState = this.websocketService.networkState;
        this.$scope.isReconnect = this.websocketService.isReconnect;
        if (this.websocketService.hasBuilding) {
            this.websocketService.registListener('grid_ticket_hall', this.$scope, this.webSocketPush);
        }
    };

    $onInit () {
        let userInfo = this.userService.getUserInfo();
        if (userInfo && userInfo.user && userInfo.user.id) {
            this.$scope.type = userInfo.user.id + 'ssr';
        }
        
        if (userInfo && userInfo.info && userInfo.info['bab.quote.batchimport']) {
            this.importAuth = userInfo.info['bab.quote.batchimport'] === 'true';
        }
    }

    $onChanges (event) {
        // console.log(event);
        if (!event || !event.searchCriteria) return;
        if (event.searchCriteria) {
            let pre = event.searchCriteria.previousValue;
            let cur = event.searchCriteria.currentValue;
            if (!cur) return;
            // if (angular.equals(pre, cur)) return;

            let quoteQueryParam = {
                "billType": "BKB",
                "billMedium": "ELE",
                "pageSize": 50,
                "orderByPriceType": "last_update_datetime",
                "oderSeq": "DESC",
                "pageNumber": 0
            };
            let param = {};
            if (!this.websocketService.hasBuilding) {
                this.$scope.filterParam = quoteQueryParam;
                this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_search_SSR, quoteQueryParam);
                this.websocketService.buildWebsocket(this.configConsts);
                this.websocketService.registListener('grid_ticket_hall', this.$scope, this.webSocketPush);
                this.websocketService.hasBuilding = true;
            } else {
                for (var prop in cur) {
                    if (cur.hasOwnProperty(prop)) {
                        if (angular.isArray(cur[prop]) && cur[prop].length !== 0) {
                            param[prop] = cur[prop].map(e => e.code);
                        } else if (angular.isObject(cur[prop])) {
                            param[prop] = cur[prop].code;
                        }
                    }
                }
                //将billTypeMedium字段分为两个字段
                if (param.billTypeMedium) {
                    param.billType = param.billTypeMedium.split('_')[1];
                    param.billMedium = param.billTypeMedium.split('_')[0];
                    delete param.billTypeMedium;
                }
                //处理承兑行和称对方
                if (param.quotePriceType && angular.isArray(param.quotePriceType) && param.quotePriceType.length !== 0) {
                    param.quotePriceTypeList = param.quotePriceType;
                    delete param.quotePriceType;
                }
                if (param.acceptingHouse && angular.isArray(param.acceptingHouse) && param.acceptingHouse.length !== 0) {
                    param.companyTypes = param.acceptingHouse;
                    delete param.acceptingHouse;
                }
                //处理dueDateWrapperList
                if (param.dueDateWrapperList) {
                    if (param.dueDateWrapperList.length === 1 && param.dueDateWrapperList[0] === undefined) {
                        param.dueDateWrapperList = angular.copy(cur.dueDateWrapperList).map(e => this.dueDateFormat(e));
                        if (param.dueDateWrapperList.length === 1 && param.dueDateWrapperList[0] === undefined) {
                            param.dueDateWrapperList = undefined;
                        }
                    } else {
                        param.dueDateWrapperList = param.dueDateWrapperList.map(e => this.dueDateFormat(e));
                    }
                }
                //处理搜索项
                if (this.searchObj) {
                    if (this.searchObj.id) param.companyId = this.searchObj.id;
                    if (this.searchObj.memo) param.memo = this.searchObj.memo;
                }
                // console.log(param);
                quoteQueryParam = angular.merge(quoteQueryParam, param);
                this.$scope.filterParam = quoteQueryParam;

                if (quoteQueryParam.billType === 'CMB') {
                    this.$scope.gridOptions.columnDefs[6].visible = false;
                    this.$scope.gridOptions.columnDefs[7].visible = true;
                    this.$scope.gridOptions.columnDefs[8].visible = true;
                } else {
                    this.$scope.gridOptions.columnDefs[6].visible = true;
                    this.$scope.gridOptions.columnDefs[7].visible = false;
                    this.$scope.gridOptions.columnDefs[8].visible = false;
                }
                this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_search_SSR, quoteQueryParam);
                if (!(pre === undefined && cur)) {
                    //强制显示时间列的排序符号
                    this.$scope.gridApi.grid.resetColumnSorting();
                    jQuery('.last-update-time i').addClass('ui-grid-icon-down-dir').removeClass('ui-grid-icon-blank').removeClass('ui-grid-icon-up-dir');
                    jQuery('.last-update-time span.ui-grid-invisible').removeClass('ui-grid-invisible');
                }

            }
        }

        // var spin = new Spinner({color:'#fff',lines:12,top:'30%'});
        // var target = document.getElementById("grid");
        // spin.spin(target);
    }

    webSocketPush (scope, data) {
        let quote = JSON.parse(data);
        let item = JSON.parse(quote.object);
        let param = scope.filterParam;
        // console.log(item, scope);
        if (quote.source && quote.source !== 'BABQuote') return;
        //页面类型
        if (quote.quoteType && quote.quoteType !== "SSRQuoteModel") return;
        //票据类型
        if (item.billType && param.billType && item.billType !== param.billType) return;
        if (item.billMedium && param.billMedium && item.billMedium !== param.billMedium) return;
        //报价方向
        if (param.direction && item.direction && item.direction !== param.direction) return;
        //承兑行类别
        if (param.quotePriceTypeList && item.quotePriceType && param.quotePriceTypeList.indexOf(item.quotePriceType) === -1) return;
        //交易地点
        if (param.provinceCodes && item.quoteProvinces && item.quoteProvinces.provinceCode && param.provinceCodes.indexOf(item.quoteProvinces.provinceCode) === -1) return;
        //票面金额
        if (param.amountList && angular.isArray(param.amountList) && param.amountList.length !== 0) {
            let amountList = [];
            param.amountList.forEach(function (e) {
                scope.gridDataDefineService.amount.forEach(function (ee) {
                    if (ee.name === e) {
                        amountList.push(ee);
                    }
                });
            });
            let isReturn = true;
            amountList.forEach(function (e) {
                if (item.amount >= e.start && item.amount <= e.end) {
                    isReturn = false;
                }
            });
            if (isReturn) return;
        }
        //剩余期限
        if (param.dueDateWrapperList && angular.isArray(param.dueDateWrapperList)) {
            let isReturn = param.dueDateWrapperList.findItem(function (e) {
                return e.dueDateBegin <= item.dueDate && item.dueDate < e.dueDateEnd;
            });
            if (!isReturn) return;
        }
        //搜索
        if (param.memo && item.memo && item.memo.indexOf(param.memo) === -1) return;
        if (param.companyId && item.quoteCompanyDto && item.quoteCompanyDto.id && param.companyId !== item.quoteCompanyDto.id) return;
        if (param.companyId && item.companyId && param.companyId !== item.companyId) return;
        //业务类型，增删改
        if (!item.quoteStatus || item.quoteStatus === 'DFT') return;
        if (item.quoteStatus === 'DSB' || item.quoteStatus === 'DLD') {
            if (!angular.isArray(scope.gridOptions.data)) return;
            console.log(item);
            scope.gridOptions.data = scope.gridOptions.data.findWhere(function (e) {
                return e.id !== item.id;
            });
            item.isNew = true;
            scope.$timeout(() => {
                item.isNew = false;
                scope.commonService.safeApply(scope);
            }, 1000);
            scope.gridOptions.data.unshift(item);
            scope.commonService.safeApply(scope);
            if (scope.gridOptions.data.length > 50) scope.gridOptions.data.length = 50;
        } else if (item.quoteStatus === 'CAL' || item.quoteStatus === 'DEL') {
            scope.gridOptions.data = scope.gridOptions.data.findWhere(function (e) {
                return e.id !== item.id;
            });
            scope.commonService.safeApply(scope);
        }
    }

    onVmChanged () {
        this.$scope.$emit('gridTicketHallSsr', this.searchObj);
    }

    doSearch (content) {
        var def = this.$q.defer();
        if (!content) {
            def.reject();
            this.searchObj = undefined;
            this.$scope.$emit('gridTicketHallSsr', undefined);
            return def.promise;
        }

        this.babQuoteService.babSearchCompanies("SSR", content).then(res => {
            if (!res || !res.result) {
                def.reject();
            }
            else {
                def.resolve(res.result);
            }
        }, res => {
            def.reject();
        })
        return def.promise;

    }

    dueDateFormat (e) {
        let now = new Date();
        now.setHours(0);
        now.setMinutes(0);
        now.setSeconds(0);
        now.setMilliseconds(0);
        now = now.getTime();
        let begin, end;
        if (e.bRanged) {
            if (!e.start && !e.end) {
                return undefined;
            }

            if (e.dropSel.code === 'day') {
                if (e.start) {
                    begin = now + (+e.start-1) * 24 * 60 * 60 * 1000;
                }
                if (e.end) {
                    end = now + (+e.end) * 24 * 60 * 60 * 1000;
                }
            } else if (e.dropSel.code === 'mon') {
                if (e.start) {
                    begin = now + (+e.start) * 30 * 24 * 60 * 60 * 1000;
                }
                if (e.end) {
                    end = now + (+e.end) * 30 * 24 * 60 * 60 * 1000;
                }
            }
        } else {
            if (e === 'MONTH') {
                begin = now + 149 * 24 * 60 * 60 * 1000;
                end = now + 180 * 24 * 60 * 60 * 1000;
            } else if (e === 'LESS_MONTH') {
                begin = now;
                end = now + 149 * 24 * 60 * 60 * 1000;
            } else if (e === 'YEAR') {
                begin = now + 329 * 24 * 60 * 60 * 1000;
                end = now + 390 * 24 * 60 * 60 * 1000;
            } else if (e === 'LESS_YEAR') {
                begin = now;
                end = now + 329 * 24 * 60 * 60 * 1000;
            }
        }
        return {dueDateBegin: begin, dueDateEnd: end};
    }

    onClickGridRefresh () {
        this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_search_SSR, this.$scope.filterParam);
        this.websocketService.isReconnect = false;
        this.$scope.isReconnect = false;
    }

}

let gridTicketHallSsr = () => {
    return {
        template: require('./template/grid_ticket_hall_ssr.html'),
        bindings: {
            theme: '@mdTheme',
            isPanelOpen: '<',
            clickAddPrice: '&',
            searchCriteria: '<',
            isViewBusy: '<',
            initData: '<'
        },
        controller: ['$scope', 'httpService', 'uiGridConstants', 'gridDataDefineService', 'qbService', 'gridColumnFactoryService', '$interval', '$timeout', '$window', 'configConsts', 'websocketService', '$q', 'babQuoteService', 'accountAssociateService', 'commonService', 'gridExcelImportService', 'userService', gridTicketHallSSRCtrl]
    }
};

export default gridTicketHallSsr;
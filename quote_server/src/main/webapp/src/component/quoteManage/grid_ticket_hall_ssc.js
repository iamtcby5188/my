/**
 * Created by jiannan.niu on 2016/12/13.
 */

const initView = Symbol('initView');

class gridTicketHallSscCtrl {

    constructor ($scope, httpService, uiGridConstants, gridDataDefineService, qbService, gridColumnFactoryService, $interval, $timeout, $window, configConsts, gridColDefineService, websocketService, componentCommonService, commonService) {
        this.$scope = $scope;
        $scope.$timeout = $timeout;
        $scope.qbService = qbService;
        $scope.commonService = commonService;
        this.configConsts = configConsts;
        this.httpService = httpService;
        this.gridDataDefineService = gridDataDefineService;
        this.gridColumnFactoryService = gridColumnFactoryService;
        this.gridColDefineService = gridColDefineService;
        $scope.gridDataDefineService = gridDataDefineService;
        this.websocketService = websocketService;
        this.componentCommonService = componentCommonService;
        window.onresize = function () {
            gridColumnFactoryService.resizeGrid('panel-title-container');
        };

        $scope.sortPeriod = function (e) {
            $scope.quoteQueryParam.orderByPriceType = gridColDefineService.sortField[e.name];
            $scope.quoteQueryParam.oderSeq = e.sort.direction.toUpperCase();
            $scope.quoteQueryParam.pageNumber = 0;
            $scope.gridApi.core.scrollTo($scope.gridOptions.data[0]);
            gridColumnFactoryService.gridInit($scope, configConsts.bab_search_SSC, $scope.quoteQueryParam);
        };

        $scope.quoteQueryParam = {"billType": "BKB", "billMedium": "ELE", "pageSize": "50", "pageNumber": "0"};

        $scope.getDataDown = function () {
            $scope.quoteQueryParam.pageNumber++;
            gridColumnFactoryService.getDataDown($scope, configConsts.bab_search_SSC, $scope.quoteQueryParam);
        };

        $scope.saveState = (state) => {
            this.gridColumnFactoryService.saveState($scope, state);
        };

        $scope.restoreState = (state) => {
            this.gridColumnFactoryService.restoreState($scope, state);
        }

        $scope.gridOptions = gridColumnFactoryService.buildGridOptions($scope, gridColDefineService.getPriceGridCol("BKB"), gridColDefineService.templateDefine);

        this[initView]();
        this.bwsInit = false;
    };

    [initView] () {
        console.debug("gridTicketHallSscCtrl initView");
        this.gridColumnFactoryService.resizeGrid('panel-title-container');

        this.$scope.networkState = this.websocketService.networkState;
        this.$scope.isReconnect = this.websocketService.isReconnect;
        this.$scope.$watch('networkState', (newValue, oldValue) => {
            this.$scope.$emit('networkStateChange', newValue);
        });
        this.$scope.$watch('isReconnect', (newValue, oldValue) => {
            this.$scope.$emit('isReconnectChange', newValue);
        });
        this.$scope.$on('gridDataRefresh', (event) => {
            this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_search_SSC, this.$scope.quoteQueryParam);
        });
        if (this.websocketService.hasBuilding) {
            this.websocketService.registListener('grid_ticket_hall', this.$scope, this.webSocketPush);
        }
    };

    $onChanges (event) {
        if (!event || !event.searchCriteria) return;
        let pre = event.searchCriteria.previousValue;
        let cur = event.searchCriteria.currentValue;
        if (!cur || (cur && !cur.searchCriteria)) return;
        if (angular.equals(pre, cur)) return;
        // console.log(cur);

        this.$scope.quoteQueryParam = this.gridFormat(cur);

        this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_search_SSC, this.$scope.quoteQueryParam);
    }

    //格式化数据请求参数
    gridFormat (paramArray) {
        let param = {
            "billType": "BKB",
            "billMedium": "ELE",
            "pageSize": 50,
            "pageNumber": 0
        };
        
        if (!paramArray) return param;
        if(paramArray && paramArray.searchCriteria){
            for (var prop in paramArray.searchCriteria) {
                if (paramArray.searchCriteria.hasOwnProperty(prop)) {
                    if (angular.isArray(paramArray.searchCriteria[prop]) && paramArray.searchCriteria[prop].length !== 0) {
                        param[prop] = paramArray.searchCriteria[prop].map(e => e.code);
                    } else if (angular.isObject(paramArray.searchCriteria[prop])) {
                        param[prop] = paramArray.searchCriteria[prop].code;
                    }
                }
            }

            if (param.billTypeMedium &&　paramArray.searchCriteria.billTypeMedium) {
                var code = paramArray.searchCriteria.billTypeMedium.code;
                var type = this.gridDataDefineService.billType[code];
                param.billType = type.billType;
                param.billMedium = type.billMedium;
                param.minor = type.bMinor;
                this.$scope.gridOptions = this.gridColumnFactoryService.buildGridOptions(this.$scope, this.gridColDefineService.getPriceGridCol(param.billType), this.gridColDefineService.templateDefine);
                delete param.billTypeMedium;    
            }
        }
        
        if (paramArray.input) {
            if (paramArray.input.memo) {
                param.memo = paramArray.input.memo;
            }
            else if (paramArray.input.id) {
                param.companyId = paramArray.input.id;
            }
            else {
                param.companyId = undefined;
                param.memo = undefined;
            }
        }

        return param;
    }


    webSocketPush (scope, data) {
        // debugger;
        let quote = JSON.parse(data);
        let item = JSON.parse(quote.object);
        let param = scope.quoteQueryParam;
        // console.log(item, scope);
        if (quote.source !== 'BABQuote') return;
        //页面类型
        if (quote.quoteType !== "SSCQuoteModel") return;
        //票据类型
        if (item.billType !== param.billType) return;
        if (item.billType !== "CMB" && item.billMedium !== param.billMedium) return;

        //小
        if (param.billType === "BKB" && param.minor !== item.minor) return;

        //搜索
        if (param.memo && item.memo && item.memo.indexOf(param.memo) === -1) return;
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
            scope.$timeout(function () {
                item.isNew = false;
                scope.commonService.safeApply(scope);
            }, 1000);
            scope.gridOptions.data.unshift(item);
            if (scope.gridOptions.data.length > 50) scope.gridOptions.data.length = 50;
        } else if (item.quoteStatus === 'CAL' || item.quoteStatus === 'DEL') {
            scope.gridOptions.data = scope.gridOptions.data.findWhere(function (e) {
                return e.id !== item.id;
            });
        }
        scope.commonService.safeApply(scope);
    }
}

let gridTicketHallSsc = () => {
    return {
        template: require('./template/grid_ticket_hall_ssc.html'),
        bindings: {
            theme: '@',
            searchCriteria: '<'
        },
        controller: ['$scope', 'httpService', 'uiGridConstants', 'gridDataDefineService', 'qbService', 'gridColumnFactoryService', '$interval', '$timeout', '$window', 'configConsts', 'gridColDefineService', 'websocketService', 'componentCommonService', 'commonService', gridTicketHallSscCtrl]
    }
};

export default gridTicketHallSsc;
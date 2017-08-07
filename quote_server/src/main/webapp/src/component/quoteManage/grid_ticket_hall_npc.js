/**
 * Created by jiannan.niu on 2016/12/13.
 */

const initView = Symbol('initView');

class gridTicketHallNpcCtrl {

    constructor ($scope, httpService, uiGridConstants, gridDataDefineService, qbService, gridColumnFactoryService, $interval, $timeout, $window, configConsts, gridColDefineService, websocketService, commonService) {
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
        window.onresize = function () {
            gridColumnFactoryService.resizeGrid('panel-title-container');
        };

        $scope.sortPeriod = function (e) {
            $scope.quoteQueryParam.orderByPriceType = gridColDefineService.sortField[e.name];
            $scope.quoteQueryParam.oderSeq = e.sort.direction.toUpperCase();
            $scope.quoteQueryParam.pageNumber = 0;
            $scope.gridApi.core.scrollTo($scope.gridOptions.data[0]);
            gridColumnFactoryService.gridInit($scope, configConsts.bab_search_NPC, $scope.quoteQueryParam);
        };

        $scope.quoteQueryParam = {"billMedium": "PAP", tradeType: "BOT", "pageSize": "50", "pageNumber": "0"};

        $scope.getDataDown = function () {
            $scope.quoteQueryParam.pageNumber++;
            gridColumnFactoryService.getDataDown($scope, configConsts.bab_search_NPC, $scope.quoteQueryParam);
        };

        $scope.saveState = (state) => {
            this.gridColumnFactoryService.saveState($scope, state);
        };

        $scope.restoreState = (state) => {
            this.gridColumnFactoryService.restoreState($scope, state);
        }

        var col = gridColDefineService.getPriceGridCol("BKB");
        col = col.findWhere(e => e.template !== 'col_province');

        $scope.gridOptions = gridColumnFactoryService.buildGridOptions($scope, col, gridColDefineService.templateDefine);

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
            this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_search_NPC, this.$scope.quoteQueryParam);
        });
        if (this.websocketService.hasBuilding) {
            this.websocketService.registListener('grid_ticket_hall', this.$scope, this.webSocketPush);
        }
    };

    $onChanges (event) {
        if (!event || !event.searchCriteria) return;
        let pre = event.searchCriteria.previousValue;
        let cur = event.searchCriteria.currentValue;
        if (!cur) return;
        if (cur.length === 0) return;
        if (angular.equals(pre, cur)) return;
        this.$scope.quoteQueryParam = this.gridFormat(cur);
        this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_search_NPC, this.$scope.quoteQueryParam);
    }

    //格式化数据请求参数
    gridFormat (paramArray) {
        let param = {
            "billMedium": "PAP",
            "tradeType": "BOT",
            "pageSize": 50,
            "pageNumber": 0
        };
        // debugger;
        if (!paramArray) return param;
        if (paramArray.searchCriteria && paramArray.searchCriteria.billMedium) {
            param.billMedium = paramArray.searchCriteria.billMedium.code;
        }

        if (paramArray.dueType) {
            param.tradeType = paramArray.dueType.code;
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
    };

    webSocketPush (scope, data) {
        let quote = JSON.parse(data);
        let item = JSON.parse(quote.object);
        let param = scope.quoteQueryParam;
        // console.log(item, scope);
        if (quote.source !== 'BABQuote') return;
        //页面类型
        if (quote.quoteType !== "NPCQuoteModel") return;
        //票据类型
        if (item.billMedium !== param.billMedium) return;

        //小
        if (param.tradeType !== item.tradeType) return;

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

let gridTicketHallNpc = () => {
    return {
        template: require('./template/grid_ticket_hall_npc.html'),
        bindings: {
            theme: '@',
            searchCriteria: '<'
        },
        controller: ['$scope', 'httpService', 'uiGridConstants', 'gridDataDefineService', 'qbService', 'gridColumnFactoryService', '$interval', '$timeout', '$window', 'configConsts', 'gridColDefineService', 'websocketService', 'commonService', gridTicketHallNpcCtrl]
    }
};

export default gridTicketHallNpc;
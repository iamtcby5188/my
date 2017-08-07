/**
 * Created by jiannan.niu on 2016/12/30.
 */


const initView = Symbol('initView');
class gridManageSscTicketHallCtrl {
    constructor($scope, httpService, uiGridConstants, gridDataDefineService, qbService, gridColumnFactoryService, $interval, $timeout, $window, configConsts, gridColDefineService) {
        this.$scope = $scope;
        $scope.$timeout = $timeout;
        $scope.qbService = qbService;
        this.configConsts = configConsts;
        this.httpService = httpService;
        this.gridDataDefineService = gridDataDefineService;
        this.gridColumnFactoryService = gridColumnFactoryService;
        this.gridColDefineService = gridColDefineService;
        $scope.gridDataDefineService = gridDataDefineService;
        window.onresize = function () {
            gridColumnFactoryService.resizeGrid('panel-title-container');
        };

        $scope.saveState = (state) => {
            this.gridColumnFactoryService.saveState($scope, state);
        };

        $scope.restoreState = (state) => {
            this.gridColumnFactoryService.restoreState($scope, state);
        }

        $scope.quoteQueryParam = { "billType": "BKB", "billMedium": "ELE", "quoteStatusList": ["DFT", "DSB"] };

        $scope.gridOptions = gridColumnFactoryService.buildGridOptions($scope, gridColDefineService.getPriceMngGridCol("BKB"), gridColDefineService.templateDefine);
        $scope.gridOptions.enableSorting = false;
        $scope.getDataDown = function () {
            //不能删除此函数
        };
        $scope.$on('gridManageSscTicketHall',function(event,param){
            gridColumnFactoryService.gridInit($scope, configConsts.bab_mng_search_SSC, $scope.quoteQueryParam);
        });

        this[initView]();
    };

    onClickTable (event) {
        if (event && event.target) {
            var target = angular.element(event.target).scope();
            while (target && !target.hasOwnProperty('row')) target = target.$parent;

            if(target && target.row && target.row.entity){
                var att = event.target.getAttribute('tag');
                switch(att){
                    case 'edit':
                         if(this.editListItem){
                            var ret = this.editListItem({
                                $event:event,
                                item:target.row.entity,
                            }).then(res =>{
                                this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_mng_search_SSC, this.$scope.quoteQueryParam);
                            });
                        }
                        break;
                    case 'publish':
                         if(this.publishListItem){
                            var ret = this.publishListItem({
                                $event:event,
                                item:target.row.entity,
                            }).then(res =>{
                                    this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_mng_search_SSC, this.$scope.quoteQueryParam);
                            });
                            break;
                        }
                    case 'del':
                        if(this.delListItem){
                             var ret = this.delListItem({
                                $event:event,
                                item:target.row.entity,
                            }).then(res =>{
                                    this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_mng_search_SSC, this.$scope.quoteQueryParam);
                            });
                        }
                        break;
                    default:
                    break;
                }
            }
        }
    };

    [initView]() {
        console.debug("gridManageSscTicketHallCtrl initView");
        this.gridColumnFactoryService.resizeGrid('panel-title-container');
    }

    $onChanges(event) {
        if (!event || !event.searchCriteria) return;
        let pre = event.searchCriteria.previousValue;
        let cur = event.searchCriteria.currentValue;
        if (!cur || !angular.isArray(cur)) return;
        if (angular.equals(pre, cur)) return;
        this.$scope.quoteQueryParam = this.gridFormat(cur);

        this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_mng_search_SSC, this.$scope.quoteQueryParam);
    }

    gridFormat(paramArray) {
        let param = {
            "billType": "BKB",
            "billMedium": "PAP",
            "quoteStatusList": ["DFT", "DSB"],
        };

        if (angular.isArray(paramArray)) {
            paramArray.forEach(e => {
                switch (e.$parentType) {
                    case 'sscBillType':
                        var code = e.code;
                        var type = this.gridDataDefineService.billType[code];
                        param.billType = type.billType;
                        param.billMedium = type.billMedium;
                        param.minor = type.bMinor;
                        this.$scope.gridOptions = this.gridColumnFactoryService.buildGridOptionsNoScroll(this.$scope, this.gridColDefineService.getPriceMngGridCol(param.billType), this.gridColDefineService.templateDefine);
                        this.$scope.gridOptions.enableSorting = false;
                        break;
                    case 'sscDate':
                        param.createTimeBegin = e.start.getTime();
                        param.createTimeEnd = e.end.getTime();
                        break;
                    default:
                        break;

                }
            })
        }

        return param;
    }
}

let gridManageSscTicketHall = () => {
    return {
        template: require('./template/grid_manage_ssc_ticket_hall.html'),
        bindings: {
            theme: '@',
            searchCriteria: '<',

            editListItem: '&',
            publishListItem: '&',
            delListItem: '&',
        },
        controller: ['$scope', 'httpService', 'uiGridConstants', 'gridDataDefineService', 'qbService', 'gridColumnFactoryService', '$interval', '$timeout', '$window', 'configConsts', 'gridColDefineService', gridManageSscTicketHallCtrl]
    }
};

export default gridManageSscTicketHall;
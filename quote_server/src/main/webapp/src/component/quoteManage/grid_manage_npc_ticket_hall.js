/**
 * Created by jiannan.niu on 2016/12/30.
 */


const initView = Symbol('initView');
class gridManageNpcTicketHallCtrl {
    constructor($scope, httpService, uiGridConstants, gridDataDefineService, qbService, gridColumnFactoryService, $interval, $timeout, $window, configConsts,gridColDefineService) {
        this.$scope = $scope;
        $scope.$timeout = $timeout;
        $scope.qbService = qbService;
        this.configConsts = configConsts;
        this.httpService = httpService;
        this.gridDataDefineService = gridDataDefineService;
        $scope.gridDataDefineService = gridDataDefineService;
        this.gridColDefineService = gridColDefineService;
        $scope.gridColDefineService = gridColDefineService;
        this.gridColumnFactoryService = gridColumnFactoryService;
        window.onresize = function () {
            gridColumnFactoryService.resizeGrid('panel-title-container');
        };

        $scope.getDataDown = function () {

        }

        $scope.saveState = (state) => {
            this.gridColumnFactoryService.saveState($scope, state);
        };

        $scope.restoreState = (state) => {
            this.gridColumnFactoryService.restoreState($scope, state);
        }

        $scope.gridOptions = gridColumnFactoryService.buildGridOptionsNoScroll($scope, gridColDefineService.getNpcMngGridCol(), gridColDefineService.templateDefine);
        $scope.gridOptions.enableSorting = false;
        $scope.$on('gridManageNpcTicketHall',function(event,param){
            gridColumnFactoryService.gridInit($scope, configConsts.bab_mng_search_NPC, $scope.quoteQueryParam);
        });

        this[initView]();
    };

    [initView] () {
        this.gridColumnFactoryService.resizeGrid('panel-title-container');
        console.debug("gridManageNpcTicketHallCtrl initView");
    }

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
                                this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_mng_search_NPC, this.$scope.quoteQueryParam);
                            });
                        }
                        break;
                    case 'publish':
                         if(this.publishListItem){
                            var ret = this.publishListItem({
                                $event:event,
                                item:target.row.entity,
                            }).then(res =>{
                                    this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_mng_search_NPC, this.$scope.quoteQueryParam);
                            });
                        }
                        break;
                    case 'del':
                        if(this.delListItem){
                             var ret = this.delListItem({
                                $event:event,
                                item:target.row.entity,
                            }).then(res =>{
                                    this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_mng_search_NPC, this.$scope.quoteQueryParam);
                            });
                        }
                        break;
                    default:
                    break;
                }
            }
        }
    };

    $onChanges (event) {
        if (!event || !event.searchCriteria) return;
        let pre = event.searchCriteria.previousValue;
        let cur = event.searchCriteria.currentValue;
        console.debug(event.searchCriteria);
        if (!cur || !angular.isArray(cur)) return;
        if (angular.equals(pre, cur)) return;

        this.$scope.quoteQueryParam = this.gridFormat(cur);

        this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_mng_search_NPC, this.$scope.quoteQueryParam);
    }

    gridFormat (paramArray) {
        let param = {
            "billType": "BKB",
            "billMedium": "PAP",
            "quoteStatusList":["DFT","DSB"]
        };

        if(angular.isArray(paramArray)){
            paramArray.forEach(e=>{
                switch(e.$parentType){
                    case 'npcBillMedium':
                        param.billMedium = e.code;
                        break;
                    case 'npcTradeModel':
                        param.tradeType = e.code;
                        break;
                    case 'npcDateRange':
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

let gridManageNpcTicketHall = () => {
    return {
        template: require('./template/grid_manage_ssr_ticket_hall.html'),
        bindings: {
            theme: '@',
            searchCriteria: '<',

            editListItem: '&',
            publishListItem: '&',
            delListItem: '&',
        },

        controller: ['$scope', 'httpService', 'uiGridConstants', 'gridDataDefineService', 'qbService', 'gridColumnFactoryService', '$interval', '$timeout', '$window', 'configConsts','gridColDefineService', gridManageNpcTicketHallCtrl]
    }
};

export default gridManageNpcTicketHall;
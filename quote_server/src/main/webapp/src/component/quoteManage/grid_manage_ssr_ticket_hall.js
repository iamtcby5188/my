/**
 * Created by jiannan.niu on 2016/12/30.
 */


const initView = Symbol('initView');
class gridManageSsrTicketHallCtrl {
    constructor ($scope, gridColumnFactoryService, gridDataDefineService, uiGridConstants, configConsts) {
        this.$scope = $scope;
        this.configConsts = configConsts;
        this.uiGridConstants = uiGridConstants;
        this.gridDataDefineService = gridDataDefineService;
        $scope.gridDataDefineService = gridDataDefineService;
        this.gridColumnFactoryService = gridColumnFactoryService;

        $scope.showBill =gridColumnFactoryService.showBill;
        $scope.gridColumnFactoryService = gridColumnFactoryService;

        let columnDefs = gridColumnFactoryService.columnDefs;
        let templateDefine = gridColumnFactoryService.templateDefine;

        $scope.getLastUpdateTime = gridColumnFactoryService.getLastUpdateTime;
        $scope.getRemainingDays = gridColumnFactoryService.getRemainingDays;

        $scope.saveState = (state) => {
            this.gridColumnFactoryService.saveState($scope, state);
        };

        $scope.restoreState = (state) => {
            this.gridColumnFactoryService.restoreState($scope, state);
        }

        $scope.gridOptions = gridColumnFactoryService.buildGridOptionsNoScroll($scope, columnDefs, templateDefine);
        $scope.gridOptions.enableSorting = false;
        $scope.gridOptions.columnDefs[0].visible = false;
        $scope.gridOptions.columnDefs[1].visible = false;
        $scope.gridOptions.columnDefs[2].visible = true;
        // $scope.gridOptions.columnDefs[6].visible = false;
        // $scope.gridOptions.columnDefs[7].visible = true;
        // $scope.gridOptions.columnDefs[8].visible = true;
        $scope.gridOptions.columnDefs[18].visible = true;

        $scope.$on('gridManageSsrTicketHallCtrl',function(event,param){
            gridColumnFactoryService.gridInit($scope, configConsts.bab_mng_search_SSR, $scope.filterParam);
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
                                this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_mng_search_SSR, this.$scope.filterParam);
                            });
                        }
                        break;
                    case 'publish':
                         if(this.publishListItem){
                            var ret = this.publishListItem({
                                $event:event,
                                item:target.row.entity,
                            }).then(res =>{
                                    this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_mng_search_SSR, this.$scope.filterParam);
                            });
                        }
                        break;
                    case 'del':
                        if(this.delListItem){
                             var ret = this.delListItem({
                                $event:event,
                                item:target.row.entity,
                            }).then(res =>{
                                    this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_mng_search_SSR, this.$scope.filterParam);
                            });
                        }
                        break;
                    default:
                    break;
                }
            }
        }
    };

    [initView] () {
        this.gridColumnFactoryService.resizeGrid();
        // console.debug(this.searchCriteria);
    }

    $onChanges (event) {
        if (!event || !event.searchCriteria) return;
        let pre = event.searchCriteria.previousValue;
        let cur = event.searchCriteria.currentValue;
        if (!cur.searchCriteria && !cur.searchPanelModel) return;
        if (angular.equals(pre, cur)) return;
        let quoteQueryParam = this.gridFormat(cur);
        this.$scope.filterParam = quoteQueryParam;
        if (pre && cur) {
            console.debug(cur);
            let columnDefs = this.gridColumnFactoryService.columnDefs;
            let templateDefine = this.gridColumnFactoryService.templateDefine;
            this.$scope.gridOptions = this.gridColumnFactoryService.buildGridOptionsNoScroll(this.$scope, columnDefs, templateDefine);
            this.$scope.gridOptions.enableSorting = false;
            this.$scope.gridOptions.columnDefs[0].visible = false;
            this.$scope.gridOptions.columnDefs[1].visible = false;
            this.$scope.gridOptions.columnDefs[2].visible = true;
            if (quoteQueryParam.billType === 'CMB') {
                this.$scope.gridOptions.columnDefs[6].visible = false;
                this.$scope.gridOptions.columnDefs[7].visible = true;
                this.$scope.gridOptions.columnDefs[8].visible = true;
            } else {
                this.$scope.gridOptions.columnDefs[6].visible = true;
                this.$scope.gridOptions.columnDefs[7].visible = false;
                this.$scope.gridOptions.columnDefs[8].visible = false;
            }
            if (quoteQueryParam.quoteStatusList[0] === "DFT" && quoteQueryParam.quoteStatusList[1] === "DSB") {
                this.$scope.gridOptions.columnDefs[18].visible = true;
                this.$scope.gridOptions.columnDefs[19].visible = false;
                this.$scope.gridOptions.columnDefs[20].visible = false;
            } else if (quoteQueryParam.quoteStatusList[0] === "DLD") {
                this.$scope.gridOptions.columnDefs[18].visible = false;
                this.$scope.gridOptions.columnDefs[19].visible = true;
                this.$scope.gridOptions.columnDefs[20].visible = false;
            } else if (quoteQueryParam.quoteStatusList[0] === "CAL") {
                this.$scope.gridOptions.columnDefs[18].visible = false;
                this.$scope.gridOptions.columnDefs[19].visible = false;
                this.$scope.gridOptions.columnDefs[20].visible = true;
            }
        }

        console.log(quoteQueryParam);
        this.gridColumnFactoryService.gridInit(this.$scope, this.configConsts.bab_mng_search_SSR, quoteQueryParam);
    }


    gridFormat (paramArray) {
        let param = {
            "billType": "BKB",
            "billMedium": "ELE",
            "quoteStatusList": ["DFT"],
        };
        if (!paramArray  || !angular.isArray(paramArray.searchPanelModel)) return param;

        let tempParam = {};
        for (var prop in paramArray.searchCriteria) {
            if (paramArray.searchCriteria.hasOwnProperty(prop)) {
                if (angular.isArray(paramArray.searchCriteria[prop]) && paramArray.searchCriteria[prop].length !== 0) {
                    tempParam[prop] = paramArray.searchCriteria[prop].map(e => e.code);
                } else if (angular.isObject(paramArray.searchCriteria[prop])) {
                    tempParam[prop] = paramArray.searchCriteria[prop].code;
                }
            }
        }

        if (tempParam.billTypeMedium) {
            tempParam.billType = tempParam.billTypeMedium.split('_')[1];
            tempParam.billMedium = tempParam.billTypeMedium.split('_')[0];
            delete tempParam.billTypeMedium;
        }
        param = angular.merge(param,tempParam);
        paramArray.searchPanelModel.forEach(function (item) {
            if (item.$parentType === 'ssrStatus') {
                if (item.code) {
                    if (item.code === "DFT") {
                        param.quoteStatusList = ["DFT", "DSB"];
                    }
                    else {
                        param.quoteStatusList[0] = item.code;
                    }
                }
            } else if (item.$parentType === 'ssrDateRange') {

                param.lastUpdateTimeBegin = item.start.getTime();
                param.lastUpdateTimeEnd = item.end.getTime();
            } else if (item.$parentType === 'ssrInputSearch') {
                if (item.memo) {
                    param.memo = item.memo;
                }  
                else {
                    param.memo = undefined;
                }
            }
        });

        if (param.quoteStatusList[0] !== 'DLD') {

            if (param.lastUpdateTimeBegin) param.lastUpdateTimeBegin = undefined;
            if (param.lastUpdateTimeEnd) param.lastUpdateTimeEnd = undefined;
        }
        return param;
    }
}

let gridManageSsrTicketHall = () => {
    return {
        template: require('./template/grid_manage_ssr_ticket_hall.html'),
        bindings: {
            theme: '@',
            searchCriteria: '<',

            editListItem: '&',
            publishListItem: '&',
            delListItem: '&',     
        },
        controller: ['$scope', 'gridColumnFactoryService', 'gridDataDefineService', 'uiGridConstants', 'configConsts', gridManageSsrTicketHallCtrl]
    }
};

export default gridManageSsrTicketHall;
const initView = Symbol('initView');

class gridPricemangeBatchimportCtrl {

    constructor ($scope, httpService,uiGridConstants, gridDataDefineService,gridColDefineService) {

        this.gridColDefineService = gridColDefineService;
        $scope.onClick = (quot,evt) =>{
            var scope = angular.element(evt.target).scope();
            if(scope.col.field === "操作"){
                this.onDelItem({"id":quot.ID});
            }
            else{
                this.onRowChanged({"quot":quot});
            }
        };
             
        $scope.onDblClick = (quot) =>{
            this.onDblClick({"quot":quot});
        };

        this.$scope = $scope;
        this.$scope.data = this.quotData;
        this[initView]();
        this.gridOptions = {
            rowHeight: 30,
            enableColumnMenus: false,
            enableColumnResizing: true,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.ALWAYS,
            enableSorting:false,
            enableRowSelection:true,
            enableFullRowSelection : true,
            enableRowHeaderSelection: false,
            multiSelect:false,
            data: $scope.data,
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;
            },
            rowTemplate: '<div ng-click=\"grid.appScope.onClick(row.entity,$event)\"  ng-dblclick=\"grid.appScope.onDblClick(row.entity)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ \'ui-grid-row-header-cell\': col.isRowHeader }\" ui-grid-cell dbl-click-row></div>',
            columnDefs:$scope.colDefs,
        };
    };


    [initView] () {
        //  this.$scope.colDefs = this.gridColDefineService.getPriceManageGridCol(this.tickType);
    };
}

let gridPricemangeBatchimport = () => {
    return {
        template: require('./template/grid_pricemanage_batchimport.html'),
        bindings: {
            tickType: '@',
            quotData: '<',
            onRowChanged: '&',
            onDblClick:'&',
            onDelItem:'&',
        },
        controller: ['$scope', 'httpService', 'uiGridConstants', 'gridDataDefineService','gridColDefineService',gridPricemangeBatchimportCtrl]
    }
};

export default gridPricemangeBatchimport;
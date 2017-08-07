const initView = Symbol('initView');
class gridSscPriceCtrl {

    constructor($scope,gridPriceMarginService,gridDataDefineService) {
        this.$scope = $scope;
        this.gridPriceMarginService = gridPriceMarginService;
        this.$scope.gridDataDefineService = gridDataDefineService;
        
        this[initView]();
    };

    [initView]() {
        console.debug('gridSscPriceCtrl initView');
        this.$scope.gridOptions = this.gridPriceMarginService.getPriceGridOption();
    };

    $onChanges(evt){
        if(evt && evt.itemSource && evt.itemSource.currentValue){
            this.$scope.gridOptions.data = evt.itemSource.currentValue;
        }
    }
};

let gridSscPrice = () => {
    return {
        template: require('./template/grid_ssc_price.html'),
        bindings: {
            theme: '@',
            title: '@',
            itemSource:'<',
        },
        controller: ['$scope','gridPriceMarginService','gridDataDefineService', gridSscPriceCtrl]
    }
};

export default gridSscPrice;
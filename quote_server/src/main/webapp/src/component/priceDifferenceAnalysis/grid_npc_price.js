const initView = Symbol('initView');
class gridNpcPriceCtrl {

    constructor($scope,gridPriceMarginService,gridDataDefineService) {
        this.$scope = $scope;
        this.gridPriceMarginService = gridPriceMarginService;
        this.$scope.gridDataDefineService = gridDataDefineService;
        
        this[initView]();
    };

    [initView]() {
        console.debug('gridNpcPriceCtrl initView');

        this.$scope.gridOptions = this.gridPriceMarginService.getPriceGridOption();
    };

    $onChanges(evt){
        if(evt && evt.itemSource && evt.itemSource.currentValue){
            this.$scope.gridOptions.data = evt.itemSource.currentValue;
        }
    }
};

let gridNpcPrice = () => {
    return {
        template: require('./template/grid_npc_price.html'),
        bindings: {
            theme: '@',
            title: '@',
            itemSource:'<',
        },
        controller: ['$scope','gridPriceMarginService','gridDataDefineService', gridNpcPriceCtrl]
    }
};

export default gridNpcPrice;
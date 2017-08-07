const initView = Symbol('initView');
class gridShiborPriceCtrl {

    constructor($scope,gridPriceMarginService,gridDataDefineService) {
        this.$scope = $scope;
        this.gridPriceMarginService = gridPriceMarginService;
        this.$scope.gridDataDefineService = gridDataDefineService;
        
        this[initView]();
    };

    [initView]() {
        console.debug('gridShiborPriceCtrl initView');

        this.$scope.gridOptions = this.gridPriceMarginService.getShiborGridOption();
    };

    $onChanges(evt){
        if(evt && evt.itemSource && evt.itemSource.currentValue){
            this.$scope.gridOptions.data = this.itemSource;
        }
    }
};

let gridShiborPrice = () => {
    return {
        template: require('./template/grid_shibor_price.html'),
        bindings: {
            theme: '@',
            title: '@',
            itemSource:'<',
        },
        controller: ['$scope','gridPriceMarginService','gridDataDefineService', gridShiborPriceCtrl]
    }
};

export default gridShiborPrice;
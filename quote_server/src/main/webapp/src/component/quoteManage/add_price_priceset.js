class addPricePricesetCtrl {

    constructor($scope) {
        this.$scope = $scope;

        this.initView();
    };

    initView() {
        console.debug('addPricePricesetCtrl initView');
    };
};

let addPricePriceset = () => {
    return {
        template: require('./template/add_price_priceset.html'),
        bindings: {
            theme: '@',
        },
        controller: ['$scope', addPricePricesetCtrl]
    }
};

export default addPricePriceset;
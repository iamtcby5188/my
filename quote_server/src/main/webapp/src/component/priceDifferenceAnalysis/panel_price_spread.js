/**
 * Created by jiannan.niu on 2017/2/7.
 */
const initView = Symbol('initView');
const THEME_NAME = "ssAvalonUi";

class panelPriceSpreadCtrl {

    constructor ($scope, gridDataDefineService) {
        this.$scope = $scope;
        this.gridDataDefineService = gridDataDefineService;
        this[initView]();
    };

    [initView] () {
        console.debug('panelPriceSpreadCtrl initView');
        this.theme = THEME_NAME;

    };
}

let panelPriceSpread = () => {
    return {
        template: require('./template/panel_price_spread.html'),
        bindings: {
            theme: '@mdTheme',
            priceMargin: '<'
        },
        controller: ['$scope', 'gridDataDefineService', panelPriceSpreadCtrl]
    }
};

export default panelPriceSpread;

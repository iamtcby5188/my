const initView = Symbol('initView');

class panelSearchPricemngCtrl {

    constructor($scope, commonService) {
        this.$scope = $scope;
        this[initView]();
    };


    [initView]() {
        console.debug('panelSearchPricemngCtrl initView');
    };

    onVmChanged() {
        this.ngModelCtrl.$viewValue = this.itemSource.findWhere(e => e.attribute && e.attribute.ngModel).map(function (item, index) { item.attribute.ngModel.$parentType = item.typeName; return item.attribute.ngModel; });
        this.ngModelCtrl.$commitViewValue();
    };

    onTabChanged(tabCode) {
        if (this.tabChanged) this.tabChanged({ tabCode: tabCode });
    };

    onClickButton(event) {
        if (!event || !event.target) return;
        var button = angular.element(event.target);
        if (event.target.nodeName !== 'BUTTON') button = button.parent();
        if (!button) return;
        if (button.attr('aria-label') === 'mdButton') {
            var item = button.scope().item;
            this.btnClick({
                $event: event,
                dataDefine: item
            });
        }
    };

    doSearch(content) {
        if (this.inputSearch)
            return this.inputSearch({ "content": content });

        return [];
    };

    doSearchAsyn(content){
        if(this.inputSearchAsyn)
            return this.inputSearchAsyn({ "content": content });
    };
};

let panelSearchPricemng = () => {
    return {
        template: require('./template/panel_search_pricemng.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            theme: '@mdTheme',
            itemSource: '<',
            ngModel: '<',

            btnClick: '&',
            tabChanged: '&',

            inputSearch: '&',
            inputSearchAsyn:'&',
        },
        controller: ['$scope', 'commonService', panelSearchPricemngCtrl]
    }
};

export default panelSearchPricemng;
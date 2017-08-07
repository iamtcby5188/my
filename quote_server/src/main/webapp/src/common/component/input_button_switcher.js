const initView = Symbol('initView');

class inputButtonSwitcherCtrl {

    constructor($scope, componentCommonService) {
        this.$scope = $scope;

        this.isPanelOpen = true || this.isPanelOpen;

        this.componentCommonService = componentCommonService;

        this[initView]();
    };

    onClickItemButton(event) {
        if (!event || !event.target) return;

        var button = angular.element(event.target);

        if (!button) return;

        var item = button.scope().item;
        if (!item) return;

        this.itemSource.forEach((item, index) => {
            item.isChecked = false;
        });

        item.isChecked = true;

        // https://docs.angularjs.org/api/ng/type/ngModel.NgModelController
        this.ngModelCtrl.$viewValue = this.itemSource.findItem(e => e.isChecked);
        this.ngModelCtrl.$commitViewValue();

        if (this.vmChanged) this.vmChanged();
    };

    [initView]() {
        console.debug('inputButtonSwitcherCtrl initView');
    };

    // https://docs.angularjs.org/guide/component
    $onChanges(event) {
        if (!event) return;

        if (event.itemSource && event.itemSource.currentValue) {
            this.componentCommonService.setDisplayNameForItems(event.itemSource.currentValue, this.displayPath ? this.displayPath : undefined);
            if (this.vmChanged) this.vmChanged();
        }

        if (event.ngModel) {
            if (event.ngModel.currentValue) {
                if (this.itemSource instanceof Array) {

                    this.itemSource.forEach(e => {
                        if (e.$id === undefined || event.ngModel.currentValue.$id === undefined) {
                            console.error(`inputButtonSwitcherCtrl: $id property is required for item in itemSource`);
                        } else {
                            e.isChecked = e.$id === event.ngModel.currentValue.$id;
                        }
                    });
                }
            } else {
                if (this.itemSource instanceof Array) {
                    this.itemSource.forEach(e => { e.isChecked = e.isDefault; });
                    this.ngModelCtrl.$viewValue = this.itemSource.findItem(e => e.isChecked);
                    this.ngModelCtrl.$commitViewValue();
                    if (this.vmChanged) this.vmChanged();
                }
            }
        }
    };
};

let inputButtonSwitcher = () => {
    return {
        template: require('./template/input_button_switcher.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            theme: '@mdTheme',

            vmChanged: '&',
            labelFlex: '<',
            inputFlex: '<',
            itemSource: '<',
            label: '@',
            displayPath: '@',
            ngModel: '<'
        },
        controller: ['$scope', 'componentCommonService', inputButtonSwitcherCtrl]
    }
};

export default inputButtonSwitcher;
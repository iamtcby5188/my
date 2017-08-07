const initView = Symbol('initView');

class inputButtonSelectorCtrl {

    constructor($scope, componentCommonService) {
        this.$scope = $scope;

        this.componentCommonService = componentCommonService;

        this[initView]();
    };

    onClickItemButton(event) {
        if (!event || !event.target || !this.itemSource) return;

        var button = angular.element(event.target);

        if (event.target.nodeName !== 'BUTTON') button = button.parent();

        if (!button) return;

        if (button.attr('aria-label') === 'selectAll') {

            this.isSelectAll = true;
            if (this.itemSource instanceof Array) this.itemSource.forEach(e => { e.isChecked = false });
            this.itemSource.forEach(e => {
                if (e.hasOwnProperty('isChecked')) delete e['isChecked'];
                return e
            });

            this.ngModelCtrl.$viewValue = undefined;

        } else {

            var item = button.scope().item;
            if (!item) return;

            this.isSelectAll = false;
            item.isChecked = !item.isChecked;

            // https://docs.angularjs.org/api/ng/type/ngModel.NgModelController
            if (this.itemSource instanceof Array) {
                this.ngModelCtrl.$viewValue = this.itemSource.findWhere(e => e.isChecked);

                if (this.vmType === "Object") {
                    // this.ngModelCtrl.$viewValue = this.ngModelCtrl.$viewValue[0];
                    if (this.ngModelCtrl.$viewValue instanceof Array)
                        this.ngModelCtrl.$viewValue = this.ngModelCtrl.$viewValue.findItem(e => e.$id === item.$id);
                    this.itemSource.forEach(e => { e.isChecked = e.$id === item.$id; });
                }
            }
        }

        this.ngModelCtrl.$commitViewValue();
        if (this.vmChanged) this.vmChanged();
    };

    [initView]() {
        console.debug('inputButtonSelectorCtrl initView.');
    };

    // https://docs.angularjs.org/guide/component
    $onChanges(event) {
        if (!event) return;
        if (event.itemSource && event.itemSource.currentValue) {
            this.componentCommonService.setDisplayNameForItems(this.itemSource, this.displayPath ? this.displayPath : undefined);
            // if (this.vmChanged) this.vmChanged();
        }

        if (event.hasSelectAllButton && event.hasSelectAllButton.currentValue === 'true') {
            this.isSelectAll = true;
        }


        if (event.ngModel && event.ngModel.currentValue) {

            if (this.hasSelectAllButton === 'true') {
                this.isSelectAll = false;
            }

            if (this.ngModel === 'none') {
                if (this.itemSource instanceof Array)
                    this.itemSource.forEach(e => {
                         e.isChecked = false;
                });
                this.ngModelCtrl.$viewValue = this.ngModel;
                this.ngModelCtrl.$commitViewValue();
                if (this.vmChanged) this.vmChanged();
                return;
            }
            else if(this.ngModel === 'all'){
                if (this.hasSelectAllButton === 'true') {
                        this.isSelectAll = true;
                        if (this.itemSource instanceof Array) this.itemSource.forEach(e => { e.isChecked = false; });
                        this.ngModelCtrl.$viewValue = undefined;
                        this.ngModelCtrl.$commitViewValue();
                        if (this.vmChanged) this.vmChanged();
                    }

                return;
            }

            if (this.itemSource instanceof Array) this.itemSource.forEach(e => { e.isChecked = false });

            var setButtonChecked = (item, index) => {
                if (this.itemSource instanceof Array)
                    this.itemSource.forEach(e => {
                        if (e.$id === undefined) {
                            console.error(`inputButtonSelectorCtrl: $id property is required for item in itemSource`);
                        } else if (item.$id === undefined) {
                            console.error(`inputButtonSelectorCtrl: $id property is required for ngModel`);
                        } else if (e.$id === item.$id) e.isChecked = true;
                    })
            };

            if (event.ngModel.currentValue instanceof Array) {
                if (this.ngModel.length > 0) {
                    this.ngModel.forEach(setButtonChecked);
                } else {
                    if (this.hasSelectAllButton === 'true') {
                        this.isSelectAll = true;
                        if (this.itemSource instanceof Array) this.itemSource.forEach(e => { e.isChecked = false; });
                        this.ngModelCtrl.$viewValue = undefined;
                    }
                }
            } else {
                setButtonChecked(angular.copy(this.ngModel));
            }

            if (!this.ngModelCtrl.$viewValue && this.ngModel) {
                this.ngModelCtrl.$viewValue = this.ngModel;
                this.ngModelCtrl.$commitViewValue();
                if (this.vmChanged) this.vmChanged();
            }

        } else {

            if (this.hasSelectAllButton === 'true') {
                this.isSelectAll = true;
                if (this.itemSource instanceof Array) this.itemSource.forEach(e => { e.isChecked = false; });
                this.ngModelCtrl.$viewValue = undefined;
            } else {

                if (this.itemSource instanceof Array) {
                    this.itemSource.forEach(e => { e.isChecked = e.isDefault; });
                    this.ngModelCtrl.$viewValue = this.itemSource.findWhere(e => e.isChecked);
                }

                if (this.vmType === "Object") {
                    if (this.ngModelCtrl.$viewValue instanceof Array) {
                        this.ngModelCtrl.$viewValue = this.ngModelCtrl.$viewValue[0];
                        this.ngModelCtrl.$commitViewValue();
                        if (this.vmChanged) this.vmChanged();
                    }
                    return;
                }
            }

            this.ngModelCtrl.$commitViewValue();
            if (this.vmChanged) this.vmChanged();
        }

    };
};

let inputButtonSelector = () => {
    return {
        template: require('./template/input_button_selector.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            theme: '@mdTheme',

            itemSource: '<',
            label: '@',
            displayPath: '@',
            labelFlex: '<',
            inputFlex: '<',
            hasSelectAllButton: '@',
            ngModel: '<',
            vmType: '@',

            vmChanged: '&',
        },
        controller: ['$scope', 'componentCommonService', inputButtonSelectorCtrl]
    }
};

export default inputButtonSelector;
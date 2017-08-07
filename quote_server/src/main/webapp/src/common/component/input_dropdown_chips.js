const initView = Symbol('initView');

class inputDropdownChipsCtrl {

    constructor($scope, componentCommonService) {
        this.$scope = $scope;

        this.componentCommonService = componentCommonService;

        this[initView]();
    };

    querySearch(query) {
        if (!query || query.length === 0) return this.itemSource;

        if (!this.itemSource || !(this.itemSource instanceof Array)) return [];        

        return this.itemSource.findWhere(item => {

            if (item.pinyin && item.pinyin.indexOf(query) > -1) return true;
            if (item.pinyinFull && item.pinyinFull.indexOf(query) > -1) return true;
            if (item.name && item.name.indexOf(query) > -1) return true;

            return false;
        });
    };

    [initView]() {
        console.debug('inputDropdownChipsCtrl initView');

        this.selectedItems = [];

        this.vm = {
            _selectedItems: [],
            _selectedItemsCount: 0
        };

        // this.ngModel.$viewValue = newValue;
        // this.ngModel.$commitViewValue();

        var vm = this.vm,
            getNgModelCtrl = () => this.ngModelCtrl,
            vmChangedEvent = this.vmChanged;

        this.vm = {
            selectedItems: function (newValue) {
                if (newValue && !vm._selectedItems) {
                    console.debug("inputDropdownChipsCtrl vm changed.")
                } else if (!newValue && vm._selectedItems) {
                    if (vm._selectedItemsCount !== vm._selectedItems.length) {
                        console.debug("inputDropdownChipsCtrl vm changed.")
                        vm._selectedItemsCount = vm._selectedItems.length

                        var ngModelCtrl = getNgModelCtrl();

                        ngModelCtrl.$viewValue = vm._selectedItems;
                        ngModelCtrl.$commitViewValue();
                        if (vmChangedEvent) vmChangedEvent();
                    }
                } else if (newValue && vm._selectedItems) {
                    if (newValue.length !== vm._selectedItems.length) {
                        console.debug("inputDropdownChipsCtrl vm changed.")
                    }
                }
                return arguments.length ? (vm._selectedItems = newValue) : vm._selectedItems;
            }
        };
    };

    $onChanges(event) {
        if (!event) return;

        if (event.ngModel) {

            if (event.ngModel.currentValue) {

                var selected = [];

                var setButtonChecked = (item, index) => {
                    if (this.itemSource instanceof Array)
                        this.itemSource.forEach(e => {
                            if (e.$id === undefined || item.$id === undefined) {
                                console.error(`inputDropdownChipsCtrl: $id property is required for item in itemSource.`);
                            } else if (e.$id === item.$id) {
                                selected.push(e);
                            }
                        })
                };

                if (event.ngModel.currentValue instanceof Array) {
                    event.ngModel.currentValue.forEach(setButtonChecked);
                }

                if (selected.length > 0) {
                    this.vm.selectedItems(selected);
                }

            } else {
                this.vm.selectedItems([]);
            }
        }

        if (event.itemSource && event.itemSource.currentValue) {
            this.componentCommonService.setDisplayNameForItems(this.itemSource);
            // if (this.vmChanged) this.vmChanged();
        }
    };
};

let inputDropdownChips = () => {
    return {
        template: require('./template/input_dropdown_chips.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            theme: '@',
            label: '@',
            labelFlex: '<',
            inputFlex: '<',
            itemSource: '<',
            ngModel: '<',

            vmChanged: '&',
        },
        controller: ['$scope', 'componentCommonService', inputDropdownChipsCtrl]
    }
};

export default inputDropdownChips;
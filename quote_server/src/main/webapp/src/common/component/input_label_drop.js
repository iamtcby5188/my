const initView = Symbol('initView');

class inputLabelDropCtrl {

    constructor($scope, componentCommonService) {
        this.$scope = $scope;
        this.componentCommonService = componentCommonService;
        this[initView]();
    };

    [initView]() {
        console.debug('inputLabelDropCtrl initView');
    };

    onItemChange() {

        this.ngModelCtrl.$viewValue = {
            inputString: this.inputString,
            selectedItem: this.selectedItem,
            value: +(this.selectedItem && this.selectedItem.procFunc ? this.selectedItem.procFunc(this.inputString) : this.inputString)
        };

        this.ngModelCtrl.$commitViewValue();

        if (this.vmChanged) this.vmChanged();
    }

    onDropChange() {
        this.onItemChange();
    }

    onInputChange() {
        this.onItemChange();
    }

    $onChanges(event) {
        if (!event) return;

        if (event.itemSource && event.itemSource.currentValue) {
            this.componentCommonService.setDisplayNameForItems(event.itemSource.currentValue);

            if (this.itemSource instanceof Array)
                this.itemSource.forEach(e => { if (e.isDefault) this.selectedItem = e });

            if (this.vmChanged) this.vmChanged();
        }

        if (event.ngModel) {
            if (event.ngModel.currentValue) {

                if (typeof this.ngModel === 'string' || typeof this.ngModel === 'number') {
                    this.inputString = this.ngModel + '';

                    this.ngModel = {
                        inputString: this.inputString,
                        selectedItem: this.selectedItem,
                        value: +(this.selectedItem && this.selectedItem.procFunc ? this.selectedItem.procFunc(this.inputString) : this.inputString)
                    };

                    this.ngModelCtrl.$viewValue = this.ngModel;
                    this.ngModelCtrl.$commitViewValue();
                    if (this.vmChanged) this.vmChanged();
                } else {
                    this.ngModel = {
                        inputString: this.inputString,
                        selectedItem: this.selectedItem,
                        value: +(this.selectedItem && this.selectedItem.procFunc ? this.selectedItem.procFunc(this.inputString) : this.inputString)
                    };
                }

            } else {
                if (this.itemSource instanceof Array)
                    this.itemSource.forEach(e => { if (e.isDefault) this.selectedItem = e });

                if (typeof this.ngModel === 'string') this.inputString = this.ngModel + '';
                else this.inputString = '';
            }
        }
    };

};

let inputLabelDrop = () => {
    return {
        template: require('./template/input_label_drop.html'),
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
            isRequired: '@',
            placeholder: '@',

            vmChanged: '&'
        },
        controller: ['$scope', 'componentCommonService', inputLabelDropCtrl]
    }
};

export default inputLabelDrop;
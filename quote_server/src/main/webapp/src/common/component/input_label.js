const initView = Symbol('initView');

class inputLabelCtrl {

    constructor($scope) {
        this.$scope = $scope;
        this[initView]();
    };

    [initView]() {
        console.debug('inputLabelCtrl initView');
        
        // if (!this.itemInfo) return;
        // this.itemValue = {};
        // this.itemValue.id = this.itemInfo.id;
        // this.itemValue.labelName = this.itemInfo.labelName;
        // this.itemValue.itemContent = this.itemInfo.itemContent;
    };

    onInputChange() {
        this.ngModelCtrl.$viewValue = this.ngModel;
        this.ngModelCtrl.$commitViewValue();

        if (this.vmChanged) this.vmChanged();
    };

    $onChanges(event) {
        if (!event) return;

        if (event.itemSource && event.itemSource.currentValue) {

            if (event.displayPath) {
                // this.componentCommonService.setDisplayNameForItems(event.itemSource.currentValue, event.displayPath.currentValue);
            }
        }
    };
};

let inputLabel = () => {
    return {
        template: require('./template/input_label.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            label: '@',

            labelFlex: '<',
            inputFlex: '<',
            isRequired: '@',
            ngModel: '<',
            mutiLine: "@",
            isReadonly: "@",
            isDisabled: "@",
            placeholder:"@",
            vmChanged: '&',
            inputText:'@'
        },
        controller: ['$scope', inputLabelCtrl]
    }
};

export default inputLabel;
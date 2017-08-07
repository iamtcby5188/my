const initView = Symbol('initView');

class dropLabelCtrl {

    constructor($scope, componentCommonService, commonService) {
        this.$scope = $scope;
        this.componentCommonService = componentCommonService;
        this.commonService = commonService;
        this[initView]();
    };

    onDropChange() {
        this.ngModelCtrl.$viewValue = this.selectedItem;
        this.ngModelCtrl.$commitViewValue();
        if (this.vmChanged) this.vmChanged();
    }

    [initView]() {
        console.debug('dropLabelCtrl initView');

        // if (!this.itemInfo) return;

        // this.itemValue = {};
        // 在ItemSource里面设置isDefault属性，否则默认只能选第一个
        // this.itemValue.selectItem = this.itemInfo.list[0];
    };

    $onChanges(event) {
        if (!event) return;

        if (event.itemSource && event.itemSource.currentValue) {
            this.componentCommonService.setDisplayNameForItems(event.itemSource.currentValue, this.displayPath ? this.displayPath : undefined);

            event.itemSource.currentValue.forEach(e => {
                if (e.isDefault) {
                    this.selectedItem = e;
                    this.ngModelCtrl.$viewValue = this.selectedItem;
                    this.ngModelCtrl.$commitViewValue();
                    if (this.vmChanged) this.vmChanged();
                }
            });
            
            if(this.isReadonly){
                this.readonlyDisplayValue = this.commonService.getPropertyX(this.ngModel, this.displayPath) ;
            }
        }


        if (event.ngModel) {
            if (event.ngModel.currentValue) {
                if (this.itemSource instanceof Array) this.itemSource.forEach(e => {
                    if (e.$id === event.ngModel.currentValue.$id) {
                        this.selectedItem = e;
                        this.ngModelCtrl.$viewValue = this.selectedItem;
                    }
                });
            } else {
                if (this.itemSource instanceof Array) {
                    this.ngModelCtrl.$viewValue = undefined;
                    this.selectedItem = undefined;
                    this.itemSource.forEach(e => {
                        if (e.isDefault) {
                            this.selectedItem = e;
                            this.ngModelCtrl.$viewValue = this.selectedItem;
                        }
                    });
                }

                this.ngModelCtrl.$commitViewValue();
                if (this.vmChanged) this.vmChanged();
            }
        }
    };
};

let dropLabel = () => {
    return {
        template: require('./template/drop_label.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            label: '@',
            displayPath: '@',
            // itemInfo: '<',
            isReadonly: "@",
            itemSource: '<',
            labelFlex: '<',
            inputFlex: '<',
            isRequired: '@',
            ngModel: '<',
            placeholder: '@',

            vmChanged: '&'
        },
        controller: ['$scope', 'componentCommonService', 'commonService', dropLabelCtrl]
    }
};

export default dropLabel;
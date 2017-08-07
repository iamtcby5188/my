const initView = Symbol('initView');

class inputSelectorRangeCtrl {

    constructor($scope, componentCommonService) {
        this.$scope = $scope;
        this.componentCommonService = componentCommonService;
        this[initView]();
    };

    [initView]() {
        console.debug('inputRangeCtrl initView' + this.theme);
    };

    onSelectorChanged(){
        if(this.selectorValue === 'all' || this.selectorValue === 'none') return;
        this.ngModelCtrl.$viewValue = this.selectorValue;
        if(this.rangeValue){
            if(this.rangeValue.start) this.rangeValue.start = undefined;
            if(this.rangeValue.end) this.rangeValue.end = undefined;
        }

        this.ngModelCtrl.$commitViewValue();
        if (this.vmChanged) this.vmChanged();
    };

    onRangeChanged() {
        this.ngModelCtrl.$viewValue = [];
        var Tmp = angular.copy(this.rangeValue);
        Tmp.bRanged = true;
        this.ngModelCtrl.$viewValue[0] = Tmp;
        if(!this.rangeValue.start && !this.rangeValue.end){
            this.selectorValue = 'all';
        }
        else{
            this.selectorValue = 'none';
        }
        // this.selectorValue = undefined;
        // 设置为'none'为所有按钮均不高亮显示

        this.ngModelCtrl.$commitViewValue();
        if (this.vmChanged) this.vmChanged();
    };

    $onChanges(event) {
        if (!event) return;

        if (event.ngModel) {
            if (event.ngModel.currentValue) {
                if (event.ngModel.currentValue instanceof Array) {
                    var Tmp = event.ngModel.currentValue[0];
                    if (!Tmp) {
                        this.selectorValue = undefined;
                    } else if (Tmp.bRanged) {
                        this.rangeValue = Tmp;
                        this.selectorValue = 'none';
                    } else {
                        this.selectorValue = event.ngModel.currentValue;
                    }
                }
            } else {
                this.selectorValue = undefined;
                this.rangeValue = undefined;
            }
        }
    };
};

let inputSelectorRange = () => {
    return {
        template: require('./template/input_selector_range.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            theme: '@mdTheme',
            label: '@',
            displayPath: '@',
            itemSource: '<',
            hasSelectAllButton: '@',
            selectorFlex: '<',
            rangeFlex: '<',
            ngModel: '<',
            vmChanged: '&'
        },
        controller: ['$scope', 'componentCommonService', inputSelectorRangeCtrl]
    }
};

export default inputSelectorRange;
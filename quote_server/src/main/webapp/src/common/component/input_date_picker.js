const initView = Symbol('initView');

class inputDatePickerCtrl {

    constructor($scope) {
        this.$scope = $scope;
        this[initView]();
    };

    [initView]() {
        console.debug('inputDatePickerCtrl initView');
    };

    onVmChanged() {
        this.ngModelCtrl.$viewValue = this.ngModel;
        this.ngModelCtrl.$commitViewValue();
        if (this.vmChanged) this.vmChanged();
    };

    $onChanges(event) {
        if (!event) return;

        if (event.ngModel) {
            if (!event.ngModel.currentValue) {
                this.ngModel = angular.copy(this.defaultValue);
                this.ngModelCtrl.$viewValue = this.ngModel;
                this.ngModelCtrl.$commitViewValue();
                if (this.vmChanged) this.vmChanged();
            }
        }
    };

    getWeek(){
        if(this.ngModel){
            var week;
            switch(this.ngModel.getDay()){
                case 1: week="星期一"; break;
                case 2: week="星期二"; break;
                case 3: week="星期三"; break;
                case 4: week="星期四"; break;
                case 5: week="星期五"; break;
                case 6: week="星期六"; break;
                default: week="星期日";
            }

            return week;
            }
    }

    getInputFlex(){
        if(!this.ngModel || !this.isShowWeek){
            return "100";
        }
        else{
            return "70";
        }
    }
};

let inputDatePicker = () => {
    return {
        template: require('./template/input_date_picker.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            label: '@',
            isShowWeek:'@',
            pickerConfig: '<',
            labelFlex: '<',
            inputFlex: '<',
            isRequired: '@',
            ngModel: '<',
            defaultValue: '<',
            mutiLine: "@",

            nullValueString: '@',

            vmChanged: '&'
        },
        controller: ['$scope', inputDatePickerCtrl]
    }
};

export default inputDatePicker;
const initView = Symbol('initView');
const formatDate = Symbol('formatDate');
class inputDatePickerRangeCtrl {

    constructor($scope,componentCommonService) {
        this.$scope = $scope;
        this.componentCommonService = componentCommonService;
        this[initView]();
    };

    [formatDate](date,hours,minute,second,millisecond){
        if(!date) return;
        if(hours !== undefined) date.setHours(hours);
        if(minute !== undefined) date.setMinutes(minute);
        if(second !== undefined) date.setSeconds(second);
        if(millisecond !== undefined) date.setMilliseconds(millisecond);
    };
  

    [initView]() {
        console.debug('inputDatePickerRangeCtrl initView');
        this.dateRange = {};
        this.dateRange.start = new Date();
        this[formatDate](this.dateRange.start,0,0,0,0)
        this.dateRange.end = new Date();
    
        this[formatDate](this.dateRange.end,23,59,59,0)
        this.preRange = angular.copy(this.dateRange);
    };

    onVmChanged() {
        if(angular.equals(this.dateRange,this.preRange)) return;
        this.preRange = angular.copy(this.dateRange);

        if(this.dateRange.start > this.dateRange.end){
            this.componentCommonService.openErrorDialog({
                title: '错误',
                theme: 'ssAvalonUi',
                message: '结束时间必须大于开始时间'
            });
            return;
        }
   
        this.ngModelCtrl.$viewValue = angular.copy(this.dateRange);

        this[formatDate](this.ngModelCtrl.$viewValue.start,0,0,0,0)
        this[formatDate](this.ngModelCtrl.$viewValue.end,23,59,59,0)
        this.ngModelCtrl.$commitViewValue();
        if(this.vmChanged) this.vmChanged();
    };

    $onInit(){
        this.ngModelCtrl.$viewValue = this.dateRange;
        this.ngModelCtrl.$commitViewValue();
        if(this.vmChanged) this.vmChanged();
    };

};

let inputDatePickerRange = () => {
    return {
        template: require('./template/input_date_picker_range.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            label: '@',

            ctrlFlex: '<',
            isRequired: '@',
            ngModel: '<',
            mutiLine: "@",
            vmChanged:'&'
        },
        controller: ['$scope',"componentCommonService", inputDatePickerRangeCtrl]
    }
};

export default inputDatePickerRange;
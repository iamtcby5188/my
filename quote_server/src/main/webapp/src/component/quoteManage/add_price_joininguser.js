class addPriceJoininguserCtrl {

    constructor($scope, componentCommonService) {
        this.$scope = $scope;

        this.componentCommonService = componentCommonService;

        this.initView();
    };

    initView() {
        console.debug('addPriceJoininguserCtrl initView');
    };

    onVmChanged() {
        this.ngModelCtrl.$viewValue = this.userInfo;
        this.ngModelCtrl.$commitViewValue();
        if (this.vmChanged) this.vmChanged();
    };

    $onChanges(event) {
        if (!event) return;

        if (event.userInfoList && event.userInfoList.currentValue) {
            if (this.userInfoList instanceof Array) {
                this.componentCommonService.setDisplayNameForItems(this.userInfoList);
            }
        }

        if (event.userInfo) {
            if (event.userInfo.currentValue) {
                if (this.userInfoList instanceof Array) {
                    this.userInfoList.forEach(e => {
                        if (e.joiningUserDto && event.userInfo.currentValue.joiningUserDto &&
                            e.joiningUserDto.id === event.userInfo.currentValue.joiningUserDto.id)
                            this.userInfo = e;

                    });
                }
            } else {
                this.userInfo = this.defaultValue;
                this.ngModel = angular.copy(this.userInfo);
                this.ngModelCtrl.$viewValue = this.ngModel;
                this.ngModelCtrl.$commitViewValue();
                if (this.vmChanged) this.vmChanged();
            }
        }

        if (event.ngModel) {
            if (event.ngModel.currentValue) {
            } else {
                this.userInfo = this.defaultValue;
                this.ngModel = angular.copy(this.userInfo);
                this.ngModelCtrl.$viewValue = this.ngModel;
                this.ngModelCtrl.$commitViewValue();
                if (this.vmChanged) this.vmChanged();
            }
        }
    };
};

let addPriceJoininguser = () => {
    return {
        template: require('./template/add_price_joininguser.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            theme: '@mdTheme',
            label: '@',
            labelFlex: '<',
            inputFlex: '<',
            isReadonly: '@',

            vmChanged: '&',

            defaultValue: '<',

            userInfoList: '<itemSource',
            userInfo: '<ngModel'
        },
        controller: ['$scope', 'componentCommonService', addPriceJoininguserCtrl]
    }
};

export default addPriceJoininguser;
const initView = Symbol('initView');

class searchcriteriaAcceptinghouseQuotepricetypeCtrl {

    constructor($scope) {
        this.$scope = $scope;

        this[initView]();
    };

    onVmChanged() {

        this.vmType = this.vmType === 'quotePriceType' ? this.vmType : 'acceptingHouse';

        if (this.vmType === 'quotePriceType') {
            this.ngModelCtrl.$viewValue = this.quotePriceType;
        } else if (this.vmType === 'acceptingHouse') {
            this.ngModelCtrl.$viewValue = this.acceptingHouse;
        }

        this.ngModelCtrl.$commitViewValue();
        if (this.vmChanged) this.vmChanged();
    };

    [initView]() {
        console.debug('searchcriteriaAcceptinghouseQuotepricetypeCtrl initView');
    };

    $onChanges(event) {
        if (!event) return;

        // if (event.vmType && event.vmType.currentValue) {
        //     if (event.vmType.currentValue === 'quotePriceType') {
        //         this.vmType = 'quotePriceType';
        //     } else {
        //         this.vmType = 'acceptingHouse';
        //     }
        // } else {
        //     this.vmType = 'quotePriceType';
        // }

        if (event.ngModel) {
            if (event.ngModel.currentValue) {
                if (this.vmType === 'quotePriceType') {
                    if (this.ngModel instanceof Array && this.ngModel.length > 0) this.quotePriceType = this.ngModel;
                    else this.quotePriceType = undefined;
                } else {
                   if (this.ngModel instanceof Array && this.ngModel.length > 0) this.acceptingHouse = this.ngModel;
                   else this.acceptingHouse = undefined;
                }
            } else {
                if (this.vmType === 'quotePriceType') {
                    this.quotePriceType = undefined;
                } else {
                    this.acceptingHouse = undefined;
                }
            }
        }

        if (event.itemSource && event.itemSource.currentValue) {
            this.quotePriceTypeItemSource = this.itemSource.quotePriceTypeItemSource;
            this.acceptingHouseItemSource = this.itemSource.acceptingHouseItemSource;
        }
    }
};

let searchcriteriaAcceptinghouseQuotepricetype = () => {
    return {
        template: require('./template/searchcriteria_acceptinghouse_quotepricetype.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            theme: '@mdTheme',
            label: '<',
            labelFlex: '<',
            inputFlex: '<',
            itemSource: '<',

            vmChanged: '&',

            ngModel: '<',

            vmType: '<'
        },
        controller: ['$scope', searchcriteriaAcceptinghouseQuotepricetypeCtrl]
    }
};

export default searchcriteriaAcceptinghouseQuotepricetype;
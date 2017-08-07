const buildModel = Symbol("buildModel");

class addPriceAcceptinghouseQuotepricetypeCtrl {

    constructor($scope) {
        this.$scope = $scope;

        this.initView();
    };

    [buildModel]() {
        this.vmType = this.vmType === 'quotePriceType' ? this.vmType : 'acceptingHouse';

        if (this.vmType === 'quotePriceType') {
            return { quotePriceType: this.quotePriceType };
        } else if (this.vmType === 'acceptingHouse') {
            return { acceptingHouse: this.acceptingHouse };
        }
    };

    onVmChanged() {

        this.ngModelCtrl.$viewValue = this[buildModel]();
        this.ngModelCtrl.$commitViewValue();
        if (this.vmChanged) this.vmChanged();
    };

    initView() {
        console.debug('addPriceAcceptinghouseQuotepricetypeCtrl initView');
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
                    this.quotePriceType = this.ngModel.quotePriceType;
                } else {
                    this.acceptingHouse = this.ngModel.acceptingHouse;
                    this.selectedAcceptingHouse = this.ngModel.acceptingHouse;
                }
            } else {
                if (this.vmType === 'quotePriceType') {
                    if (this.itemSource) this.quotePriceType = this.itemSource.findItem(e => e.isDefault);
                } else {
                    this.acceptingHouse = undefined;
                }

                this.ngModelCtrl.$viewValue = this[buildModel]();
                this.ngModelCtrl.$commitViewValue();
                if (this.vmChanged) this.vmChanged();
            }
        }
    }
};

let addPriceAcceptinghouseQuotepricetype = () => {
    return {
        template: require('./template/add_price_acceptinghouse_quotepricetype.html'),
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

            vmType: '<',

            qptDisplayPath: '@'
        },
        controller: ['$scope', addPriceAcceptinghouseQuotepricetypeCtrl]
    }
};

export default addPriceAcceptinghouseQuotepricetype;
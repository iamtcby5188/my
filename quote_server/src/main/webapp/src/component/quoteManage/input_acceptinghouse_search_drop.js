const initView = Symbol('initView');

class inputAcceptinghouseSearchDropCtrl {

    constructor($scope, $q, $timeout, babQuoteService, componentCommonService, commonService) {
        this.$scope = $scope;
        this.$q = $q;
        this.$timeout = $timeout;

        this.babQuoteService = babQuoteService;
        this.componentCommonService = componentCommonService;
        this.commonService = commonService;

        this[initView]();
    };

    [initView]() {
        console.debug('inputAcceptinghouseSearchDropCtrl initView');

        // this.displayPath = "companyName";

        this.searchSource = "quick";
    };

    onVmChanged() {
        // this.selectedItem.$queryString = this.inputString;

        // if (this.selectedItem.type === undefined) {
        //     this.ngModelCtrl.$viewValue = this.selectedItem;
        //     this.ngModelCtrl.$commitViewValue();
        //     if (this.vmChanged) this.vmChanged();

        // } else {
        //     if (this.selectedItem.type === 'searchMore') {
        //         debugger;
        //         this.searchMoreText = this.selectedItem.queryString;
        //     }

        // }

        this.ngModelCtrl.$viewValue = this.selectedItem;
        this.ngModelCtrl.$commitViewValue();
        if (this.vmChanged) this.vmChanged();
    };

    searchMore(event) {

        if (!this.searchText || this.searchText === "") return;

        if (this.searchResult && this.searchResult.$$state && this.searchResult.$$state.status === 0) return;

        if (this.searchTaskTimer) this.$timeout.cancel(this.searchTaskTimer);

        var defer = this.$q.defer();

        // this.doSearch(defer, true);
        this.babQuoteService.babSearchAcceptingHouse(this.searchText, true).then(res => {
            if (!res || !res.result) {
                defer.reject();
            }

            defer.resolve(res.result);

            this.$timeout(() => {
                if (event && event.target) {

                    var element = $(event.target);

                    element = element.parentsUntil('input-acceptinghouse-search-drop').find('input');

                    if (element && element.length > 0) {
                        element.focus();
                    }
                }
            }, 500, true);

        }, res => {
            defer.reject();
        });

        this.searchResult = defer.promise;


    };

    onSelectedItemChange() {

        this.ngModelCtrl.$viewValue = this.selectedItem;
        this.ngModelCtrl.$commitViewValue();
        if (this.vmChanged) this.vmChanged();
    };

    $onChanges(event) {
        if (!event) return;

        if (event.ngModel) {
            if (event.ngModel.currentValue) {
                this.selectedItem = this.ngModel;
            } else {
                this.selectedItem = undefined;
            }
        }
    };

    searchTextChange() {

        if (!this.searchText || this.searchText === "") return;

        if (this.searchTaskTimer) this.$timeout.cancel(this.searchTaskTimer);

        var defer = this.$q.defer();

        this.searchTaskTimer = this.$timeout(() => {
            if (!this.searchText || this.searchText === "") {
                this.$timeout.cancel(this.searchTaskTimer);
                return;
            }
            this.babQuoteService.babSearchAcceptingHouse(this.searchText, false).then(res => {
                if (!res || !res.result) {
                    defer.reject();
                }

                defer.resolve(res.result);
            }, res => {
                defer.reject();
            });
        }, 500, true);

        this.searchResult = defer.promise;
    };
};

let inputAcceptinghouseSearchDrop = () => {

    return {
        template: require('./template/input_acceptinghouse_search_drop.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            theme: '@',
            label: '@',
            labelFlex: '<',
            inputFlex: '<',
            ngModel: '<',
            isRequired: '@',
            placeholder: '@',

            vmChanged: '&'
        },
        controller: ['$scope', '$q', '$timeout', 'babQuoteService', 'componentCommonService', 'commonService', inputAcceptinghouseSearchDropCtrl]
    }
};

export default inputAcceptinghouseSearchDrop;
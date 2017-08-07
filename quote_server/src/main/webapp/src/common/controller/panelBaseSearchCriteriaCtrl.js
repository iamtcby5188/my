const initView = Symbol('initView');

const panelTemplate = require('./../component/template/panel_search_criteria.html');

class panelBaseSearchCriteriaCtrl {

    constructor($scope, commonService) {
        this.$scope = $scope;

        this.commonService = commonService;

        this[initView]();
    };

    onVmChanged() {
        
        // this.ngModelCtrl.$viewValue = this.itemSource.findWhere(e => e.attribute && e.attribute.ngModel).map(e => e.attribute.ngModel);
        // this.ngModelCtrl.$commitViewValue();
        var viewModel = {};

        this.itemSource.forEach(item => {

            if (!item) return;

            if (item.attribute && item.vm) {
                if (item.attribute.ngModel instanceof Array) {
                    if (item.component === "searchcriteriaAcceptinghouseQuotepricetype") {
                        this.commonService.setPropertyX(viewModel, item.attribute.vmType, item.attribute.ngModel);
                    } else {
                        this.commonService.setPropertyX(viewModel, item.vm, item.attribute.ngModel);
                    }
                } else {
                    if (item.attribute.ngModel) this.commonService.setPropertyX(viewModel, item.vm, item.attribute.ngModel);
                }
            }
        });

        this.ngModelCtrl.$viewValue = viewModel;
        this.ngModelCtrl.$commitViewValue();

        if (this.$onSearchCriteriaVmChanged) this.$onSearchCriteriaVmChanged(this.ngModelCtrl.$modelValue);
    };

    [initView]() {
        console.debug('panelBaseSearchCriteriaCtrl initView');
    };

    $onChanges(event) {
        if (event.itemSource && event.itemSource.currentValue) {
            // this.itemSource = event.itemSource.currentValue;
        }

        if (event.ngModel) {

            if (event.ngModel.currentValue) {
                if (!(this.itemSource instanceof Array)) return;

                this.itemSource.forEach(item => {
                    var modelValue = this.commonService.getPropertyX(this.ngModel, item.vm);

                    if (item.component === 'searchcriteriaAcceptinghouseQuotepricetype') {
                        if (modelValue) {
                            var temp = {};
                            this.setPropertyX(temp, item.attribute.vmType, modelValue);
                            item.attribute.ngModel = temp;
                        }
                    } else {
                        // debugger;
                        // if (modelValue)
                        item.attribute.ngModel = modelValue;
                    }
                });

            } else {
                if (this.itemSource) this.itemSource.forEach((item, index) => {
                    item.attribute.ngModel = undefined;
                });
            }
        }
    };
};

export { panelBaseSearchCriteriaCtrl, panelTemplate };
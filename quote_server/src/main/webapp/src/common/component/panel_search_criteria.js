const initView = Symbol('initView');

class panelSearchCriteriaCtrl {

    constructor($scope, componentCommonService, commonService) {
        this.$scope = $scope;

        this.componentCommonService = componentCommonService;
        this.commonService = commonService;

        this[initView]();
    };

    onVmChanged() {
        this.ngModelCtrl.$viewValue = this.itemSource.findWhere(e => e.attribute && e.attribute.ngModel).map(e => e.attribute.ngModel);
        this.ngModelCtrl.$commitViewValue();
    };

    [initView]() {
        console.debug('panelSearchCriteriaCtrl initView');
    };

    $onChanges(event) {
        if (event.itemSource && event.itemSource.currentValue) {
            // this.componentCommonService.setDisplayNameForItems(event.itemSource.currentValue, event.displayPath.currentValue);
            this.itemSource = event.itemSource.currentValue;
        }

        if (event.ngModel) {

            if (event.ngModel.currentValue) {
                if (!this.itemSource) return;

                if (!(event.ngModel.currentValue instanceof Array)) {
                    console.error(`panelBaseSearchCriteriaCtrl $onChanges for 'ngModel': invalid value.`);
                    return;
                }

                if (this.itemSource) {
                    this.itemSource.forEach((item, index) => {
                        var isFound = false;

                        event.ngModel.currentValue.forEach(e => {
                            if (e instanceof Array) {
                                e.forEach(e1 => {
                                    if (e1.$parentCriteriaId === item.id) {
                                        isFound = true;
                                        item.attribute.ngModel = e;
                                    }
                                });
                            } else {
                                if (e.$parentCriteriaId === item.id) {
                                    isFound = true;
                                    item.attribute.ngModel = e;
                                }
                            }
                        });

                        if (!isFound) {
                            item.attribute.ngModel = undefined;
                        }
                    });
                }               

            } else {
                if (this.itemSource) this.itemSource.forEach((item, index) => {
                    item.attribute.ngModel = undefined;
                });
            }
        }
    };
};

let panelSearchCriteria = () => {
    return {
        template: require('./template/panel_search_criteria.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            theme: '@mdTheme',
            itemSource: '<',
            ngModel: '<'
        },
        controller: ['$scope', 'componentCommonService', 'commonService', panelSearchCriteriaCtrl]
    }
};

export default panelSearchCriteria;
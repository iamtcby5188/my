import { panelBaseSearchCriteriaCtrl, panelTemplate } from './../../common/controller/panelBaseSearchCriteriaCtrl';

const initView = Symbol('initView');

class searchCriteriaSscCtrl extends panelBaseSearchCriteriaCtrl {
    constructor($scope, componentCommonService, commonService) {
        super($scope, componentCommonService);

        this[initView]();
    };

    [initView]() {
        console.debug('searchCriteriaSscCtrl initView');
    };

    $onSearchCriteriaVmChanged(viewModel) {
        // debugger;

        if (!viewModel) return;

        if (viewModel instanceof Array && this.itemSource instanceof Array) {
            viewModel.forEach(criteriaVm => {
                var parentCriteria = this.itemSource.findItem(e => criteriaVm.$parentCriteriaId && e.id === criteriaVm.$parentCriteriaId);

                if (!parentCriteria) return;

                switch (parentCriteria.conditionName) {
                    case '票据类型':
                        if (parentCriteria.attribute && parentCriteria.attribute.ngModel) {
                            if (parentCriteria.attribute.ngModel === 'PAP_BKB' || parentCriteria.attribute.ngModel === 'PAP_CMB') {
                                this.itemSource.findItem(e => e);
                            } else if (parentCriteria.attribute.ngModel === 'ELE_BKB' || parentCriteria.attribute.ngModel === 'ELE_CMB') {

                            }
                        }
                        break;
                    default:
                        break;
                };
            });
        }
    };
};

let searchCriteriaSsc = () => {
    return {
        template: panelTemplate,
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            theme: '@mdTheme',
            itemSource: '<',
            ngModel: '<'
        },
        controller: ['$scope', 'componentCommonService', 'commonService', searchCriteriaSscCtrl]
    }
};

export default searchCriteriaSsc;
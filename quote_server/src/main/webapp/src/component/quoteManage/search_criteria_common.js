import { panelBaseSearchCriteriaCtrl, panelTemplate } from './../../common/controller/panelBaseSearchCriteriaCtrl';

const initView = Symbol('initView');

class searchCriteriaCommonCtrl extends panelBaseSearchCriteriaCtrl {
    constructor($scope, componentCommonService, commonService) {
        super($scope, commonService);

        this[initView]();
    };

    [initView]() {
        console.debug('searchCriteriaCommonCtrl initView');
    };

    $onSearchCriteriaVmChanged(viewModel) {
        // debugger;
    };
};

let searchCriteriaCommon = () => {
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
        controller: ['$scope', 'componentCommonService', 'commonService', searchCriteriaCommonCtrl]
    }
};

export default searchCriteriaCommon;
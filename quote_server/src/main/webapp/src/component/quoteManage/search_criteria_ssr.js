import { panelBaseSearchCriteriaCtrl, panelTemplate } from './../../common/controller/panelBaseSearchCriteriaCtrl';

const initView = Symbol('initView');

class searchCriteriaSsrCtrl extends panelBaseSearchCriteriaCtrl {
    constructor($scope, componentCommonService, commonService) {
        super($scope, commonService);

        this[initView]();
    };

    [initView]() {
        console.debug('searchCriteriaSsrCtrl initView');
    };

    $onSearchCriteriaVmChanged(viewModel) {
        // debugger;

        if (!viewModel) return;

        if (viewModel) {
            var define = this.itemSource.findItem(e => e.conditionName === "票据类型");
            var target = this.itemSource.findItem(e => e.conditionName === "承兑行承兑方类别");
            var targetDueDate = this.itemSource.findItem(e => e.conditionName === "剩余期限");
            if (viewModel[define.vm]) {
                if (viewModel[define.vm].code === 'PAP_BKB' || viewModel[define.vm].code === 'ELE_BKB') {
                    if (target && target.attribute) target.attribute.vmType = 'quotePriceType';
                } else if (viewModel[define.vm].code === 'PAP_CMB' || viewModel[define.vm].code === 'ELE_CMB') {
                    if (target && target.attribute) target.attribute.vmType = 'acceptingHouse';
                }
            }            
        }

        // if (viewModel instanceof Array && this.itemSource instanceof Array) {
        //     viewModel.forEach(criteriaVm => {
        //         if (!criteriaVm) return;

        //         var parentCriteria = this.itemSource.findItem(e => criteriaVm.$parentCriteriaId && e.id === criteriaVm.$parentCriteriaId);
        //         var targetCriteria = undefined;

        //         if (!parentCriteria) return;

        //         switch (parentCriteria.conditionName) {
        //             case '票据类型':
        //                 if (parentCriteria.attribute && parentCriteria.attribute.ngModel) {
        //                     // targetCriteria = this.itemSource.findItem(e => e.conditionName === "剩余期限");
        //                     // if (parentCriteria.attribute.ngModel.code === 'PAP_BKB' || parentCriteria.attribute.ngModel.code === 'PAP_CMB') {
        //                     //     if (targetCriteria && targetCriteria.attribute.itemSource.selectorValue instanceof Array) {
        //                     //         targetCriteria.attribute.itemSource.selectorValue.forEach(e => {
        //                     //             e.isDisabled = e.code === "LESS_YEAR" || e.code === "YEAR";
        //                     //         });
        //                     //     }
        //                     // } else if (parentCriteria.attribute.ngModel.code === 'ELE_BKB' || parentCriteria.attribute.ngModel.code === 'ELE_CMB') {
        //                     //     if (targetCriteria && targetCriteria.attribute.itemSource.selectorValue instanceof Array) {
        //                     //         targetCriteria.attribute.itemSource.selectorValue.forEach(e => { e.isDisabled = false; });
        //                     //     }
        //                     // }

        //                     targetCriteria = this.itemSource.findItem(e => e.conditionName === "承兑行承兑方类别");
        //                     if (parentCriteria.attribute.ngModel.code === 'PAP_BKB' || parentCriteria.attribute.ngModel.code === 'ELE_BKB') {
        //                         if (targetCriteria && targetCriteria.attribute) targetCriteria.attribute.vmType = 'quotePriceType';
        //                     } else if (parentCriteria.attribute.ngModel.code === 'PAP_CMB' || parentCriteria.attribute.ngModel.code === 'ELE_CMB') {
        //                         if (targetCriteria && targetCriteria.attribute) targetCriteria.attribute.vmType = 'acceptingHouse';
        //                     }
        //                 }
        //             default:
        //                 break;
        //         };
        //     });
        // }
    };
};

let searchCriteriaSsr = () => {
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
        controller: ['$scope', 'componentCommonService', 'commonService', searchCriteriaSsrCtrl]
    }
};

export default searchCriteriaSsr;
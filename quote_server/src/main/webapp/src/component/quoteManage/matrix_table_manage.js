const initView = Symbol('initView');

class matrixTableManageCtrl {

    constructor($scope, $http, testDataService) {
        this[initView]();
    };

    [initView]() {
        console.debug('matrixTableManageCtrl initView');
    };

    $onChanges(event) {
        if (!event) return;

        if (event.itemSource) {

            if (event.itemSource.currentValue) {
                this.isNoData = event.itemSource.currentValue instanceof Array && event.itemSource.currentValue.length === 0;
            } else {
                this.isNoData = true;
            }
        }
    };
};

let matrixTableManage = () => {
    return {
        template: require('./template/matrix_table_manage.html'),
        bindings: {
            theme: '@mdTheme',

            itemSource: '<'
        },
        controller: [matrixTableManageCtrl]
    }
};

export default matrixTableManage;
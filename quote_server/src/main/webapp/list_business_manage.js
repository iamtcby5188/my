const initView = Symbol('initView');

class listBusinessManageCtrl {

    constructor($scope, $http) {
        this.$scope = $scope;

        this.$http = $http;

        this[initView]();
    };

    [initView]() {
        console.debug('listBusinessManageCtrl initView');
        console.debug(`listBusinessManageCtrl this.theme = ${this.theme}`);

        this.$scope.message = "BusinessManage page."
    };
};

let listBusinessManage = () => {
    return {
        template: require('./template/list_business_manage.html'),
        bindings: {
            theme: '@'
        },
        controller: ['$scope', '$http', listBusinessManageCtrl]
    }
};

export default listBusinessManage;
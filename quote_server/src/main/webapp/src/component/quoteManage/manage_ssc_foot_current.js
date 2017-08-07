const initView = Symbol('initView');

class manageSscFootCurrentCtrl {

    constructor($scope) {
        this.$scope = $scope;

        this[initView]();
    };

    [initView]() {
        console.debug('manageSscFootCurrentCtrl initView');
    };

    $onChanges(event) {
        if (!event) return;

        if (event.ngModel) {
            
        }
    };
};

let manageSscFootCurrent = () => {
    return {
        template: require('./template/manageSscFootCurrentCtrl.html'),
        bindings: {
            theme: '@mdTheme',


        },
        controller: ['$scope', manageSscFootCurrentCtrl]
    }
};

export default manageSscFootCurrent;
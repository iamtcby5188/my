const initView = Symbol('initView');

class chartManageSscCtrl {

    constructor($scope)  {
        this.$scope = $scope;

        this[initView]();
    };

    [initView]() {
        console.debug('chartManageSscCtrl initView');
   };

};

let chartManageSsc = () => {
    return {
        template: require('./template/list_ticket_hall.html'),
        bindings: {
            theme: '@'
        },
        controller: ['$scope', chartManageSscCtrl]
    }
};

export default chartManageSsc;
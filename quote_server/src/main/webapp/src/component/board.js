const initView = Symbol('initView');

class boardCtrl {

    constructor($scope) {
        this.$scope = $scope;

        this[initView]();
    };

    [initView]() {
        console.debug('boardCtrl initView');
        console.debug(`boardCtrl this.theme = ${this.theme}`);

        this.$scope.message = "Board page."
    };
};

let board = () => {
    return {
        template: require('./template/board.html'),
        bindings: {
            theme: '@'
        },
        controller: ['$scope', boardCtrl]
    }
};

export default board;
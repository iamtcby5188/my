const initView = Symbol('initView');

class inputHiddenCtrl {

    constructor() {
        this[initView]();
    };

    [initView]() {
        console.debug('inputHiddenCtrl initView');
    };
};

let inputHidden = () => {
    return {
        template: '<div class="hidden"></div>',
        bindings: {
            ngModel: '<',
        },
        controller: ['$scope', inputHiddenCtrl]
    }
};

export default inputHidden;
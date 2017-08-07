const initView = Symbol('initView');

class showImgCtrl {

    constructor($mdDialog) {
        this.$mdDialog = $mdDialog;
        this[initView]();
        this.theme="ssAvalonUi"; 
    };


    [initView]() {
        console.debug('showImgCtrl initView');
    };


    $onClickCancel(event) {

        this.$mdDialog.cancel();
    };
};

export default ['$mdDialog',showImgCtrl];
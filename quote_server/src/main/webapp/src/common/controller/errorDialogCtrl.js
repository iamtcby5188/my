import panelBaseDialogCtrl from './panelBaseDialogCtrl';

const initView = Symbol('initView');

class errorDialogCtrl extends panelBaseDialogCtrl {

    constructor($scope, commonService) {
        super(commonService)

        this.$scope = $scope;

        this[initView]();
    };

    [initView]() {
        console.debug('errorDialogCtrl initView');

        this.dialogType = 'errorDialog';
    };

    $onClickOk(event) {
        this.$mdDialog.hide(this.$dialogResult);
    };

    $onClickCancel(event) {
        this.$mdDialog.cancel();
    };

    $onChanges(event) {
    };
};

export default ['$mdDialog', '$scope', 'commonService', errorDialogCtrl];
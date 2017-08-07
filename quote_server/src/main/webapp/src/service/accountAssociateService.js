import addAccountAssociateCtrl from '../controller/addAccountAssociateCtrl';
const init = Symbol('init');

class accountAssociateService {
    constructor($mdDialog) {
        this.$mdDialog = $mdDialog;
        this[init]();
    };

    [init](){
        console.debug('accountAssociateService initView');
    }

    openAddAccountAssociateDlg(params) {
        var config = {
            attachTo: angular.element(document.body),
            controller: addAccountAssociateCtrl,
            controllerAs: '$ctrl',
            bindToController: true,
            locals: {
                theme: params.theme || 'default',
                title: " 新增关联账户",
                okText: params.okText || "确认",
                cancelText: params.cancelText || "取消",
                onClosing:params.onClosing || (()=>true),
            },
            template:require('../component/accountManage/template/panel_add_account_associate.html'),
            hasBackdrop: true,
            panelClass: 'panel-addprice-dialog',
            trapFocus: true,
            clickOutsideToClose: false,
        };

        return this.$mdDialog.show(config);
    }
};

export default ['$mdDialog', accountAssociateService];
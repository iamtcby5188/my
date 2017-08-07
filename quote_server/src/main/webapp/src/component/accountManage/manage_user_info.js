const initView = Symbol('initView');
const onAuthFailed = Symbol('onAuthFailed');


class manageUserInfoCtrl {
    constructor($scope,httpService,configConsts,userService,md5,accountAssociateService,componentCommonService){
        this.$scope = $scope;
        this.httpService = httpService;
        this.configConsts = configConsts;
        this.userService = userService;
        this.md5 = md5;
        this.accountAssociateService = accountAssociateService;
        this.componentCommonService = componentCommonService;
        this.joinUserinfo =[];
        this[initView]();
    };

    [initView]() {
        console.debug('manageUserInfoCtrl initView');
    };

    $onInit(){
        var userInfo = this.userService.getUserInfo();
        let header = this.userService.getAuthHeaders();

        this.enableButton = userInfo.info["bab.quote.ssr.management"]?false:true;

        this.httpService.postService(this.configConsts.query_user_info,userInfo.user.id,header).then(e=>{
            if(e && e.result && angular.isArray(e.result)){
                this.joinUserinfo = e.result[0].availableContactDto;
                this.curUserInfo = e.result[0].userInfo;
            }
        },res=>{
            if (res && res.return_message && res.return_message.exceptionCode === "E8888") {
                this.componentCommonService.openErrorDialog({
                    title: res.return_message.exceptionName,
                    message: res.return_message.exceptionMessage,
                    theme: this.theme
                });
            } else {
                this.componentCommonService.openErrorDialog({
                    title: '错误',
                    message: '初始化数据获取失败',
                    theme: this.theme
                });
            }
        })
    }

    $routerOnActivate(currentInstruction, previousInstruction) {

        if (currentInstruction) {
            if (currentInstruction.routeData && currentInstruction.routeData.data) {
                this.theme = currentInstruction.routeData.data.theme;
            }
        }

        var userInfo = this.userService.getUserInfo();

        if (userInfo) {
            var canActive = userInfo.info["bab.quote.ssr.management"];

            if (!canActive || canActive !== "true") {
                this[onAuthFailed]();
            }
        }
    };

   [onAuthFailed]() {
        this.isAuthFailed = true;

        this.$router.navigateByUrl('../login');
    };

    onClickAdd(evt){
        this.accountAssociateService.openAddAccountAssociateDlg({
            theme:this.theme,
            onClosing:()=>{
                let header = this.userService.getAuthHeaders();
                let userinfo = this.userService.getUserInfo();
                this.httpService.postService(this.configConsts.query_user_info, userinfo.user.id, header).then(e=>{
                    if(e && e.result && angular.isArray(e.result)){
                        this.joinUserinfo = e.result[0].availableContactDto;
                    }
                });
            }
        });
    }

    unJoinUser(contact){
        console.debug(contact);
        let header = this.userService.getAuthHeaders();
        let userinfo = this.userService.getUserInfo();
        let param = {
            currentUserId:userinfo.user.id,
            joiningUserDto:{
                id:contact.id,
            },
            joiningMode:"CTR",
            joingTarget:"CHILD",
            delJoinUser:true,
        };
        this.httpService.postService(this.configConsts.set_user_join_relation, param, header).then(e=>{
            if(e && e.return_code == 0){
                this.httpService.postService(this.configConsts.query_user_info, userinfo.user.id, header).then(e1=>{
                    if(e1 && e1.result && angular.isArray(e1.result)){
                        this.joinUserinfo = e1.result[0].availableContactDto;
                    }
                });
            }
        });
    }
}

let manageUserInfo = () => {
    return {
        template: require('./template/manage_user_info.html'),
        bindings: {
            theme: '@mdTheme',
            $router: '<'
        },
        controller: ['$scope','httpService','configConsts','userService','md5','accountAssociateService','componentCommonService',manageUserInfoCtrl]
    }
};

export default manageUserInfo;
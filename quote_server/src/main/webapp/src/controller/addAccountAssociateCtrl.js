const initView = Symbol('initView');

class addAccountAssociateCtrl {

    constructor(componentCommonService,$mdDialog,userService,httpService,md5,configConsts) {
        this.componentCommonService = componentCommonService;
        this.$mdDialog = $mdDialog;
        this.userService = userService;
        this.httpService = httpService;
        this.md5 = md5;
        this.configConsts = configConsts;
        
        this.theme="ssAvalonUi";
        this.labelFlex = "15";
        this.inputFlex = "85";

        this.radioDate=[
            {value:"parent",display:"关联为我的母账户",type:"PARENT"},
            {value:"child",display:"关联为我的子账户",type:"CHILD"},
        ];

        this.selected = "parent";
        this[initView]();
    };


    [initView]() {
        console.debug('addAccountAssociateCtrl initView');
    };

    getErrorMessage(code){
        switch(code){
            case 'E8888':
            return "认证失败";
            case 'E6001':
            return "已是子账户,无法关联其他账户为自己的子账户";
            case 'E6002':
            return "已是母账户,无法关联其他账户为自己的母账户";
            default:
            return "关联失败";
        }
    }

    getSuccessMessage(ret){
        if(!ret || !ret.result ||!angular.isArray(ret.result) || ret.result.length <= 0) 
        return "关联成功";

        var info = ret.result[0];
        if(!info || !info.joiningCompanyDto || !info.joiningCompanyDto.province ||
           !info.joiningUserDto || !info.joiningUserDto.name) 
           
           return "关联成功";

        var message = `${info.joiningCompanyDto.province}的${info.joiningUserDto.name}已与您的账户关联,成为您的${this.getTarget() ==="PARENT"?"母":"子"}账户`

        return message;
    }
    getTarget(){
        var type = "PARENT";
        this.radioDate.forEach(e=>{
            if(e.value === this.selected){
                type = e.type;
            }
        });

        return type;
    }

    $onClickOk(event) {
        let headers = this.getAuthHeaders();
        let param = {
            currentUserId:this.userService.getUserInfo().user.id,
            joiningUserDto:{
                userName:this.userName,
                password:this.md5.createHash(this.password || '')
            },
            joiningMode:"CTR",
            joingTarget:this.getTarget()
        };

        this.httpService.postService(this.configConsts.set_user_join_relation, param, headers)
            .then(e=>{
                    this.$mdDialog.hide();
                    this.componentCommonService.openErrorDialog({
                        title:"提示",
                        theme:this.theme,
                        message:this.getSuccessMessage(e)
                    });

                    if(this.onClosing)
                        this.onClosing();
            },e=>{
                    var message = "关联失败";
                    if(e && e.return_message && e.return_message.exceptionCode){
                        message = this.getErrorMessage(e.return_message.exceptionCode);
                    }
                    this.componentCommonService.openErrorDialog({
                        title:"关联失败",
                        theme:this.theme,
                        message:message
                    });
            });

    };

    $onClickCancel(event) {

        this.$mdDialog.cancel();
    };

    getAuthHeaders () {
        return this.userService.getAuthHeaders();
    };

};

export default ['componentCommonService','$mdDialog','userService','httpService','md5','configConsts',addAccountAssociateCtrl];
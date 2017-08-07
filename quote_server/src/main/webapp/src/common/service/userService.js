const init = Symbol('init');
const $isLoggedIn = Symbol('$isLoggedIn');
const $loginInfo = Symbol('$loginInfo');
const setUserInfo = Symbol('setUserInfo');
const userExpiredTimer = Symbol('userExpiredTimer');
const registLoginExpire = Symbol('registLoginExpire');
const userExpireHandler = Symbol('userExpireHandler');
const buildAuthHeader = Symbol('buildAuthHeader');   

class userService {

    constructor($q, httpService, qbService, componentCommonService, configConsts) {
        this.$q = $q;

        this.httpService = httpService;
        this.qbService = qbService;
        this.componentCommonService = componentCommonService;

        this.configConsts = configConsts;

        this[init]();
    };

    login(param) {

        var defer = this.$q.defer();

        // if(!param) defer.reject(undefined);        

        // this.$http.post('/service/login', { userName: param.userName, password: param.password }).then(res =>{
        //     this.isLoggedIn = true;
        //     this.userInfo = res;
        //     defer.resolve(res);
        //     return res;
        // }, res=>{
        //     defer.reject(res);
        //     return res;
        // });

        // this[$loginInfo] = param;
        // defer.resolve(param);

        this.httpService.postService(this.configConsts.bab_init_login, { userName: param.userName, password: param.password }).then(res => {

            if (res.result && res.result[0]) {
                var time = +res.result[0].currentUser.tokenExpiredTime;

                if (time) {
                    res.result[0].currentUser.expiredTime = new Date(new Date().getTime() + time * 1000);
                }
            }

            this[setUserInfo](res);

            // TODO 
            // this[userExpiredTimer] = setTimeout(() => {
            //     if (this[userExpireHandler]) this[userExpireHandler]();
            // }, 0);

            defer.resolve(res);
        }, res => {
            defer.reject(res);
        });

        // private static final String TOKEN = "token";
        // private static final String USERID = "userid";
        // private static final String USERNAME = "username";
        // private static final String PASSWORD = "password";

        return defer.promise;
    };

    getLoginInfoAsync() {
        var defer = this.$q.defer();

        if (this[$loginInfo]) defer.resolve(this[$loginInfo]);

        if (this.qbService) {
            this.qbService.getUserData(res => {

                let user = undefined;
                try {
                    user = JSON.parse(res);
                } catch (e) {
                    defer.reject();
                };

                this[$loginInfo] = { userName: user.UserAccount, password: user.Password };
                defer.resolve(this[$loginInfo]);
            }, res => {
                console.warn("Call qbService.getUserData failed. " + res)
                defer.reject();
            });
        } else {
            console.warn("qbService.getUserData not defined. ");
            defer.reject();
        }

        return defer.promise;
    };

    logout() {
        this[$isLoggedIn] = false;

        this.userInfo = undefined;
    };

    registLoginExpireHandler(callback) {
        if (callback && typeof callback === 'function') {
            this[userExpireHandler] = [];
            this[userExpireHandler].push(callback);
        }
    };

    [registLoginExpire]() {
        this.componentCommonService.openErrorDialog({
            title: '会话超时',
            theme: 'ssAvalonUi',
            message: '请关闭当前画面，再重新打开'
        });

        if (this[userExpireHandler] instanceof Array) {
            this[userExpireHandler].forEach(e => { if(e) e(); });
        }
    };

    // isLoggedIn() {
    //     return this.isLoggedIn;
    // };    

    getUserInfo() {
        if (!this[$isLoggedIn]) return undefined;

        if (this.userInfo.user.expiredTime < new Date()) {
            this[registLoginExpire]();
        }

        return this.userInfo;
    };

    getUserInfoAsync() {
        let userInfo = this.getUserInfo();

        if (userInfo) return this.$q.resolve(userInfo);

        return this.getLoginInfoAsync().then(res => {
            if (!res) return this.$q.reject(res);
            return this.login(res);
        }, res => this.$q.reject(res));
    };

    getAuthHeaders() {
        var userInfo = this.getUserInfo();
        var headers = this[buildAuthHeader](userInfo);
        return headers;
    };

    getAuthHeadersAsync() {
        return this.getUserInfoAsync().then(userInfo => {
            var headers = this[buildAuthHeader](userInfo);
            if (!headers || !headers.token) return this.$q.resolve(undefined);
            return this.$q.resolve(headers);
        }, res => this.$q.reject(res));
    };

    [setUserInfo](res) {
        if (res && res.result instanceof Array && res.result.length === 1) res = res.result[0];

        var info = {};

        if (res) {
            info.user = res.currentUser;
            info.info = res.info;
            info.company = res.currentCompany;
        }

        if (!info.user || !info.company) {
            console.error("userService[setUserInfo] 获取登录信息失败。");
        }

        this[$isLoggedIn] = true;
        this.userInfo = info;
    };

    [buildAuthHeader](userInfo) {
        var headers = userInfo ? {
            'token': userInfo.info ? userInfo.info.Token : '',
            'username': userInfo.user ? userInfo.user.userName : '',
            'userid': userInfo.user ? userInfo.user.id : '',
        } : undefined;

        return headers;
    };

    getToken(type) {
        return this.httpService.postService(this.configConsts.bab_get_token);
    };

    [init]() {
        console.debug('userService initialized.');

        this[$isLoggedIn] = false;
    };
};

export default ['$q', 'httpService', 'qbService', 'componentCommonService', 'configConsts', userService];
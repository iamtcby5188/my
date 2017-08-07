const initView = Symbol('initView');
const initNavDisPlay = Symbol('initNavDisPlay');

const THEME_NAME = "ssAvalonUi";

const dataDefine = {

};

class homeCtrl {

    constructor($scope, userService, babQuoteService, configConsts) {
        this.theme = THEME_NAME;

        this.$scope = $scope;

        this.userService = userService;
        this.babQuoteService = babQuoteService;

        this.configConsts = configConsts;
        this.navVisiable = true;
        this[initView]();
    };

    [initNavDisPlay]() {

        if (this.configConsts.showMainNavbar === true) {
            return true;
        }

        var userInfo = this.userService.getUserInfo();

        var display = false;

        if (this.$scope.currentNavItem === "SSR") {
            display = {
                SSR: true,
                SSC: true,
                NPC: false,

                MngSsr: false,
                MngSsc: false,
                MngNpc: false,
            };

            display.NPC = userInfo && userInfo.info && userInfo.info["bab.quote.npc.view"] === "true";
        } else {
            display = {
                SSR: false,
                SSC: false,
                NPC: false,

                MngSsr: false,
                MngSsc: false,
                MngNpc: false,
            };

            if (this.$scope.currentNavItem === "MngSsr") {
                display.MngSsr = userInfo && userInfo.info && userInfo.info["bab.quote.ssr.management"] === "true";
            } else if (this.$scope.currentNavItem === "MngSsc") {
                display.MngSsc = userInfo && userInfo.info && userInfo.info["bab.quote.ssr.management"] === "true";
            } else if (this.$scope.currentNavItem === "MngNpc") {
                display.MngNpc = userInfo && userInfo.info && userInfo.info["bab.quote.npc.management"] === "true";
            } 
        }

        // console.debug(`homeCtrl this.$scope.currentNavItem = ${JSON.stringify(this.$scope.currentNavItem)}`);

        return display;
    };

    [initView]() {
        console.debug('homeCtrl initView');

        // if (this.$router.parent.lastNavigationAttempt) {
        //     if (/\/home\/list\w+/.exec(this.$router.parent.lastNavigationAttempt)[0] === this.$router.parent.lastNavigationAttempt) {
        //         this.$scope.currentNavItem = 'SSR';
        //         this.$router.navigate(['SSR']);
        //     }
        //     else if (/\/home\/manage\w+/.exec(this.$router.parent.lastNavigationAttempt)[0] === this.$router.parent.lastNavigationAttempt) {
        //         this.$scope.currentNavItem = 'MngSsr';
        //         this.$router.navigate(['MngSsr']);
        //     } else {
        //         this.$router.navigate(['Login']);
        //     }
        // } else {
        //     this.$router.navigate(['Login']);
        // }
    };

    // 登录验证 + 路由拦截
    $routerOnActivate(currentInstruction, previousInstruction) {

        console.debug('homeCtrl $routerOnActivate + ' + this.$router.parent.lastNavigationAttempt);

        var checkLogin = (componentName, pageName) => {
            if (this.userService.getUserInfo()) {
                this.$scope.currentNavItem = pageName;
                this.navDisplay = this[initNavDisPlay]();
                // this.$router.navigate([componentName]);
            } else {
                // this.$router.navigateByUrl('../Login' + this.$router.parent.lastNavigationAttempt);
                this.$router.navigate(['Login', { page: componentName }]);
            }
        };
        this.navVisiable = true;
        if (this.$router.parent.lastNavigationAttempt) {
            if (this.$router.parent.lastNavigationAttempt === "/login") {
                this.$scope.currentNavItem = 'SSR';
                this.navDisplay = this[initNavDisPlay]();
            } else if (/\/login\?page=List\w+/.test(this.$router.parent.lastNavigationAttempt)) { checkLogin('ListSsrTicketHall', 'SSR'); }
            else if (/\/login\?page=Manage\w+/.test(this.$router.parent.lastNavigationAttempt)) {
                if (this.$router.parent.lastNavigationAttempt === "/login?page=ManageSsrTicketHall") { checkLogin('ManageSsrTicketHall', 'MngSsr'); }
                else if (this.$router.parent.lastNavigationAttempt === "/login?page=ManageSscTicketHall") { checkLogin('ManageSscTicketHall', 'MngSsc'); }
                else if (this.$router.parent.lastNavigationAttempt === "/login?page=ManageNpcTicketHall") { checkLogin('ManageNpcTicketHall', 'MngNpc'); }
                else if (this.$router.parent.lastNavigationAttempt === "/login?page=ManageUserInfo") { this.navVisiable = false;checkLogin('ManageUserInfo', 'MngUser'); }
            }
            else if (/\/login\?page=Price\w+/.test(this.$router.parent.lastNavigationAttempt)) {this.navVisiable = false; checkLogin('PriceDifferenceAnalysis', 'PDA'); }
            else if (/\/home\/list\w+/.test(this.$router.parent.lastNavigationAttempt)) { checkLogin('ListSsrTicketHall', 'SSR'); }
            else if (/\/home\/manage\w+/.test(this.$router.parent.lastNavigationAttempt)) {
                if (this.$router.parent.lastNavigationAttempt === "/home/manageSsrTicketHall") { checkLogin('ManageSsrTicketHall', 'MngSsr'); }
                else if (this.$router.parent.lastNavigationAttempt === "/home/manageSscTicketHall") { checkLogin('ManageSscTicketHall', 'MngSsc'); }
                else if (this.$router.parent.lastNavigationAttempt === "/home/manageNpcTicketHall") { checkLogin('ManageNpcTicketHall', 'MngNpc'); }
                else if (this.$router.parent.lastNavigationAttempt === "/home/manageUserInfo") {this.navVisiable = false; checkLogin('ManageUserInfo', 'MngUser'); }
            }
            else if (/\/home\/price\w+/.test(this.$router.parent.lastNavigationAttempt)) {this.navVisiable = false; checkLogin('PriceDifferenceAnalysis', 'PDA'); }
        }

        // debugger;
        // if (!this.userService.getUserInfo()) {
        //     this.$router.navigate(['Login']);
        // }
    };
};

let home = () => {
    console.debug('Create component home');

    return {
        template: require('./template/home.html'),
        bindings: {
            theme: '@mdTheme',
            $router: '<'
        },
        controller: ['$scope', 'userService', 'babQuoteService', 'configConsts', homeCtrl],

        $routeConfig: [
            // SSR/SSC/NPC
            // 直贴交易/全国直贴报价/全国转帖报价
            { path: '/listSsrTicketHall', name: 'ListSsrTicketHall', component: 'listSsrTicketHall', data: { theme: THEME_NAME }, useAsDefault: true },
            { path: '/listSscTicketHall', name: 'ListSscTicketHall', component: 'listSscTicketHall', data: { theme: THEME_NAME } },
            { path: '/listNpcTicketHall', name: 'ListNpcTicketHall', component: 'listNpcTicketHall', data: { theme: THEME_NAME } },

            { path: '/manageSsrTicketHall', name: 'ManageSsrTicketHall', component: 'manageSsrTicketHall', data: { theme: THEME_NAME } },
            { path: '/manageSscTicketHall', name: 'ManageSscTicketHall', component: 'manageSscTicketHall', data: { theme: THEME_NAME } },
            { path: '/manageNpcTicketHall', name: 'ManageNpcTicketHall', component: 'manageNpcTicketHall', data: { theme: THEME_NAME } },

            { path: '/manageUserInfo', name: 'ManageUserInfo', component: 'manageUserInfo', data: { theme: THEME_NAME } },
            { path: '/priceDifferenceAnalysis', name: 'PriceDifferenceAnalysis', component: 'priceDifferenceAnalysis' ,data: { theme: THEME_NAME }}, 
            // { path: '/ssc', name: 'ListBusinessManage', component: 'listBusinessManage', data: { theme: THEME_NAME } },
            // { path: '/add_price_input', name: 'AddPriceInput', component: 'addPriceInput', data: { theme: THEME_NAME } },
            // { path: '/listTransferPriceManage', name: 'ListTransferPriceManage', component: 'listTransferPriceManage', data: { theme: THEME_NAME } }
        ],

        // $canActivate: ['$nextInstruction', '$prevInstruction', '$route', 'userService', function ($nextInstruction, $prevInstruction, $location, userService) {
        //     console.log('home $canActivate', arguments);

        //     if(userService.isLoggedIn) return true;

        //     return true;

        //     // $router.navigate(['Login']);
        // }]
    }
};

export default home;
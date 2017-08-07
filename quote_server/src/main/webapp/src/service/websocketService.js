/**
 * Created by jiannan.niu on 2017/1/6.
 */

const init = Symbol('init');

class websocketService {

    constructor(configConsts, gridColumnFactoryService, userService, commonService) {
        this.gridColumnFactoryService = gridColumnFactoryService;
        this.userService = userService;
        this.commonService = commonService;
        this[init](configConsts);
    }

    [init](configConsts) {
        console.debug('websocketService initialized.');
        this.listener = [];
        this.isBuild = false;
        this.isReconnect = false;

        this.userService.registLoginExpireHandler(() => {
            this.connection.close(1000, '');
        });
    }

    buildWebsocket(configConsts) {
        if (this.isBuild) return;
        let time = parseInt(Math.random() * 10000);
        //https://github.com/joewalnes/reconnecting-websocket/
        let options = {
            debug: false, //default: false
            reconnectInterval: time, //default: 1000
            maxReconnectInterval: 30000,//default: 30000
            reconnectDecay: 2, //default: 1.5
            maxReconnectAttempts: 15 //default: null
        }
        this.connection = new ReconnectingWebSocket(configConsts.ws_address, [], options);
        this.isBuild = true;
        let listener = this.listener;
        let ws = this.connection;
        let userInfo = this.gridColumnFactoryService.getAuthHeaders();
        this.connection.onopen = () => {
            // console.log(userInfo);
            if (this.isDead) {
                this.isReconnect = true;
                listener.forEach(e => {
                    e.scope.isReconnect = true;
                    this.commonService.safeApply(e.scope);
                });
            }
            this.networkState = '良好';
            listener.forEach(e => {
                e.scope.networkState = '良好';
                this.commonService.safeApply(e.scope);
            });
            ws.send(JSON.stringify(userInfo));
        };

        this.connection.onmessage = (msg) => {
            this.gridColumnFactoryService.getAuthHeaders();
            if (msg.data === "ping") {
                console.log("Received ping for keeping websocket connection.");
                return;
            } else {
                listener.forEach(e => {
                    if (e.callback && typeof e.callback === "function") e.callback(e.scope, msg.data);
                    // this.commonService.safeApply(e.scope);
                });
            }
        };

        this.connection.onclose = () => {
            this.isDead = true;
            this.networkState = '断开';
            listener.forEach(e => {
                e.scope.networkState = '断开';
                e.scope.isReconnect = false;
                this.commonService.safeApply(e.scope);
            });
        };
    };

    closeConnection(code, reason) {
        this.connection.close(code || 1000, '');
    };

    registListener(name, scope, callback) {
        if (this.listener) {
            let lis = this.listener.map(e => e.name);
            if (lis.indexOf(name) !== -1) {
                this.listener.findWhere(function (item) {
                    return item.name !== name;
                });
            }
            this.listener.push({
                name: name,
                scope: scope,
                callback: callback
            });
        }
    }
}
export default ['configConsts', 'gridColumnFactoryService', 'userService', 'commonService', websocketService];
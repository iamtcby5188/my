const init = Symbol('init');

class priceDifferenceService {
    constructor(httpService, configConsts) {

        this.httpService = httpService;
        this.configConsts = configConsts;

        this[init]();
    };

    [init]() {
        console.debug('priceDifferenceService initialized.');
    };

    initCharts(dto) {

        // {"billMedium":"PAP"}

        // var headers = this.getAuthHeaders();
        return this.userService.getAuthHeadersAsync().then(headers => {
            if (!headers || !headers.token) return this.$q.resolve({ result: undefined });
            return this.httpService.postService(this.configConsts.chart_init, dto, headers);
        }, res => this.$q.reject(res)).then(res => {
            if (res && res.return_code !== 0) {
                console.error(res);
                return undefined;
            }

            return this.$q.resolve(res.result);
        }, res => {
            console.error(res);
            return this.$q.reject(res)
        }); 
    };
};

export default ['httpService', 'configConsts', priceDifferenceService];
/**
 * Created by weilai on 2016/02/16.
 * 2016/05/25 Updated 报文结构标准化
 * 2016/11/30 Updated Return promise
 * 2016/12/08 Updated
 * 2016/12/15 Updated
 */

// define(['angular', 'app'], function (angular, appModule) {
//     // ReSharper disable once InconsistentNaming
//     var HTTP_SUCCESS = "success";

//     appModule.service('httpService', 

const ERRMEG_404 = "Cannot connect to server.";

export default ['$http', '$location', '$q', 'configConsts',

    function ($http, $location, $q, servicePathConst) {

        function succeedCallback(defer) {
            return function (data, status, headers, config) {
                if (!data) {
                    defer.reject(data, status);
                    return undefined;
                }

                if (data.return_code === -1) {
                    console.log(JSON.stringify(data));
                    console.log(JSON.stringify(config));

                    defer.reject(data, status);
                    return data;
                }

                defer.resolve(data, status, headers, config);
                return data;
            };
        };

        function failedCallback(defer) {
            return function (data, status, headers, config) {
                if (!data) console.log(ERRMEG_404);
                else console.log(JSON.stringify(data));

                console.log(JSON.stringify(config));

                defer.reject(data, status);
                return data;
            };
        };

        // Http
        this.http = function (method, url, params, headers) {

            var defer = $q.defer();

            var fullUrl = servicePathConst.service_root ? servicePathConst.service_root + url : url;

            var config = { method: method, url: fullUrl, dataType: 'JSON', params: params };

            if (headers) config.headers = headers;

            $http(config).then(succeedCallback(defer), failedCallback(defer));

            return defer.promise;
        };

        // Get Method
        this.getService = function (url, params, headers) {

            var defer = $q.defer();

            var config = { params: params };

            if (headers) config.headers = headers;

            var fullUrl = servicePathConst.service_root ? servicePathConst.service_root + url : url;

            $http.get(fullUrl, config).success(succeedCallback(defer)).error(failedCallback(defer));

            return defer.promise;
        };

        // Post Method
        this.postService = function (url, params, headers) {

            var defer = $q.defer();

            var fullUrl = servicePathConst.service_root ? servicePathConst.service_root + url : url;

            var config = headers ? { headers: headers } : undefined;

            $http.post(fullUrl, params, config).success(succeedCallback(defer)).error(failedCallback(defer));

            return defer.promise;
        };

        var uniqueRequestMap = new Map();

        this.unique = function (id, dto, fn) {

            if (uniqueRequestMap.has(id)) {

                var req = uniqueRequestMap.get(id);

                if (req.dto && angular.equals(req.dto, dto)) { 
                    return $q.resolve(angular.copy(req.response));
                } else {

                    return fn(dto).then(res => {
                        uniqueRequestMap.set(id, { dto: dto, fn: fn, response: res });
                        return $q.resolve(angular.copy(res));
                    }, res => {
                        return $q.reject(res);
                    });
                }
            } else {
                return fn(dto).then(res => {
                    uniqueRequestMap.set(id, { dto: dto, fn: fn, response: res });
                    return $q.resolve(angular.copy(res));
                }, res => {
                    return $q.reject(res);
                });;
            }
        };

    }];


//         );
// });
